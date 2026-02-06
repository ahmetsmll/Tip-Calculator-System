import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TipCalculationFrame extends JFrame {

    private JTable tipTable;
    private DefaultTableModel tableModel;
    private double totalTipAmount;

    public TipCalculationFrame(double totalTipAmount) {
        this.totalTipAmount = totalTipAmount;
        setTitle("Tip Calculation Results");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Name", "Employment Type", "Points", "Days Worked", "Tip Amount (TL)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tipTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(tipTable);
        add(scrollPane, BorderLayout.CENTER);

        // Delete button
        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedEmployee();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadEmployeeData();

        setSize(800, 400);
        setLocationRelativeTo(null);
    }

    private void deleteSelectedEmployee() {
        int selectedRow = tipTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int employeeId = (int) tableModel.getValueAt(selectedRow, 0); // ID'yi al

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this employee?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement("DELETE FROM employees WHERE id = ?")) {

                ps.setInt(1, employeeId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Employee deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadEmployeeData(); // Tabloyu yenile
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting employee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadEmployeeData() {
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM employees")) {

            while (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("id"));
                emp.setName(rs.getString("name"));
                emp.setFullTime(rs.getBoolean("is_full_time"));
                emp.setPoints(rs.getInt("points"));
                emp.setDaysWorked(rs.getInt("days_worked"));
                employees.add(emp);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Part-time çalışanların toplam bahşişini hesapla
        double totalPartTimeTip = 0;
        for (Employee emp : employees) {
            if (!emp.isFullTime()) {
                double dailyRate = (double) emp.getPoints() / 7; // Günlük oran
                double tip = dailyRate * emp.getDaysWorked() * (totalTipAmount / getTotalPoints(employees));
                emp.setTipAmount(tip);
                totalPartTimeTip += tip;
            }
        }

        // Kalan bahşiş miktarını hesapla
        double remainingTipAmount = totalTipAmount - totalPartTimeTip;

        // Full-time çalışanların toplam puanını hesapla
        double totalFullTimePoints = getTotalFullTimePoints(employees);

        // Full-time çalışanlara bahşiş dağıt
        for (Employee emp : employees) {
            if (emp.isFullTime()) {
                double tip;
                if (totalFullTimePoints > 0) {
                    tip = (emp.getPoints() / totalFullTimePoints) * remainingTipAmount;
                } else {
                    tip = 0; // Eğer hiç full-time çalışan yoksa
                }
                emp.setTipAmount(tip);
            }
        }

        // Tabloyu güncelle
        updateTable(employees);
    }

    private double getTotalPoints(List<Employee> employees) {
        double totalPoints = 0;
        for (Employee emp : employees) {
            totalPoints += emp.getPoints();
        }
        return totalPoints;
    }

    private double getTotalFullTimePoints(List<Employee> employees) {
        double totalPoints = 0;
        for (Employee emp : employees) {
            if (emp.isFullTime()) {
                totalPoints += emp.getPoints();
            }
        }
        return totalPoints;
    }

    private void updateTable(List<Employee> employees) {
        tableModel.setRowCount(0); // Tabloyu temizle
        for (Employee emp : employees) {
            Object[] row = {
                    emp.getId(),
                    emp.getName(),
                    emp.isFullTime() ? "Full-time" : "Part-time",
                    emp.getPoints(),
                    emp.isFullTime() ? "-" : emp.getDaysWorked(),
                    String.format("%.2f", emp.getTipAmount())
            };
            tableModel.addRow(row);
        }
    }

    private static class Employee {
        private int id;
        private String name;
        private boolean fullTime;
        private int points;
        private int daysWorked;
        private double tipAmount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isFullTime() {
            return fullTime;
        }

        public void setFullTime(boolean fullTime) {
            this.fullTime = fullTime;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public int getDaysWorked() {
            return daysWorked;
        }

        public void setDaysWorked(int daysWorked) {
            this.daysWorked = daysWorked;
        }

        public double getTipAmount() {
            return tipAmount;
        }

        public void setTipAmount(double tipAmount) {
            this.tipAmount = tipAmount;
        }
    }
}

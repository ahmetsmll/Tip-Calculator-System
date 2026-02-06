import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmployeeEntryFrame extends JFrame {

    private JTextField nameField;
    private JComboBox<String> employmentTypeComboBox;
    private JTextField pointsField;
    private JTextField daysWorkedField;
    private JButton addEmployeeButton;
    private JButton calculateTipsButton;
    private double tipAmount;

    public EmployeeEntryFrame(double tipAmount) {
        this.tipAmount = tipAmount;
        setTitle("Enter Employee Information");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(0, 2));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);

        JLabel employmentTypeLabel = new JLabel("Employment Type:");
        employmentTypeComboBox = new JComboBox<>(new String[]{"Full-time", "Part-time"});

        JLabel pointsLabel = new JLabel("Points:");
        pointsField = new JTextField(5);

        JLabel daysWorkedLabel = new JLabel("Days Worked (Part-time):");
        daysWorkedField = new JTextField(5);
        daysWorkedField.setEnabled(false); // Başlangıçta devre dışı

        employmentTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (employmentTypeComboBox.getSelectedItem().equals("Part-time")) {
                    daysWorkedField.setEnabled(true);
                } else {
                    daysWorkedField.setEnabled(false);
                }
            }
        });

        addEmployeeButton = new JButton("Add Employee");
        addEmployeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployeeToDatabase();
            }
        });

        calculateTipsButton = new JButton("Calculate Tips");
        calculateTipsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Tip hesaplama ekranına geçiş
                TipCalculationFrame tipCalculationFrame = new TipCalculationFrame(tipAmount);
                tipCalculationFrame.setVisible(true);
                dispose();
            }
        });

        add(nameLabel);
        add(nameField);
        add(employmentTypeLabel);
        add(employmentTypeComboBox);
        add(pointsLabel);
        add(pointsField);
        add(daysWorkedLabel);
        add(daysWorkedField);
        add(addEmployeeButton);
        add(calculateTipsButton);

        pack();
        setLocationRelativeTo(null);
    }

    private void addEmployeeToDatabase() {
        String name = nameField.getText();
        String employmentType = (String) employmentTypeComboBox.getSelectedItem();
        int points = 0;
        int daysWorked = 0;

        try {
            points = Integer.parseInt(pointsField.getText());
            if (employmentType.equals("Part-time")) {
                daysWorked = Integer.parseInt(daysWorkedField.getText());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(EmployeeEntryFrame.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isFullTime = employmentType.equals("Full-time");

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO employees (name, is_full_time, points, days_worked) VALUES (?, ?, ?, ?)"
             )) {

            preparedStatement.setString(1, name);
            preparedStatement.setBoolean(2, isFullTime);
            preparedStatement.setInt(3, points);
            if (isFullTime) {
                preparedStatement.setNull(4, java.sql.Types.INTEGER);
            } else {
                preparedStatement.setInt(4, daysWorked);
            }

            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(EmployeeEntryFrame.this, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Alanları temizle
            nameField.setText("");
            pointsField.setText("");
            daysWorkedField.setText("");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(EmployeeEntryFrame.this, "Error adding employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

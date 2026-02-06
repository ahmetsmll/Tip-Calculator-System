import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TipAmountFrame extends JFrame {

    private JTextField tipAmountField;
    private JButton nextButton;

    public TipAmountFrame() {
        setTitle("Enter Tip Amount");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel tipAmountLabel = new JLabel("Enter Tip Amount:");
        tipAmountField = new JTextField(10);

        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double tipAmount = Double.parseDouble(tipAmountField.getText());
                    EmployeeEntryFrame employeeEntryFrame = new EmployeeEntryFrame(tipAmount);
                    employeeEntryFrame.setVisible(true);
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(TipAmountFrame.this, "Invalid tip amount!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(tipAmountLabel);
        add(tipAmountField);
        add(nextButton);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TipAmountFrame tipAmountFrame = new TipAmountFrame();
            tipAmountFrame.setVisible(true);
        });
    }
}

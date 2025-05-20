package ui;

import dao.UserDAO;
import model.Customer;
import javax.swing.*;
import java.awt.*;

/**
 * Dashboard for logged-in Customer.
 */
public class CustomerDashboard extends JFrame {
    private Customer customer;
    private JLabel balanceLabel;

    public CustomerDashboard(Customer customer) {
        this.customer = customer;
        setTitle("Customer Dashboard - Welcome " + customer.getUsername());
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // UI Components
        balanceLabel = new JLabel("Current Balance: ₹" + customer.getBalance());
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton logoutButton = new JButton("Logout");

        // Action Listeners
        depositButton.addActionListener(e -> performTransaction(true));
        withdrawButton.addActionListener(e -> performTransaction(false));
        logoutButton.addActionListener(e -> logout());

        // Layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.add(balanceLabel);
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(logoutButton);

        add(panel);
        setVisible(true);
    }

    /**
     * Handles deposit or withdrawal.
     */
    private void performTransaction(boolean isDeposit) {
        String action = isDeposit ? "Deposit" : "Withdraw";
        String input = JOptionPane.showInputDialog(this, action + " Amount:");

        try {
            double amount = Double.parseDouble(input);

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Enter a positive amount.");
                return;
            }

            double newBalance = isDeposit
                    ? customer.getBalance() + amount
                    : customer.getBalance() - amount;

            if (!isDeposit && newBalance < 0) {
                JOptionPane.showMessageDialog(this, "Insufficient balance.");
                return;
            }

            boolean success = UserDAO.updateBalance(customer.getId(), newBalance);

            if (success) {
                customer.setBalance(newBalance);
                balanceLabel.setText("Current Balance: ₹" + newBalance);
                JOptionPane.showMessageDialog(this, action + " successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Transaction failed. Try again.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount entered.");
        }
    }

    /**
     * Logs the user out and returns to the login screen.
     */
    private void logout() {
        JOptionPane.showMessageDialog(this, "Logged out.");
        dispose();
        new LoginFrame();
    }
}

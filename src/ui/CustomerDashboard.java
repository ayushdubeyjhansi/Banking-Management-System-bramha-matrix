package ui;

import model.Customer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CustomerDashboard extends JFrame {
    private Customer customer;

    public CustomerDashboard(Customer customer) {
        this.customer = customer;
        setTitle("Customer Dashboard");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel header = new JLabel("Welcome, " + customer.getUsername() + "!", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setOpaque(true);
        header.setBackground(new Color(0, 120, 215));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(header, BorderLayout.NORTH);

        // Center panel for info and actions
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Account Info Panel
        JPanel accountPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        accountPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Account Details", TitledBorder.LEFT, TitledBorder.TOP));
        accountPanel.add(new JLabel("Username: " + customer.getUsername()));
        accountPanel.add(new JLabel("User ID: " + customer.getId()));
        accountPanel.add(new JLabel("Balance: ₹ " + customer.getBalance()));
        centerPanel.add(accountPanel);

        // Action Buttons Panel
        JPanel actionPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        actionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Actions", TitledBorder.LEFT, TitledBorder.TOP));

        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton checkBalanceBtn = new JButton("Check Balance");
        JButton logoutBtn = new JButton("Logout");

        actionPanel.add(depositBtn);
        actionPanel.add(withdrawBtn);
        actionPanel.add(checkBalanceBtn);
        actionPanel.add(logoutBtn);
        centerPanel.add(actionPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Logout Action
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }
}

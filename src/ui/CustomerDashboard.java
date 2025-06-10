package ui;

import dao.TransactionDAO;
import model.Customer;
import model.Transaction;
import db.DBConnection;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

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
        header.setBackground(new Color(0, 172, 215));
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
        JPanel actionPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        actionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), "Actions", TitledBorder.LEFT, TitledBorder.TOP));

        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton checkBalanceBtn = new JButton("Check Balance");
        JButton viewTransactionsBtn = new JButton("View Transactions");
        JButton logoutBtn = new JButton("Logout");

        actionPanel.add(depositBtn);
        actionPanel.add(withdrawBtn);
        actionPanel.add(checkBalanceBtn);
        actionPanel.add(viewTransactionsBtn);
        actionPanel.add(logoutBtn);
        centerPanel.add(actionPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Button Actions
        depositBtn.addActionListener(e -> performTransaction("deposit"));
        withdrawBtn.addActionListener(e -> performTransaction("withdraw"));

        checkBalanceBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this, "Current Balance: ₹" + customer.getBalance()));

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        viewTransactionsBtn.addActionListener(e -> showTransactions());

        setVisible(true);
    }

    private void showTransactions() {
        try (Connection conn = DBConnection.getConnection()) {
            TransactionDAO transactionDAO = new TransactionDAO(conn);
            List<Transaction> transactions = transactionDAO.getTransactionsByUserId(customer.getId());

            String[] columnNames = {"Transaction ID", "Type", "Amount", "Date"};
            Object[][] data = new Object[transactions.size()][4];

            for (int i = 0; i < transactions.size(); i++) {
                Transaction txn = transactions.get(i);
                data[i][0] = txn.getTxnId();
                data[i][1] = txn.getType();
                data[i][2] = txn.getAmount();
                data[i][3] = txn.getTxnDate();
            }

            JTable table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);

            JFrame frame = new JFrame("My Transactions");
            frame.add(scrollPane);
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading transactions: " + ex.getMessage());
        }
    }

    private void performTransaction(String type) {
        String message = type.equals("deposit") ? "Enter amount to deposit:" : "Enter amount to withdraw:";
        String input = JOptionPane.showInputDialog(this, message);

        if (input != null && !input.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(input);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive.");
                    return;
                }

                try (Connection conn = DBConnection.getConnection()) {
                    conn.setAutoCommit(false);
                    PreparedStatement updateBalanceStmt;

                    if (type.equals("withdraw")) {
                        if (customer.getBalance() < amount) {
                            JOptionPane.showMessageDialog(this, "Insufficient balance.");
                            return;
                        }
                        updateBalanceStmt = conn.prepareStatement("UPDATE users SET balance = balance - ? WHERE id = ?");
                        customer.setBalance(customer.getBalance() - amount);
                    } else {
                        updateBalanceStmt = conn.prepareStatement("UPDATE users SET balance = balance + ? WHERE id = ?");
                        customer.setBalance(customer.getBalance() + amount);
                    }

                    updateBalanceStmt.setDouble(1, amount);
                    updateBalanceStmt.setInt(2, customer.getId());
                    updateBalanceStmt.executeUpdate();

                    TransactionDAO txnDAO = new TransactionDAO(conn);
                    txnDAO.addTransaction(customer.getId(), type, amount);

                    conn.commit();
                    JOptionPane.showMessageDialog(this, type + " successful! Updated Balance: ₹" + customer.getBalance());

                    // Refresh UI
                    this.dispose();
                    new CustomerDashboard(customer);

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error during transaction: " + ex.getMessage());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount entered.");
            }
        }
    }
}

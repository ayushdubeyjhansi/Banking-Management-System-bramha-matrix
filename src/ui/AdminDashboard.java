package ui;

import dao.UserDAO;
import model.Admin;
import model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Dashboard for Admin to manage users.
 */
public class AdminDashboard extends JFrame {
    private Admin admin;
    private JTable userTable;

    public AdminDashboard(Admin admin) {
        this.admin = admin;
        setTitle("Admin Dashboard - Welcome " + admin.getUsername());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // UI: User Table
        userTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(userTable);

        // Buttons
        JButton refreshButton = new JButton("Refresh Users");
        JButton deleteButton = new JButton("Delete Selected User");
        JButton logoutButton = new JButton("Logout");

        refreshButton.addActionListener(e -> loadUsers());
        deleteButton.addActionListener(e -> deleteUser());
        logoutButton.addActionListener(e -> logout());

        // Layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(logoutButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadUsers(); // Load users at startup
        setVisible(true);
    }
    // Inside your AdminDashboard class

    private void viewAllTransactions() {
        try {
            Connection conn = db.DBConnection.getConnection();
            dao.TransactionDAO transactionDAO = new dao.TransactionDAO(conn);
            java.util.List<model.Transaction> transactions = transactionDAO.getAllTransactions();

            String[] columnNames = {"Transaction ID", "User ID", "Type", "Amount", "Date"};
            Object[][] data = new Object[transactions.size()][5];

            for (int i = 0; i < transactions.size(); i++) {
                model.Transaction txn = transactions.get(i);
                data[i][0] = txn.getTxnId();
                data[i][1] = txn.getUserId();
                data[i][2] = txn.getType();
                data[i][3] = txn.getAmount();
                data[i][4] = txn.getTxnDate();
            }

            javax.swing.JTable table = new javax.swing.JTable(data, columnNames);
            javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(table);
            javax.swing.JFrame frame = new javax.swing.JFrame("All Transactions");
            frame.add(scrollPane);
            frame.setSize(800, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        } catch (SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error fetching transactions: " + e.getMessage());
        }
    }


    /**
     * Loads all customer users into the table.
     */
    private void loadUsers() {
        List<Customer> customers = UserDAO.getAllCustomers();

        String[] columnNames = {"ID", "Username", "Balance"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (Customer c : customers) {
            Object[] row = {c.getId(), c.getUsername(), "₹" + c.getBalance()};
            model.addRow(row);
        }

        userTable.setModel(model);
    }

    /**
     * Deletes the selected customer from the database.
     */
    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
            return;
        }

        int userId = (int) userTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete user ID " + userId + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = UserDAO.deleteUserById(userId);
            if (success) {
                JOptionPane.showMessageDialog(this, "User deleted successfully.");
                loadUsers(); // Refresh list
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user.");
            }
        }
    }

    /**
     * Logs out and returns to login.
     */
    private void logout() {
        JOptionPane.showMessageDialog(this, "Logged out.");
        dispose();
        new LoginFrame();
    }
}


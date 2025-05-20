package ui;

import dao.UserDAO;
import model.Admin;
import model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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


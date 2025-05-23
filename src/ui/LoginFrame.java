package ui;

import dao.UserDAO;
import model.Admin;
import model.Customer;
import model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Login - BRAMHA MATRIX BANK");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Bank Title Header
        JLabel bankTitle = new JLabel("BRAMHA MATRIX BANK", JLabel.CENTER);
        bankTitle.setFont(new Font("Serif", Font.BOLD, 24));
        bankTitle.setOpaque(true);
        bankTitle.setBackground(new Color(0, 102, 204));
        bankTitle.setForeground(Color.WHITE);
        bankTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(bankTitle, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        formPanel.add(new JLabel()); // Empty label for spacing
        formPanel.add(loginButton);

        add(formPanel, BorderLayout.CENTER);

        // Login button action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            User user = UserDAO.login(username, password);
            if (user != null) {
                dispose();
                if (user instanceof Customer) {
                    new CustomerDashboard((Customer) user);
                } else if (user instanceof Admin) {
                    new AdminDashboard((Admin) user);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}

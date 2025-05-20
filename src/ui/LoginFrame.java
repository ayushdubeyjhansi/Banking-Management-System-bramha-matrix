package ui;

import dao.UserDAO;
import model.Admin;
import model.User;
import model.Customer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login screen for the Multi-user Banking System.
 */
public class LoginFrame extends JFrame {

    private JTextField userField;
    private JPasswordField passField;

    public LoginFrame() {
        setTitle("Banking System Login");
        setSize(350, 200);
        setLocationRelativeTo(null); // Center screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(30, 30, 80, 25);
        add(userLabel);

        userField = new JTextField();
        userField.setBounds(120, 30, 180, 25);
        add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(30, 70, 80, 25);
        add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(120, 70, 180, 25);
        add(passField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(120, 110, 100, 30);
        add(loginButton);

        // Event handling
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText().trim();
                String password = new String(passField.getPassword()).trim();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter both fields.");
                    return;
                }

                User user = UserDAO.login(username, password);

                if (user != null) {
                    if (user instanceof Admin) {
                        Admin admin = (Admin) user;
                        new AdminDashboard(admin);
                        dispose(); // close login window
                    } else if (user instanceof Customer) {
                        new CustomerDashboard((Customer) user);
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials. Try again.");
                }
            }
        });

        setVisible(true);
    }
}

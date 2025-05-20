package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class handles the connection between the Java app and MySQL database.
 */
public class DBConnection {
    // Database URL
    private static final String URL = "jdbc:mysql://localhost:3306/banking_system";
    private static final String USER = "root"; // change this if your MySQL username is different
    private static final String PASSWORD = "Ayush##PRO19"; // replace with your MySQL password

    /**
     * This method returns a Connection object to the database.
     */
    public static Connection getConnection() {
        try {
            // Establish and return the connection
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
            return null;
        }
    }
}

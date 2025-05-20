import java.sql.Connection;
import com.mysql.cj.jdbc.Driver;
import javax.swing.SwingUtilities;
import ui.LoginFrame;

/**
 * Entry point for the Multi-user Banking System.
 */
public class Main {
    public static void main(String[] args) {
        // Run GUI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            new LoginFrame();  // Open login window
        });
    }
}


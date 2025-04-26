import java.sql.*;
import java.util.concurrent.locks.*;

public class ReportService {
    private static final Lock reportLock = new ReentrantLock();

    public static void generateReport() {
        reportLock.lock();
        try (Connection conn = DriverManager.getConnection(DatabaseManager.DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM accounts")) {

            System.out.println("\n[REPORT] Current Balances:");
            while (rs.next()) {
                System.out.println("Student: " + rs.getString("student_id") + 
                                 ", Balance: $" + rs.getDouble("balance"));
            }
        } catch (SQLException e) {
            System.err.println("Report generation failed: " + e.getMessage());
        } finally {
            reportLock.unlock();
        }
    }
}
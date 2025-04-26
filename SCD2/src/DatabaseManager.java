import java.sql.*;

public class DatabaseManager {
    static final String DB_URL = "jdbc:sqlite:fee_system.db";

    public static void initializeDB() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS accounts (" +
                    "student_id TEXT PRIMARY KEY," +
                    "balance REAL DEFAULT 0)");
            stmt.execute("CREATE TABLE IF NOT EXISTS transactions (" +
                    "txn_id TEXT PRIMARY KEY," +
                    "student_id TEXT," +
                    "amount REAL," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
        } catch (SQLException e) {
            System.err.println("DB Initialization failed: " + e.getMessage());
        }
    }

    public static void updateBalance(String studentId, double amount, String txnId) {
        String updateSQL = "UPDATE accounts SET balance = balance + ? WHERE student_id = ?";
        String insertSQL = "INSERT INTO transactions (txn_id, student_id, amount) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
             PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {

            conn.setAutoCommit(false); // Start transaction

            updateStmt.setDouble(1, amount);
            updateStmt.setString(2, studentId);
            updateStmt.executeUpdate();

            insertStmt.setString(1, txnId);
            insertStmt.setString(2, studentId);
            insertStmt.setDouble(3, amount);
            insertStmt.executeUpdate();

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            System.err.println("Transaction failed: " + e.getMessage());
        }
    }
}
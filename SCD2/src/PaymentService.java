import java.util.concurrent.*;

public class PaymentService {
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void submitPayment(String studentId, double amount, String txnId) {
        executor.submit(() -> {
            synchronized (PaymentService.class) { // Ensure atomicity
                DatabaseManager.updateBalance(studentId, amount, txnId);
                System.out.println("[PAYMENT] Student: " + studentId + ", Amount: $" + amount);
            }
        });
    }

    public static void shutdown() {
        executor.shutdown();
    }
}
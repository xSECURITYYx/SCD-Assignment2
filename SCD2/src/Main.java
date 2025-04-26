import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.initializeDB();

        // Simulate concurrent fee submissions
        for (int i = 1; i <= 20; i++) {
            String studentId = "S" + (100 + i);
            double amount = 50 + (i * 10);
            String txnId = UUID.randomUUID().toString();
            PaymentService.submitPayment(studentId, amount, txnId);
        }

        // Simulate admin generating a report
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Simulate delay
                ReportService.generateReport();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        PaymentService.shutdown();
    }
}
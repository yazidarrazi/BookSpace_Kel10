/**
 * Interface strategi untuk implementasi berbagai metode pembayaran.
 * Memastikan setiap metode bayar memiliki fungsi pemrosesan yang seragam.
 */
interface PaymentMethod {
    String getPaymentName();
    void processPayment(double amount);
}

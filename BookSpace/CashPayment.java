/**
 * Implementasi pembayaran Tunai/COD.
 */
class CashPayment implements PaymentMethod {
    @Override public String getPaymentName() { return "COD (Bayar di Tempat)"; }
    @Override public void processPayment(double amount) {}
}

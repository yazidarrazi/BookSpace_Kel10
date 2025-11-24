/**
 * Implementasi pembayaran via Transfer Bank.
 */
class BankTransferPayment implements PaymentMethod {
    private final String bankName;
    public BankTransferPayment(String bankName) { this.bankName = bankName; }
    @Override public String getPaymentName() { return "Transfer Bank " + bankName; }
    @Override public void processPayment(double amount) {
        Color.printSuccess("Transfer ke Bank " + bankName + " senilai Rp" + String.format("%,.0f", amount) + " berhasil.");
    }
}

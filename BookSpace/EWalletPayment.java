/**
 * Implementasi pembayaran via E-Wallet.
 */
class EWalletPayment implements PaymentMethod {
    private final String providerName;
    public EWalletPayment(String providerName) { this.providerName = providerName; }
    @Override public String getPaymentName() { return "E-Wallet (" + providerName + ")"; }
    @Override public void processPayment(double amount) {
        Color.printSuccess("Saldo E-Wallet " + providerName + " didebit sebesar Rp" + String.format("%,.0f", amount) + ".");
    }
}

/**
 * Implementasi pembayaran placeholder (dummy).
 * Digunakan saat membaca riwayat transaksi lama dari file teks,
 * di mana logika pembayaran asli (seperti potong saldo) tidak perlu dijalankan ulang.
 */
class HistoryPayment implements PaymentMethod {
    private final String name;
    
    public HistoryPayment(String name) { this.name = name; }
    
    @Override public String getPaymentName() { return name; }
    
    @Override public void processPayment(double amount) {
        // Tidak melakukan apa-apa karena ini hanya riwayat
    }
}

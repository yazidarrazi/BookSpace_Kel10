/**
 * Exception kustom (Checked Exception) yang dilempar ketika stok item
 * tidak mencukupi untuk memenuhi permintaan pembelian.
 */
class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) { super(message); }
}

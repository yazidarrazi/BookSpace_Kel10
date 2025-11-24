/**
 * Representasi item dalam keranjang atau transaksi.
 * Menghubungkan entitas Buku dengan kuantitas pembelian.
 */
class CartItem {
    private final Book book;
    private int jumlah;
    
    public CartItem(Book book, int jumlah) {
        this.book = book;
        this.jumlah = jumlah;
    }
    
    public Book getBook() { return book; }
    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    
    /**
     * Menghitung subtotal harga (Harga Satuan * Kuantitas).
     * @return Nilai subtotal.
     */
    public double getSubtotal() { return book.getHarga() * jumlah; }
    
    public void displayInfo() {
        System.out.printf("%-25s | x%-3d | = " + Color.GREEN + "Rp%,.0f" + Color.RESET + "\n", 
                book.getJudul(), jumlah, getSubtotal());
    }
}

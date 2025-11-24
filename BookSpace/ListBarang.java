import java.util.ArrayList;

/**
 * Kelas repositori untuk manajemen koleksi buku.
 * Menyediakan metode CRUD in-memory.
 */
class ListBarang {
    private ArrayList<Book> barang;
    
    public ListBarang() {
        this.barang = new ArrayList<>();
    }
    
    public void tambahBarang(Book book) {
        barang.add(book);
    }
    
    /**
     * Mencari buku berdasarkan ID unik.
     * @param id ID Buku.
     * @return Objek Book jika ditemukan, null jika tidak.
     */
    public Book cariBarang(String id) {
        return barang.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public ArrayList<Book> lihatSemua() {
        return barang;
    }
    
    public boolean hapusBarang(String id) {
        return barang.removeIf(b -> b.getId().equals(id));
    }
    
    public void tampilkanSemua() {
        System.out.println(Color.CYAN + "\n=== KATALOG BUKU BOOKSPACE ===" + Color.RESET);
        if (barang.isEmpty()) {
            Color.printInfo("Katalog kosong.");
        } else {
            System.out.println("----------------------------------------------------------------------------------------------");
            System.out.printf("| %-5s | %-25s | %-15s | %-10s | %-14s | %-4s |\n", "ID", "JUDUL", "PENULIS", "KATEGORI", "HARGA", "STOK");
            System.out.println("----------------------------------------------------------------------------------------------");
            barang.forEach(Book::displayInfo);
            System.out.println("----------------------------------------------------------------------------------------------");
        }
    }
}import java.util.ArrayList;

/**
 * Kelas repositori untuk manajemen koleksi buku.
 * Menyediakan metode CRUD in-memory.
 */
class ListBarang {
    private ArrayList<Book> barang;
    
    public ListBarang() {
        this.barang = new ArrayList<>();
    }
    
    public void tambahBarang(Book book) {
        barang.add(book);
    }
    
    /**
     * Mencari buku berdasarkan ID unik.
     * @param id ID Buku.
     * @return Objek Book jika ditemukan, null jika tidak.
     */
    public Book cariBarang(String id) {
        return barang.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public ArrayList<Book> lihatSemua() {
        return barang;
    }
    
    public boolean hapusBarang(String id) {
        return barang.removeIf(b -> b.getId().equals(id));
    }
    
    public void tampilkanSemua() {
        System.out.println(Color.CYAN + "\n=== KATALOG BUKU BOOKSPACE ===" + Color.RESET);
        if (barang.isEmpty()) {
            Color.printInfo("Katalog kosong.");
        } else {
            System.out.println("----------------------------------------------------------------------------------------------");
            System.out.printf("| %-5s | %-25s | %-15s | %-10s | %-14s | %-4s |\n", "ID", "JUDUL", "PENULIS", "KATEGORI", "HARGA", "STOK");
            System.out.println("----------------------------------------------------------------------------------------------");
            barang.forEach(Book::displayInfo);
            System.out.println("----------------------------------------------------------------------------------------------");
        }
    }
}

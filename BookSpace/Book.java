/**
 * Kelas abstrak yang merepresentasikan entitas Buku.
 * Menyediakan properti dasar dan metode manipulasi stok.
 */
abstract class Book {
    private final String id;
    private String judul;
    private String penulis;
    private double harga;
    private int stok;
    
    public Book(String id, String judul, String penulis, double harga, int stok) {
        this.id = id;
        this.judul = judul;
        this.penulis = penulis;
        this.harga = harga;
        this.stok = stok;
    }
    
    public String getId() { return id; }
    public String getJudul() { return judul; }
    public String getPenulis() { return penulis; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }
    
    public void setJudul(String judul) { this.judul = judul; }
    public void setHarga(double harga) { this.harga = harga; }
    public void setStok(int stok) { this.stok = stok; }
    
    /**
     * Mengurangi stok buku secara thread-safe (logika bisnis).
     * @param jumlah Jumlah yang akan dikurangi.
     * @throws InsufficientStockException Jika stok tidak mencukupi.
     */
    public void kurangiStok(int jumlah) throws InsufficientStockException {
        if (stok < jumlah) {
            throw new InsufficientStockException("Stok tidak mencukupi untuk item: " + judul);
        }
        this.stok -= jumlah;
    }
    
    public abstract String getKategori();
    
    /**
     * Menampilkan informasi buku terformat ke konsol.
     */
    public void displayInfo() {
        String colorStok = (stok < 5) ? Color.RED : Color.GREEN;
        
        System.out.printf("| %-5s | %-25s | %-15s | %-10s | " + Color.CYAN + "Rp%,12.0f" + Color.RESET + " | %s%-4d%s |\n",
                id, 
                (judul.length() > 25 ? judul.substring(0, 22) + "..." : judul), 
                (penulis.length() > 15 ? penulis.substring(0, 12) + "..." : penulis),
                getKategori(), 
                harga, 
                colorStok, stok, Color.RESET);
    }
    
    /**
     * Mengonversi objek buku menjadi format CSV string.
     * @return String CSV.
     */
    public String toCSV() {
        String tipe = "UNKNOWN";
        if (this instanceof Novel) tipe = "NOVEL";
        else if (this instanceof Komik) tipe = "KOMIK";
        else if (this instanceof Majalah) tipe = "MAJALAH";
        return String.format("%s;%s;%s;%s;%.0f;%d", tipe, id, judul, penulis, harga, stok);
    }
}

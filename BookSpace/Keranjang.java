import java.util.ArrayList;
import java.util.List;

/**
 * Kelas yang mengelola item belanja sementara milik pelanggan.
 */
class Keranjang {
    private ArrayList<CartItem> barang;
    
    public Keranjang() {
        this.barang = new ArrayList<>();
    }
    
    /**
     * Menambahkan item ke keranjang. 
     * Jika item sudah ada, kuantitas akan diakumulasikan.
     * @param item Objek CartItem yang akan ditambahkan.
     */
    public void tambah(CartItem item) {
        for (CartItem existing : barang) {
            if (existing.getBook().getId().equals(item.getBook().getId())) {
                existing.setJumlah(existing.getJumlah() + item.getJumlah());
                return;
            }
        }
        barang.add(item);
    }
    
    public ArrayList<CartItem> getBarang() {
        return barang;
    }
    
    public void lihatBarang() {
        System.out.println(Color.CYAN + "\n=== KERANJANG BELANJA ===" + Color.RESET);
        if (barang.isEmpty()) {
            Color.printInfo("Keranjang kosong.");
        } else {
            double total = 0;
            int index = 1;
            for (CartItem item : barang) {
                System.out.print(index + ". ");
                item.displayInfo();
                total += item.getSubtotal();
                index++;
            }
            System.out.println("----------------------------------------");
            System.out.printf(Color.WHITE_BOLD + "Estimasi Total: Rp%,.0f" + Color.RESET + "\n", total);
        }
        System.out.println("========================================");
    }
    
    public void hapusBarang(List<CartItem> items) {
        barang.removeAll(items);
    }
    
    public void kosongkan() {
        barang.clear();
    }
}

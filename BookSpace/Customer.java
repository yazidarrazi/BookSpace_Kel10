/**
 * Representasi pengguna dengan peran Pelanggan (Customer).
 * Memiliki fitur keranjang belanja dan riwayat transaksi.
 */
class Customer extends User {
    private Keranjang keranjang;
    
    public Customer(String username, String password) {
        super(username, password);
        this.keranjang = new Keranjang();
    }
    
    /**
     * Mengembalikan objek keranjang belanja milik pelanggan.
     * @return Objek {@link Keranjang}.
     */
    public Keranjang getKeranjang() { return keranjang; }
    
    @Override
    public void displayMenu() {
        Color.printMenuHeader("AREA PELANGGAN (" + getUsername() + ")", Color.PURPLE);
        System.out.println("1. Jelajahi Katalog");
        System.out.println("2. " + Color.CYAN + "Tambah ke Keranjang" + Color.RESET);
        System.out.println("3. Manajemen Keranjang");
        System.out.println("4. " + Color.GREEN + "Checkout" + Color.RESET);
        System.out.println("5. Riwayat Pembelian");
        System.out.println("6. Pengaturan Akun");
        System.out.println("7. " + Color.RED + "Keluar" + Color.RESET);
    }
}

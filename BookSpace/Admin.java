/**
 * Representasi pengguna dengan peran Administrator.
 * Memiliki akses ke fitur manajemen konten dan validasi transaksi.
 */
class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
    }
    
    @Override
    public void displayMenu() {
        Color.printMenuHeader("DASHBOARD ADMIN (" + getUsername() + ")", Color.BLUE);
        System.out.println("1. " + Color.YELLOW + "Tambah Buku Baru" + Color.RESET);
        System.out.println("2. " + Color.YELLOW + "Perbarui Data Buku" + Color.RESET);
        System.out.println("3. " + Color.YELLOW + "Hapus Buku" + Color.RESET);
        System.out.println("4. Lihat Katalog Buku");
        System.out.println("5. Lihat Transaksi Tertunda");
        System.out.println("6. " + Color.GREEN + "Validasi Transaksi" + Color.RESET);
        System.out.println("7. Pengaturan Akun");
        System.out.println("8. " + Color.RED + "Keluar" + Color.RESET);
    }
}

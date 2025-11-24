/**
 * Kelas abstrak yang merepresentasikan entitas pengguna dalam sistem.
 * Mengimplementasikan {@link UserInterface} dan menyediakan fungsionalitas dasar.
 */
abstract class User implements UserInterface {
    private String username;
    private String password;
    
    /**
     * Konstruktor untuk inisialisasi pengguna.
     * @param username Nama pengguna (unik).
     * @param password Kata sandi pengguna.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    @Override
    public String getUsername() { return username; }
    
    @Override
    public String getPassword() { return password; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    
    /**
     * Menampilkan menu spesifik berdasarkan peran pengguna.
     * Harus diimplementasikan oleh kelas turunan.
     */
    @Override
    public abstract void displayMenu();
}

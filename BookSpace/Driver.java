import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * Kelas abstrak dasar untuk driver logika bisnis.
 * Menyediakan utilitas umum untuk interaksi konsol dan manajemen sesi.
 */
abstract class Driver {
    protected Scanner scanner;
    protected User currentUser;
    protected ListBarang listBarang;
    protected ArrayList<Transaction> allTransactions;
    protected Map<String, User> allUsers;
    protected Runnable saveCallback;
    
    public Driver(Scanner scanner, ListBarang listBarang, ArrayList<Transaction> transactions, Map<String, User> users, Runnable saveCallback) {
        this.scanner = scanner;
        this.listBarang = listBarang;
        this.allTransactions = transactions;
        this.allUsers = users;
        this.saveCallback = saveCallback;
    }
    
    /**
     * Mengatur sesi pengguna aktif saat ini.
     * @param user Objek pengguna yang sedang login.
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    /**
     * Metode abstrak untuk menjalankan logika utama driver.
     */
    public abstract void run();
    
    protected int getValidIntInput(int min, int max) {
        while (true) {
            System.out.print(Color.YELLOW + "Pilih Opsi (" + min + "-" + max + ") > " + Color.RESET);
            String input = scanner.nextLine();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) return value;
                else Color.printError("Input di luar jangkauan (" + min + "-" + max + ").");
            } catch (NumberFormatException e) {
                Color.printError("Input tidak valid. Harap masukkan angka.");
            }
        }
    }
    
    protected void pause() {
        System.out.print("\n" + Color.WHITE_BOLD + "[Tekan ENTER untuk melanjutkan...]" + Color.RESET);
        scanner.nextLine();
    }

    protected void changeProfile() {
        System.out.println("1. Ganti Password");
        System.out.println("2. Ganti Username");
        int choice = getValidIntInput(1, 2);

        if (choice == 1) {
            System.out.print("Password Baru: "); String pass = scanner.nextLine();
            if (!pass.isEmpty()) { 
                currentUser.setPassword(pass); 
                saveCallback.run(); 
                Color.printSuccess("Password berhasil diperbarui."); 
            }
        } else if (choice == 2) {
            System.out.print("Username Baru: "); String name = scanner.nextLine();
            if (!name.isEmpty() && !allUsers.containsKey(name)) {
                String old = currentUser.getUsername(); 
                allUsers.remove(old); 
                currentUser.setUsername(name);
                allUsers.put(name, currentUser); 
                saveCallback.run();
                Color.printSuccess("Username diperbarui. Silakan login ulang."); 
                currentUser = null; 
            } else {
                Color.printError("Username tidak valid atau sudah digunakan.");
            }
        }
    }
}

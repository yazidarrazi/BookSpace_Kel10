import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller utama yang mengatur orkestrasi seluruh aplikasi BookSpace.
 * <p>
 * Kelas ini bertanggung jawab untuk:
 * <ul>
 * <li>Manajemen siklus hidup aplikasi (Application Lifecycle).</li>
 * <li>Inisialisasi dan persistensi data (File I/O).</li>
 * <li>Pengelolaan state global pengguna (Session Management).</li>
 * <li>Routing navigasi antara antarmuka Admin dan Customer.</li>
 * </ul>
 * * @author BookSpace Team
 * @version 1.0
 */
public class Main {
    /** Penyimpanan in-memory untuk data pengguna (User). Key: Username. */
    private final Map<String, User> users;
    
    /** Repositori in-memory untuk katalog barang. */
    private final ListBarang listBarang;
    
    /** Penyimpanan in-memory untuk riwayat transaksi. */
    private final ArrayList<Transaction> transactions;
    
    /** Scanner global untuk input dari standar input stream. */
    private final Scanner scanner;
    
    /** Referensi ke pengguna yang sedang login (Session). Null jika belum login. */
    private User currentUser;
    
    // Konstanta path file untuk persistensi data
    private final String USER_FILE = "users.txt";
    private final String BOOK_FILE = "books.txt";
    private final String TRX_FILE = "transactions.txt";
    
    /** Driver untuk menangani logika bisnis spesifik Admin. */
    private AdminDriver adminDriver;
    
    /** Driver untuk menangani logika bisnis spesifik Customer. */
    private CustomerDriver customerDriver;
    
    /**
     * Titik masuk sekunder untuk menjalankan kelas Main secara independen.
     * Menginstansiasi aplikasi dan memulai metode {@link #run()}.
     * * @param args Argumen baris perintah (tidak digunakan).
     */
    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }
    
    /**
     * Konstruktor default.
     * Menginisialisasi struktur data, memuat data dari penyimpanan sekunder,
     * dan menyiapkan dependensi driver.
     */
    public Main() {
        users = new HashMap<>();
        listBarang = new ListBarang();
        transactions = new ArrayList<>();
        scanner = new Scanner(System.in);
        
        initializeData();
        
        // Callback Runnable untuk operasi penyimpanan data asinkron/sinkron
        Runnable saveAction = this::saveAll;
        
        // Injeksi dependensi ke driver
        adminDriver = new AdminDriver(scanner, listBarang, transactions, users, saveAction);
        customerDriver = new CustomerDriver(scanner, listBarang, transactions, users, saveAction);
    }
    
    /**
     * Menginisialisasi data aplikasi dengan memuat dari file teks.
     * Jika data kosong, melakukan seeding data awal (data dummy).
     */
    private void initializeData() {
        loadUsersFromFile();
        loadBooksFromFile();
        loadTransactionsFromFile();
        
        // Data Seeding jika katalog kosong
        if (listBarang.lihatSemua().isEmpty()) {
            listBarang.tambahBarang(new Novel("B01", "Laskar Pelangi", "Andrea Hirata", 85000, 10));
            listBarang.tambahBarang(new Komik("B02", "Naruto Vol. 1", "Masashi K", 25000, 15));
            listBarang.tambahBarang(new Majalah("B03", "Bobo Edisi Baru", "Grid Network", 15000, 20));
            saveBooksToFile();
        }
    }

    /**
     * Menyimpan seluruh perubahan state aplikasi ke penyimpanan persisten (File).
     * Metode ini dipanggil setiap kali terjadi perubahan data yang signifikan (Commit).
     */
    private void saveAll() {
        saveUsersToFile();
        saveBooksToFile();
        saveTransactionsToFile();
    }

    /**
     * Membaca data pengguna dari file {@code users.txt}.
     * Membuat file baru dengan akun default jika file tidak ditemukan.
     */
    private void loadUsersFromFile() {
        File file = new File(USER_FILE);
        if (!file.exists()) {
            users.put("admin", new Admin("admin", "admin123"));
            users.put("user", new Customer("user", "123"));
            saveUsersToFile();
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String u = parts[0], p = parts[1], r = parts[2];
                    if (r.equals("ADMIN")) users.put(u, new Admin(u, p));
                    else users.put(u, new Customer(u, p));
                }
            }
        } catch (IOException e) { Color.printError("Gagal membaca data user: " + e.getMessage()); }
    }

    /**
     * Menulis ulang seluruh data pengguna dari memori ke file {@code users.txt}.
     */
    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User u : users.values()) {
                String role = (u instanceof Admin) ? "ADMIN" : "CUSTOMER";
                writer.write(u.getUsername() + "," + u.getPassword() + "," + role);
                writer.newLine();
            }
        } catch (IOException e) { Color.printError("Gagal menyimpan data user."); }
    }

    /**
     * Memuat data buku dari file {@code books.txt}.
     * Melakukan parsing CSV dan instansiasi objek buku berdasarkan tipenya (Polimorfisme).
     */
    private void loadBooksFromFile() {
        File file = new File(BOOK_FILE);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split(";");
                if (p.length == 6) {
                    String tipe = p[0], id = p[1], judul = p[2], penulis = p[3];
                    double harga = Double.parseDouble(p[4]);
                    int stok = Integer.parseInt(p[5]);
                    
                    Book b = switch(tipe) {
                        case "NOVEL" -> new Novel(id, judul, penulis, harga, stok);
                        case "KOMIK" -> new Komik(id, judul, penulis, harga, stok);
                        case "MAJALAH" -> new Majalah(id, judul, penulis, harga, stok);
                        default -> null;
                    };
                    if(b != null) listBarang.tambahBarang(b);
                }
            }
        } catch (IOException | NumberFormatException e) { Color.printError("Gagal membaca data buku."); }
    }

    /**
     * Menyimpan data katalog buku ke file {@code books.txt} dalam format CSV.
     */
    private void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOK_FILE))) {
            for (Book b : listBarang.lihatSemua()) {
                writer.write(b.toCSV());
                writer.newLine();
            }
        } catch (IOException e) { Color.printError("Gagal menyimpan data buku."); }
    }

    /**
     * Menyimpan data transaksi ke file {@code transactions.txt}.
     * Menggunakan delimiter khusus untuk memisahkan field dan detail item.
     */
    private void saveTransactionsToFile() {
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRX_FILE))) {
            for (Transaction t : transactions) {
                if (t.getCustomer() == null) continue;
                StringBuilder sb = new StringBuilder();
                sb.append(t.getTransactionId()).append("|")
                  .append(t.getCustomer().getUsername()).append("|")
                  .append(t.getTotalAmount()).append("|")
                  .append(t.getStatus()).append("|")
                  .append(t.getTransactionDate().format(fmt)).append("|")
                  .append(t.getPaymentMethod().getPaymentName()).append("|");
                
                List<String> itemsStr = t.getItems().stream()
                    .map(i -> i.getBook().getId() + ":" + i.getJumlah())
                    .collect(Collectors.toList());
                sb.append(String.join("#", itemsStr));
                
                writer.write(sb.toString());
                writer.newLine();
            }
        } catch (IOException e) { Color.printError("Gagal menyimpan transaksi."); }
    }

    /**
     * Memuat riwayat transaksi dari file.
     * Melakukan rekonstruksi objek transaksi, termasuk item belanja dan status.
     */
    private void loadTransactionsFromFile() {
        File file = new File(TRX_FILE);
        if (!file.exists()) return;
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    String id = parts[0], username = parts[1];
                    double total = Double.parseDouble(parts[2]);
                    String status = parts[3];
                    LocalDateTime date = LocalDateTime.parse(parts[4], fmt);
                    String payName = parts[5];
                    
                    User u = users.get(username);
                    if (u instanceof Customer) {
                        List<CartItem> items = new ArrayList<>();
                        for (String pair : parts[6].split("#")) {
                            String[] kv = pair.split(":");
                            Book b = listBarang.cariBarang(kv[0]);
                            if (b != null) items.add(new CartItem(b, Integer.parseInt(kv[1])));
                        }
                        transactions.add(new Transaction(id, (Customer)u, items, new HistoryPayment(payName), total, status, date));
                    }
                }
            }
        } catch (Exception e) { Color.printError("Gagal membaca transaksi."); }
    }
    
    /**
     * Helper method untuk memvalidasi input integer dari pengguna.
     * Memastikan input berada dalam rentang [min, max] dan menangani kesalahan format.
     * * @param min Batas bawah (inklusif).
     * @param max Batas atas (inklusif).
     * @return Nilai integer yang valid.
     */
    private int getValidIntInput(int min, int max) {
        while (true) {
            System.out.print(Color.YELLOW + "Pilih Menu (" + min + "-" + max + ") > " + Color.RESET);
            String input = scanner.nextLine();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) return value;
                else Color.printError("Pilihan tidak tersedia. Harap masukkan angka yang benar.");
            } catch (NumberFormatException e) { Color.printError("Input harus berupa format angka."); }
        }
    }
    
    /**
     * Menahan eksekusi program sementara hingga pengguna menekan ENTER.
     * Digunakan untuk memberi waktu pengguna membaca output.
     */
    private void pause() {
        System.out.print("\n" + Color.WHITE_BOLD + "[Tekan ENTER untuk melanjutkan...]" + Color.RESET);
        scanner.nextLine();
    }

    /**
     * Loop utama aplikasi (Main Event Loop).
     * Menangani alur login, registrasi, dan pendelegasian kontrol ke driver yang sesuai.
     */
    public void run() {
        boolean running = true;
        while (running) {
            Color.clear();
            Color.printBanner();
            try {
                if (currentUser == null) {
                    System.out.println("1. Login ke Akun");
                    System.out.println("2. Registrasi Akun Baru");
                    System.out.println("3. " + Color.RED + "Keluar Aplikasi" + Color.RESET);
                    
                    int input = getValidIntInput(1, 3);
                    switch (input) {
                        case 1 -> login();
                        case 2 -> register();
                        case 3 -> { Color.printInfo("Terima kasih telah menggunakan BookSpace!"); running = false; }
                    }
                } else {
                    // Routing berdasarkan tipe instance pengguna (Polimorfisme)
                    if (currentUser instanceof Admin) {
                        adminDriver.setCurrentUser(currentUser);
                        adminDriver.run(); 
                        currentUser = adminDriver.currentUser; // Update state (handling logout/profile change)
                    } else {
                        customerDriver.setCurrentUser(currentUser);
                        customerDriver.run(); 
                        currentUser = customerDriver.currentUser;
                    }
                }
            } catch (Exception e) {
                Color.printError("Terjadi kesalahan sistem yang tidak terduga: " + e.getMessage());
                pause();
            }
        }
    }

    /**
     * Menangani proses autentikasi pengguna.
     * * @throws InvalidLoginException Jika username tidak ditemukan atau password salah.
     */
    private void login() throws InvalidLoginException {
        System.out.print("Username: "); String username = scanner.nextLine();
        System.out.print("Password: "); String password = scanner.nextLine();
        User user = users.get(username);
        
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            Color.printSuccess("Login berhasil. Selamat datang kembali, " + Color.WHITE_BOLD + username + Color.RESET + "!");
            pause();
        } else {
            Color.printError("Kombinasi username atau password salah.");
            pause();
            throw new InvalidLoginException("Autentikasi Gagal");
        }
    }

    /**
     * Menangani proses pendaftaran akun pelanggan baru.
     * Memvalidasi keunikan username dan kelengkapan data.
     */
    private void register() {
        System.out.print("Username Baru: "); String newUsername = scanner.nextLine();
        if (newUsername.isEmpty() || users.containsKey(newUsername)) {
            Color.printError("Username tidak valid atau sudah digunakan."); pause(); return;
        }
        System.out.print("Password Baru: "); String newPassword = scanner.nextLine();
        if (newPassword.isEmpty()) { Color.printError("Password tidak boleh kosong."); pause(); return; }
        
        users.put(newUsername, new Customer(newUsername, newPassword));
        saveUsersToFile();
        Color.printSuccess("Registrasi berhasil! Silakan login dengan akun baru Anda."); pause();
    }
}

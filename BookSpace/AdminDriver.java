import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * Driver implementasi untuk logika bisnis Administrator.
 * Menangani CRUD buku dan validasi transaksi.
 */
class AdminDriver extends Driver {
    
    public AdminDriver(Scanner scanner, ListBarang listBarang, ArrayList<Transaction> transactions, Map<String, User> users, Runnable saveCallback) {
        super(scanner, listBarang, transactions, users, saveCallback);
    }
    
    @Override
    public void run() {
        boolean adminRunning = true;
        while (adminRunning && currentUser != null) {
            Color.clear();
            Color.printBanner();
            currentUser.displayMenu();
            
            int choice = getValidIntInput(1, 8);
            
            switch (choice) {
                case 1: addBook(); break;
                case 2: editBook(); break;
                case 3: deleteBook(); break;
                case 4: listBarang.tampilkanSemua(); pause(); break;
                case 5: viewPendingTransactions(); pause(); break;
                case 6: approveTransaction(); break;
                case 7: changeProfile(); pause(); break;
                case 8: 
                    Color.printInfo("Logout berhasil."); 
                    currentUser = null; 
                    adminRunning = false; 
                    pause(); 
                    break;
            }
        }
    }
    
    private void addBook() {
        try {
            System.out.println("\n-- Pilih Kategori Buku --");
            System.out.println("1. Novel");
            System.out.println("2. Komik");
            System.out.println("3. Majalah");
            int kat = getValidIntInput(1, 3);

            String id;
            while (true) {
                System.out.print("ID Buku (Primary Key): "); 
                id = scanner.nextLine();
                if (id.isEmpty()) {
                    Color.printError("ID tidak boleh kosong.");
                } else if (listBarang.cariBarang(id) != null) {
                    Color.printError("ID '" + id + "' sudah terdaftar.");
                } else {
                    break;
                }
            }

            System.out.print("Judul: "); String judul = scanner.nextLine();
            System.out.print("Penulis: "); String penulis = scanner.nextLine();
            
            double harga = -1;
            while (harga < 0) {
                System.out.print("Harga (Angka > 0): ");
                try { harga = Double.parseDouble(scanner.nextLine()); if(harga < 0) Color.printError("Harga tidak boleh negatif."); }
                catch(Exception e) { Color.printError("Input numerik tidak valid."); }
            }

            int stok = -1;
            while (stok < 0) {
                System.out.print("Stok (Angka >= 0): ");
                try { stok = Integer.parseInt(scanner.nextLine()); if(stok < 0) Color.printError("Stok tidak boleh negatif."); }
                catch(Exception e) { Color.printError("Input numerik tidak valid."); }
            }
            
            Book book = null;
            switch (kat) {
                case 1: book = new Novel(id, judul, penulis, harga, stok); break;
                case 2: book = new Komik(id, judul, penulis, harga, stok); break;
                case 3: book = new Majalah(id, judul, penulis, harga, stok); break;
            }
            if (book != null) { 
                listBarang.tambahBarang(book); 
                saveCallback.run();
                Color.printSuccess("Data buku berhasil ditambahkan."); 
            }
        } catch (Exception e) { Color.printError("Terjadi kesalahan sistem."); } pause();
    }
    
    private void editBook() {
        listBarang.tampilkanSemua();
        System.out.print("Masukkan ID Buku: ");
        String id = scanner.nextLine();
        Book b = listBarang.cariBarang(id);
        
        if (b != null) {
            boolean editing = true;
            while (editing) {
                System.out.println(Color.CYAN + "\n=== EDIT DATA: " + b.getJudul() + " ===" + Color.RESET);
                System.out.println("1. Edit Judul");
                System.out.println("2. Edit Harga");
                System.out.println("3. Edit Stok");
                System.out.println("4. Edit Semua Field");
                System.out.println("5. Kembali");
                
                int editChoice = getValidIntInput(1, 5);
                
                switch (editChoice) {
                    case 1:
                        System.out.print("Judul Baru: "); String j = scanner.nextLine(); 
                        if (!j.isEmpty()) { b.setJudul(j); Color.printSuccess("Judul diperbarui."); }
                        break;
                    case 2:
                        System.out.print("Harga Baru: "); 
                        try { 
                            double h = Double.parseDouble(scanner.nextLine()); 
                            if(h >= 0) { b.setHarga(h); Color.printSuccess("Harga diperbarui."); } else Color.printError("Nilai negatif.");
                        } catch(Exception e) { Color.printError("Input salah."); }
                        break;
                    case 3:
                        System.out.print("Stok Baru: "); 
                        try { 
                            int s = Integer.parseInt(scanner.nextLine()); 
                            if(s >= 0) { b.setStok(s); Color.printSuccess("Stok diperbarui."); } else Color.printError("Nilai negatif.");
                        } catch(Exception e) { Color.printError("Input salah."); }
                        break;
                    case 4:
                        System.out.print("Judul: "); String ja = scanner.nextLine(); if(!ja.isEmpty()) b.setJudul(ja);
                        System.out.print("Harga: "); try { b.setHarga(Double.parseDouble(scanner.nextLine())); } catch(Exception e){}
                        System.out.print("Stok: "); try { b.setStok(Integer.parseInt(scanner.nextLine())); } catch(Exception e){}
                        Color.printSuccess("Data berhasil diperbarui.");
                        break;
                    case 5: editing = false; break;
                }
                saveCallback.run();
            }
        } else {
            Color.printError("Buku tidak ditemukan.");
        }
        pause();
    }
    
    private void deleteBook() {
        System.out.print("ID Buku untuk dihapus: ");
        if (listBarang.hapusBarang(scanner.nextLine())) {
            saveCallback.run();
            Color.printSuccess("Buku berhasil dihapus dari sistem.");
        } else Color.printError("Buku tidak ditemukan."); pause();
    }
    
    private void viewPendingTransactions() {
        System.out.println(Color.CYAN + "\n=== TRANSAKSI TERTUNDA ===" + Color.RESET);
        allTransactions.stream().filter(t -> t.getStatus().equals("pending")).forEach(Transaction::displayInfo);
    }
    
    private void approveTransaction() {
        System.out.print("Masukkan ID Transaksi: "); String id = scanner.nextLine();
        for (Transaction t : allTransactions) {
            if (t.getTransactionId().equals(id) && t.getStatus().equals("pending")) {
                try {
                    for (CartItem item : t.getItems()) {
                        item.getBook().kurangiStok(item.getJumlah());
                    }
                    t.setCompleted();
                    Color.printSuccess("Transaksi DISETUJUI.");
                    t.printInvoice();
                } catch (InsufficientStockException e) {
                    t.setCancelled();
                    Color.printError("Gagal: Stok tidak mencukupi. Transaksi DIBATALKAN.");
                }
                saveCallback.run(); 
                pause(); return;
            }
        }
        Color.printError("ID Transaksi tidak ditemukan atau status bukan pending."); pause();
    }
}
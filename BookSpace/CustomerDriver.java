import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Driver implementasi untuk logika bisnis Pelanggan.
 * Menangani alur pembelian, manajemen keranjang, dan checkout.
 */
class CustomerDriver extends Driver {
    
    public CustomerDriver(Scanner scanner, ListBarang listBarang, ArrayList<Transaction> transactions, Map<String, User> users, Runnable saveCallback) {
        super(scanner, listBarang, transactions, users, saveCallback);
    }
    
    @Override
    public void run() {
        boolean custRunning = true;
        while (custRunning && currentUser != null) {
            Color.clear();
            Color.printBanner();
            currentUser.displayMenu();
            
            int choice = getValidIntInput(1, 7);
            
            switch (choice) {
                case 1: listBarang.tampilkanSemua(); pause(); break;
                case 2: addToCart(); pause(); break;
                case 3: manageCart(); break;
                case 4: checkout(); break;
                case 5: viewHistory(); pause(); break;
                case 6: changeProfile(); pause(); break;
                case 7: 
                    Color.printInfo("Logout berhasil."); 
                    currentUser = null; 
                    custRunning = false; 
                    pause(); 
                    break;
            }
        }
    }
    
    private void addToCart() {
        listBarang.tampilkanSemua();
        System.out.print("ID Buku: "); Book b = listBarang.cariBarang(scanner.nextLine());
        if (b != null) {
            int qty = -1;
            while(qty <= 0) {
                System.out.print("Jumlah (Positif): ");
                try { 
                    qty = Integer.parseInt(scanner.nextLine()); 
                    if(qty <= 0) Color.printError("Jumlah harus lebih dari 0.");
                } catch(Exception e) { Color.printError("Input angka valid."); }
            }
            
            if (qty <= b.getStok()) {
                ((Customer)currentUser).getKeranjang().tambah(new CartItem(b, qty));
                Color.printSuccess("Item berhasil ditambahkan ke keranjang.");
            } else Color.printError("Stok tidak mencukupi.");
        } else Color.printError("Buku tidak ditemukan.");
    }

    private void manageCart() {
        boolean managing = true;
        while (managing) {
            Color.clear();
            Color.printBanner();
            ((Customer)currentUser).getKeranjang().lihatBarang();
            
            System.out.println("\n--- MANAJEMEN KERANJANG ---");
            System.out.println("1. Checkout");
            System.out.println("2. Ubah Kuantitas");
            System.out.println("3. Hapus Item");
            System.out.println("4. Kosongkan Keranjang");
            System.out.println("5. Kembali");
            
            int choice = getValidIntInput(1, 5);
            ArrayList<CartItem> items = ((Customer)currentUser).getKeranjang().getBarang();
            
            switch (choice) {
                case 1: checkout(); managing = false; break;
                case 2:
                    if(items.isEmpty()) { Color.printError("Keranjang kosong."); pause(); break; }
                    System.out.print("Nomor urut item: ");
                    try {
                        int idx = Integer.parseInt(scanner.nextLine()) - 1;
                        if(idx >= 0 && idx < items.size()) {
                            System.out.print("Jumlah baru: ");
                            int newQty = Integer.parseInt(scanner.nextLine());
                            if(newQty > 0 && newQty <= items.get(idx).getBook().getStok()) {
                                items.get(idx).setJumlah(newQty);
                                Color.printSuccess("Kuantitas diperbarui.");
                            } else Color.printError("Jumlah tidak valid/stok kurang.");
                        } else Color.printError("Indeks tidak valid.");
                    } catch(Exception e) { Color.printError("Kesalahan input."); }
                    pause();
                    break;
                case 3:
                    if(items.isEmpty()) { Color.printError("Keranjang kosong."); pause(); break; }
                    System.out.print("Nomor urut item: ");
                    try {
                        int idx = Integer.parseInt(scanner.nextLine()) - 1;
                        if(idx >= 0 && idx < items.size()) {
                            items.remove(idx);
                            Color.printSuccess("Item dihapus.");
                        } else Color.printError("Indeks tidak valid.");
                    } catch(Exception e) { Color.printError("Kesalahan input."); }
                    pause();
                    break;
                case 4:
                    ((Customer)currentUser).getKeranjang().kosongkan();
                    Color.printSuccess("Keranjang telah dikosongkan.");
                    pause();
                    break;
                case 5: managing = false; break;
            }
        }
    }
    
    private void checkout() {
        Keranjang cart = ((Customer)currentUser).getKeranjang();
        if (cart.getBarang().isEmpty()) { Color.printError("Keranjang kosong."); pause(); return; }

        List<CartItem> selected = new ArrayList<>();
        cart.lihatBarang();
        System.out.println("Ketik 'all' untuk semua, atau nomor urut dipisah koma (contoh: 1,3)");
        System.out.print("Pilih > ");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("all")) selected.addAll(cart.getBarang());
        else {
            try {
                for (String idx : input.split(",")) selected.add(cart.getBarang().get(Integer.parseInt(idx.trim()) - 1));
            } catch (Exception e) { Color.printError("Format pilihan salah."); pause(); return; }
        }

        if (selected.isEmpty()) { Color.printError("Tidak ada item yang dipilih."); pause(); return; }

        double total = 0;
        for(CartItem i : selected) total += i.getSubtotal();
        System.out.printf("Total Tagihan: " + Color.GREEN + "Rp%,.0f" + Color.RESET + "\n", total);

        System.out.println("\n=== PILIH PEMBAYARAN ===");
        System.out.println("1. COD (Bayar di Tempat)");
        System.out.println("2. Transfer Bank");
        System.out.println("3. E-Wallet");
        
        int payChoice = getValidIntInput(1, 3);
        PaymentMethod pm = null;

        switch (payChoice) {
            case 1: pm = new CashPayment(); break;
            case 2:
                System.out.println("\n-- BANK TERSEDIA --");
                System.out.println("1. BCA\n2. BRI\n3. BNI\n4. Mandiri\n5. BSI");
                int bChoice = getValidIntInput(1, 5);
                String bName = switch(bChoice) { case 1->"BCA"; case 2->"BRI"; case 3->"BNI"; case 4->"Mandiri"; default->"BSI"; };
                pm = new BankTransferPayment(bName);
                break;
            case 3:
                System.out.println("\n-- E-WALLET TERSEDIA --");
                System.out.println("1. GoPay\n2. OVO\n3. Dana\n4. ShopeePay");
                int wChoice = getValidIntInput(1, 4);
                String wName = switch(wChoice) { case 1->"GoPay"; case 2->"OVO"; case 3->"Dana"; default->"ShopeePay"; };
                pm = new EWalletPayment(wName);
                break;
        }

        if (pm != null) {
            Transaction t = new Transaction((Customer) currentUser, selected, pm);
            allTransactions.add(t);
            pm.processPayment(t.getTotalAmount());
            
            System.out.println(Color.GREEN + "Pesanan berhasil dibuat! Menunggu konfirmasi admin." + Color.RESET);
            
            cart.hapusBarang(selected);
            saveCallback.run();
        }
        pause();
    }
    
    private void viewHistory() {
        System.out.println(Color.CYAN + "\n=== RIWAYAT TRANSAKSI ===" + Color.RESET);
        for (Transaction t : allTransactions) {
            if (t.getCustomer() != null && t.getCustomer().getUsername().equals(currentUser.getUsername())) {
                t.displayInfo();
            }
        }
    }
}

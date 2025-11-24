import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Kelas entity yang merepresentasikan sebuah transaksi pembelian.
 * Menyimpan status, detail item, metode pembayaran, dan timestamp.
 */
class Transaction {
    private final String transactionId;
    private final Customer customer;
    private final List<CartItem> items;
    private final PaymentMethod paymentMethod;
    private final double totalAmount;
    private String status;
    private final LocalDateTime transactionDate;
    
    /**
     * Konstruktor untuk transaksi baru.
     * ID transaksi dibangkitkan secara otomatis.
     */
    public Transaction(Customer customer, List<CartItem> items, PaymentMethod paymentMethod) {
        this.transactionId = "TRX-" + System.currentTimeMillis() % 10000;
        this.customer = customer;
        this.items = new ArrayList<>(items);
        this.paymentMethod = paymentMethod;
        this.totalAmount = calculateTotal();
        this.status = "pending";
        this.transactionDate = LocalDateTime.now();
    }

    /**
     * Konstruktor untuk rekonstruksi transaksi dari data persisten.
     */
    public Transaction(String id, Customer customer, List<CartItem> items, PaymentMethod paymentMethod, double total, String status, LocalDateTime date) {
        this.transactionId = id;
        this.customer = customer;
        this.items = new ArrayList<>(items);
        this.paymentMethod = paymentMethod;
        this.totalAmount = total;
        this.status = status;
        this.transactionDate = date;
    }
    
    public String getTransactionId() { return transactionId; }
    public Customer getCustomer() { return customer; }
    public List<CartItem> getItems() { return items; }
    public String getStatus() { return status; }
    public double getTotalAmount() { return totalAmount; }
    public LocalDateTime getTransactionDate() { return transactionDate; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    
    public void setCompleted() { this.status = "completed"; }
    public void setCancelled() { this.status = "cancelled"; }
    
    private double calculateTotal() {
        double total = 0;
        for (CartItem item : items) total += item.getSubtotal();
        return total;
    }
    
    public void displayInfo() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM HH:mm");
        String colorStatus;
        
        if (status.equals("completed")) {
            colorStatus = Color.GREEN;
        } else if (status.equals("cancelled")) {
            colorStatus = Color.RED;
        } else {
            colorStatus = Color.YELLOW;
        }

        System.out.println(Color.PURPLE + "------------------------------------------------" + Color.RESET);
        System.out.printf("ID: %s | Tgl: %s | Status: %s%s%s\n", 
                transactionId, transactionDate.format(fmt), colorStatus, status.toUpperCase(), Color.RESET);
        
        String custName = (customer != null) ? customer.getUsername() : "[User Dihapus]";
        System.out.println("Pembeli: " + Color.WHITE_BOLD + custName + Color.RESET);
        
        for (CartItem item : items) {
            System.out.printf(" - %s " + Color.YELLOW + "(x%d)" + Color.RESET + "\n", item.getBook().getJudul(), item.getJumlah());
        }
        System.out.printf("Total: " + Color.GREEN + "Rp%,.0f" + Color.RESET + " (Via %s)\n", totalAmount, paymentMethod.getPaymentName());
    }
    
    public void printInvoice() {
        if ("completed".equals(status)) {
            System.out.println(Color.CYAN + "\n╔═══ INVOICE RESMI BOOKSPACE ════════╗" + Color.RESET);
            displayInfo();
            System.out.println(Color.CYAN + "╚════════════════════════════════════╝" + Color.RESET);
        }
    }
}

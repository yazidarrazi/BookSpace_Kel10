/**
 * Utilitas untuk mencetak bukti transaksi (Invoice) ke konsol.
 * Hanya mencetak jika status transaksi sudah 'completed'.
 */
class Invoice {
    private Transaction transaksi;
    
    public Invoice(Transaction transaksi) {
        this.transaksi = transaksi;
    }
    
    /**
     * Menampilkan invoice resmi dengan format rapi.
     */
    public void printInvoice() {
        if ("completed".equals(transaksi.getStatus())) {
            System.out.println(Color.CYAN + "\n╔═══ INVOICE RESMI BOOKSPACE ════════╗" + Color.RESET);
            transaksi.displayInfo();
            System.out.println(Color.CYAN + "╚════════════════════════════════════╝" + Color.RESET);
        }
    }
}

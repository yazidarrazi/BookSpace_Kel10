/**
 * Implementasi konkret Buku untuk kategori Majalah.
 */
class Majalah extends Book {
    public Majalah(String id, String j, String p, double h, int s) { super(id, j, p, h, s); }
    @Override public String getKategori() { return "Majalah"; }
}

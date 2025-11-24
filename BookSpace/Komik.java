/**
 * Implementasi konkret Buku untuk kategori Komik.
 */
class Komik extends Book {
    public Komik(String id, String j, String p, double h, int s) { super(id, j, p, h, s); }
    @Override public String getKategori() { return "Komik"; }
}

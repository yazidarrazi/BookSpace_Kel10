/**
 * Implementasi konkret Buku untuk kategori Novel.
 */
class Novel extends Book {
    public Novel(String id, String j, String p, double h, int s) { super(id, j, p, h, s); }
    @Override public String getKategori() { return "Novel"; }
}

/**
 * Kelas utama (Main Class) yang berfungsi sebagai titik masuk (entry point) aplikasi BookSpace.
 * Kelas ini bertanggung jawab untuk membersihkan layar konsol dan menginisialisasi
 * siklus hidup aplikasi melalui kelas {@link Main}.
 * * @author BookSpace Team
 * @version 1.0
 */
public class BookSpace {
    
    /**
     * Metode utama yang dieksekusi oleh JVM saat aplikasi dijalankan.
     * * @param args Argumen baris perintah (tidak digunakan dalam aplikasi ini).
     */
    public static void main(String[] args) {
        Color.clear(); 
        Main mainApp = new Main();
        mainApp.run(); 
    }
}

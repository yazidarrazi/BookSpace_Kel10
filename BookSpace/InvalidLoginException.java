/**
 * Exception kustom (Checked Exception) yang dilempar ketika
 * proses autentikasi gagal (username/password salah).
 */
class InvalidLoginException extends Exception {
    public InvalidLoginException(String message) { super(message); }
}

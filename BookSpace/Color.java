/**
 * Kelas utilitas statis untuk memanipulasi warna teks di terminal (ANSI Escape Codes).
 * Menyediakan metode helper untuk UI seperti clear screen dan banner.
 */
class Color {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE_BOLD = "\u001B[1;37m";

    public static void printBanner() {
        System.out.println(CYAN + "██████╗  ██████╗  ██████╗ ██╗  ██╗" + PURPLE + "███████╗██████╗  █████╗  ██████╗███████╗");
        System.out.println(CYAN + "██╔══██╗██╔═══██╗██╔═══██╗██║ ██╔╝" + PURPLE + "██╔════╝██╔══██╗██╔══██╗██╔════╝██╔════╝");
        System.out.println(CYAN + "██████╔╝██║   ██║██║   ██║█████╔╝ " + PURPLE + "███████╗██████╔╝███████║██║     █████╗  ");
        System.out.println(CYAN + "██╔══██╗██║   ██║██║   ██║██╔═██╗ " + PURPLE + "╚════██║██╔═══╝ ██╔══██║██║     ██╔══╝  ");
        System.out.println(CYAN + "██████╔╝╚██████╔╝╚██████╔╝██║  ██╗" + PURPLE + "███████║██║     ██║  ██║╚██████╗███████╗");
        System.out.println(CYAN + "╚═════╝  ╚═════╝  ╚═════╝ ╚═╝  ╚═╝" + PURPLE + "╚══════╝╚═╝     ╚═╝  ╚═╝ ╚═════╝╚══════╝");
        System.out.println(YELLOW + "        >>> SISTEM TOKO BUKU DIGITAL TERLENGKAP <<<        " + RESET);
        System.out.println("====================================================================");
    }

    public static void printMenuHeader(String title, String colorCode) {
        int width = 50;
        String border = "═".repeat(width);
        
        System.out.println(colorCode + "\n╔" + border + "╗" + RESET);
        
        int paddingLeft = (width - title.length()) / 2;
        int paddingRight = width - title.length() - paddingLeft;
        
        String leftSpace = " ".repeat(paddingLeft);
        String rightSpace = " ".repeat(paddingRight);
        
        System.out.println(colorCode + "║" + leftSpace + title + rightSpace + "║" + RESET);
        System.out.println(colorCode + "╚" + border + "╝" + RESET);
    }

    public static void printSuccess(String msg) {
        System.out.println(GREEN + "[SUKSES] " + msg + RESET);
    }

    public static void printError(String msg) {
        System.out.println(RED + "[ERROR] " + msg + RESET);
    }

    public static void printInfo(String msg) {
        System.out.println(CYAN + "[INFO] " + msg + RESET);
    }

    public static void clear() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("\n".repeat(50));
        }
    }
}

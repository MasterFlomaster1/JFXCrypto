package Cipher;

public class CurrentCipher {

    private static int currentCipher;

    public static final int AES = 0;
    public static final int AES256 = 1;
    public static final int SimpleCipher = 2;

    public static void setCurrentCipher(int a) {
        if (a>2) {
            System.out.println("Error: cipher not found");
            return;
        }
        currentCipher = a;
    }

    public static int getCurrentCipher() {
        return currentCipher;
    }
}

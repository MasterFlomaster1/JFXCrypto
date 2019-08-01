package Cipher;

public class CurrentCipher {

    private static int currentCipher;

    public static final int AES = 0;
    public static final int AES256 = 1;
    public static final int SimpleCipher = 2;

    public static void setCurrentCipher(int a) {
        currentCipher = a;
    }

    public static int getCurrentCipher() {
        return currentCipher;
    }
}

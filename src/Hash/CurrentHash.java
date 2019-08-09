package Hash;

public class CurrentHash {

    public static final int MD2 = 0;
    public static final int MD5 = 1;
    public static final int SHA1 = 2;
    public static final int SHA224 = 3;
    public static final int SHA256 = 4;
    public static final int SHA384 = 5;
    public static final int SHA512 = 6;

    private static int currentHash;

    public static void setCurrentHash(int a) {
        currentHash = a;
    }

    private static int getCurrentHash() {
        return currentHash;
    }

    public static String getCurrentHashName() {
        switch (getCurrentHash()) {
            case MD2:
                return "MD2";
            case MD5:
                return "MD5";
            case SHA1:
                return "SHA-1";
            case SHA224:
                return "SHA-224";
            case SHA256:
                return "SHA-256";
            case SHA384:
                return "SHA-384";
            case SHA512:
                return "SHA-512";
        }
        return null;
    }

}

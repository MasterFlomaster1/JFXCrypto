package Hash;

import java.io.File;

public class CurrentHash {

    public static final int MD2 = 0;
    public static final int MD5 = 1;
    public static final int SHA1 = 2;
    public static final int SHA224 = 3;
    public static final int SHA256 = 4;
    public static final int SHA384 = 5;
    public static final int SHA512 = 6;

    private static MD2 md2 = new MD2();
    private static MD5 md5 = new MD5();
    private static SHA1 sha1 = new SHA1();
    private static SHA224 sha224 = new SHA224();
    private static SHA256 sha256 = new SHA256();
    private static SHA384 sha384 = new SHA384();
    private static SHA512 sha512 = new SHA512();

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

    public static String hashText(String input) {
        switch (getCurrentHash()) {
            case MD2:
                return md2.getHashFromText(input);
            case MD5:
                return md5.getHashFromText(input);
            case SHA1:
                return sha1.getHashFromText(input);
            case SHA224:
                return sha224.getHashFromText(input);
            case SHA256:
                return sha256.getHashFromText(input);
            case SHA384:
                return sha384.getHashFromText(input);
            case SHA512:
                return sha512.getHashFromText(input);
        }
        return null;
    }

    public static String fileHashSum(File file) {
        switch (getCurrentHash()) {
            case MD2:
                return md2.getHashFromFile(file);
            case MD5:
                return md5.getHashFromFile(file);
            case SHA1:
                return sha1.getHashFromFile(file);
            case SHA224:
                return sha224.getHashFromFile(file);
            case SHA256:
                return sha256.getHashFromFile(file);
            case SHA384:
                return sha384.getHashFromFile(file);
            case SHA512:
                return sha512.getHashFromFile(file);
        }
        return null;
    }

}

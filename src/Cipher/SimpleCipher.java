package Cipher;

public class SimpleCipher {

    public static String crypt(boolean encrypting, String line) {
        StringBuilder encrypted = new StringBuilder();
        for (int i=0;i<line.length();i++) {
            encrypted.append(cryptChar(encrypting, line.charAt(i)));
        }
        return encrypted.toString();
    }

    private static char cryptChar(boolean encrypt, char c) {
        String CHECKS = ("abcdefghijklmnopqrstuvwxyz1234567890 ,.!?:;()*&^%$#@-=+_");
        String REPLACES = ("0&q^a9z% x$s3.w)ed:c6v#f2,8*r5tg-b7nhy?uj!m1k(i;olp@+4_");
        int index = 0;
        if (encrypt) {
            for (int i = 0; i < CHECKS.length(); i++) {
                if (c == CHECKS.charAt(i)) {
                    index = i;
                    break;
                } else {
                    index++;
                }
            }
            return REPLACES.charAt(index);
        } else {
            for (int i = 0; i < REPLACES.length(); i++) {
                if (c == REPLACES.charAt(i)) {
                    index = i;
                    break;
                } else
                    index++;
            }
            return CHECKS.charAt(index);
        }
    }

}

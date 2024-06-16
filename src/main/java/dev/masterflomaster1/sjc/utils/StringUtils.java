package dev.masterflomaster1.sjc.utils;

public class StringUtils {

    /**
     * @see <a href="https://stackoverflow.com/questions/140131/convert-a-string-representation-of-a-hex-dump-to-a-byte-array-using-java">Convert hex string to a byte array</a>
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String spaceAfterN(String input, int n) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < input.length(); i += n) {
            if (i > 0) {
                result.append(" ");
            }
            int end = Math.min(i + n, input.length());
            result.append(input, i, end);
        }

        return result.toString();
    }

}

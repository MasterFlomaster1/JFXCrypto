package dev.masterflomaster1.sjc.utils;

public class StringUtils {

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

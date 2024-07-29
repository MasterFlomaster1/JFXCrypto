package dev.masterflomaster1.jfxc.utils;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public final class StringUtils {

    private StringUtils() { }

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

    public static String removeSpaces(String input) {
        return input.replaceAll(" ", "");
    }

    public static String removePunctuation(String value) {
        return value.replaceAll("[^a-zA-Z0-9]", "");
    }

    /**
     * Formatting byte size to human-readable format.
     *
     * @param bytes the value to convert
     * @return formatted {@code String} e.g. 108.0 KiB
     *
     * @see <a href="https://stackoverflow.com/a/3758880">Algorithm information</a>
     * @see <a href="https://programming.guide/java/formatting-byte-size-to-human-readable-format.html">Algorithm information</a>
     */
    public static String convert(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }

}

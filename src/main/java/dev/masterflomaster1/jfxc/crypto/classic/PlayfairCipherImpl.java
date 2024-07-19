package dev.masterflomaster1.jfxc.crypto.classic;

import java.util.HashSet;
import java.util.Set;

/**
 * @see <a href="https://en.wikipedia.org/wiki/Playfair_cipher">Playfair cipher</a>
 */
public final class PlayfairCipherImpl {

    private static final char[][] PLAYFAIR_TABLE = new char[5][5];

    private PlayfairCipherImpl() { }

    public static String encrypt(String plaintext, String keyword) {
        buildPlayfairTable(keyword);
        plaintext = preprocessText(plaintext);

        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i += 2) {
            char first = plaintext.charAt(i);
            char second = plaintext.charAt(i + 1);
            int[] firstPos = findPosition(first);
            int[] secondPos = findPosition(second);

            int row1 = firstPos[0];
            int col1 = firstPos[1];
            int row2 = secondPos[0];
            int col2 = secondPos[1];

            if (row1 == row2) {
                col1 = (col1 + 1) % 5;
                col2 = (col2 + 1) % 5;
            } else if (col1 == col2) {
                row1 = (row1 + 1) % 5;
                row2 = (row2 + 1) % 5;
            } else {
                int temp = col1;
                col1 = col2;
                col2 = temp;
            }

            ciphertext.append(PLAYFAIR_TABLE[row1][col1]);
            ciphertext.append(PLAYFAIR_TABLE[row2][col2]);
        }

        return ciphertext.toString();
    }

    public static String decrypt(String ciphertext, String keyword) {
        buildPlayfairTable(keyword);

        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i += 2) {
            char first = ciphertext.charAt(i);
            char second = ciphertext.charAt(i + 1);
            int[] firstPos = findPosition(first);
            int[] secondPos = findPosition(second);

            int row1 = firstPos[0];
            int col1 = firstPos[1];
            int row2 = secondPos[0];
            int col2 = secondPos[1];

            if (row1 == row2) {
                col1 = (col1 - 1 + 5) % 5;
                col2 = (col2 - 1 + 5) % 5;
            } else if (col1 == col2) {
                row1 = (row1 - 1 + 5) % 5;
                row2 = (row2 - 1 + 5) % 5;
            } else {
                int temp = col1;
                col1 = col2;
                col2 = temp;
            }

            plaintext.append(PLAYFAIR_TABLE[row1][col1]);
            plaintext.append(PLAYFAIR_TABLE[row2][col2]);
        }

        return plaintext.toString();
    }

    private static void buildPlayfairTable(String keyword) {
        // Prepare keyword
        keyword = keyword.toUpperCase().replaceAll("[^A-Z]", "");
        keyword = keyword.replace("J", "I");

        // Build key table
        Set<Character> usedChars = new HashSet<>();
        StringBuilder keyBuilder = new StringBuilder();

        for (char ch : keyword.toCharArray()) {
            if (!usedChars.contains(ch)) {
                keyBuilder.append(ch);
                usedChars.add(ch);
            }
        }

        for (char ch = 'A'; ch <= 'Z'; ch++) {
            if (ch != 'J' && !usedChars.contains(ch)) {
                keyBuilder.append(ch);
                usedChars.add(ch);
            }
        }

        keyword = keyBuilder.toString();

        // Fill the key table into the 5x5 matrix
        int index = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                PLAYFAIR_TABLE[i][j] = keyword.charAt(index);
                index++;
            }
        }
    }

    private static String preprocessText(String text) {
        // Replace J with I
        text = text.toUpperCase().replaceAll("[^A-Z]", "");
        text = text.replace("J", "I");

        // Insert X between repeated letters and add X if the length is odd
        StringBuilder processedText = new StringBuilder();
        for (int i = 0; i < text.length() - 1; i += 2) {
            char first = text.charAt(i);
            char second = text.charAt(i + 1);
            processedText.append(first);
            if (first == second) {
                processedText.append('X');
            } else {
                processedText.append(second);
            }
        }
        if (processedText.length() % 2 != 0) {
            processedText.append('X');
        }
        return processedText.toString();
    }

    private static int[] findPosition(char ch) {
        int[] pos = new int[2];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (PLAYFAIR_TABLE[i][j] == ch) {
                    pos[0] = i;
                    pos[1] = j;
                    return pos;
                }
            }
        }
        return pos;
    }

}

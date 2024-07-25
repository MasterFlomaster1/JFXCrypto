package dev.masterflomaster1.jfxc.crypto.classic;

import java.util.List;

/**
 * @see <a href="https://en.wikipedia.org/wiki/Affine_cipher">Affine cipher</a>
 */
public final class AffineCipherImpl {

    private static final int ALPHABET_SIZE = 26;

    public static final List<Integer> SLOPE = List.of(1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25);
    public static final List<Integer> INTERCEPT = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25);

    private AffineCipherImpl() { }

    public static String encrypt(String plaintext, int a, int b) {
        StringBuilder ciphertext = new StringBuilder();

        for (char ch : plaintext.toUpperCase().toCharArray()) {
            if (Character.isLetter(ch)) {
                int x = ch - 'A';
                char encryptedChar = (char) (((a * x + b) % ALPHABET_SIZE) + 'A');
                ciphertext.append(encryptedChar);
            } else {
                ciphertext.append(ch);
            }
        }

        return ciphertext.toString();
    }

    public static String decrypt(String ciphertext, int a, int b) {
        StringBuilder plaintext = new StringBuilder();
        int aInverse = modInverse(a, ALPHABET_SIZE);

        for (char ch : ciphertext.toUpperCase().toCharArray()) {
            if (Character.isLetter(ch)) {
                int y = ch - 'A';
                char decryptedChar = (char) ((aInverse * (y - b + ALPHABET_SIZE) % ALPHABET_SIZE) + 'A');
                plaintext.append(decryptedChar);
            } else {
                plaintext.append(ch);
            }
        }

        return plaintext.toString();
    }

    // Function to find modular inverse of a under modulo m
    private static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        throw new IllegalArgumentException("Multiplicative inverse for the given 'a' does not exist.");
    }

}

package dev.masterflomaster1.jfxc.crypto.classic;

/**
 * @see <a href="https://en.wikipedia.org/wiki/Vigen%C3%A8re_cipher">Vigenère cipher</a>
 */
public final class VigenereCipherImpl {

    private static final int ALPHABET_SIZE = 26;

    private VigenereCipherImpl() { }

    public static String encrypt(String plaintext, String keyword) {
        StringBuilder ciphertext = new StringBuilder();
        plaintext = plaintext.toUpperCase();
        keyword = keyword.toUpperCase();

        int keywordIndex = 0;
        for (char ch : plaintext.toCharArray()) {
            if (Character.isLetter(ch)) {
                int shift = keyword.charAt(keywordIndex) - 'A';
                char encryptedChar = (char) ((ch + shift - 'A') % ALPHABET_SIZE + 'A');
                ciphertext.append(encryptedChar);
                keywordIndex = (keywordIndex + 1) % keyword.length();
            } else {
                ciphertext.append(ch);
            }
        }

        return ciphertext.toString();
    }

    public static String decrypt(String ciphertext, String keyword) {
        StringBuilder plaintext = new StringBuilder();
        ciphertext = ciphertext.toUpperCase();
        keyword = keyword.toUpperCase();

        int keywordIndex = 0;
        for (char ch : ciphertext.toCharArray()) {
            if (Character.isLetter(ch)) {
                int shift = keyword.charAt(keywordIndex) - 'A';
                char decryptedChar = (char) ((ch - shift - 'A' + ALPHABET_SIZE) % ALPHABET_SIZE + 'A');
                plaintext.append(decryptedChar);
                keywordIndex = (keywordIndex + 1) % keyword.length();
            } else {
                plaintext.append(ch);
            }
        }

        return plaintext.toString();
    }

}

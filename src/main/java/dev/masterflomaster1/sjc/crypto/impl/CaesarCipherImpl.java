package dev.masterflomaster1.sjc.crypto.impl;

public class CaesarCipherImpl {

    public static String encrypt(String text, int shift) {
        StringBuilder encrypted = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int newChar = (c - base + shift) % 26 + base;
                encrypted.append((char) newChar);
            } else {
                encrypted.append(c);
            }
        }

        return encrypted.toString();
    }

    public static String decrypt(String text, int shift) {
        return encrypt(text, 26 - shift % 26);
    }

}

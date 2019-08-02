package Cipher;

import GUI.AlertDialog;

public class CurrentCipher {

    private static int currentCipher;

    public static final int AES = 0;
    public static final int AES256 = 1;
    public static final int SimpleCipher = 2;

    private static Aes aes = new Aes();
    private static Aes256 aes256 = new Aes256();
    private static SimpleCipher simpleCipher;

    public static void setCurrentCipher(int a) {
        currentCipher = a;
    }

    public static int getCurrentCipher() {
        return currentCipher;
    }

    public static String getCurrentCipherName() {
        switch (getCurrentCipher()) {
            case AES:
                return "AES";
            case AES256:
                return "AES-256";
            case SimpleCipher:
                return "SimpleCipher";
        }
        return null;
    }

    public static String encrypt(String text) {
        switch (getCurrentCipher()) {
            case AES:
                AlertDialog.showError("Currently not available.");
                break;
            case AES256:
                return aes256.encryptString(text);
            case SimpleCipher:
                AlertDialog.showError("Currently not available.");
                break;
        }
        return null;
    }

    public static String decrypt(String encryptedText) {
        switch (getCurrentCipher()) {
            case AES:
                AlertDialog.showError("Currently not available.");
                break;
            case AES256:
                return aes256.decryptString(encryptedText);
            case SimpleCipher:
                AlertDialog.showError("Currently not available.");
                break;
        }
        return null;
    }

    public static void generateKey() {
        switch (getCurrentCipher()) {
            case AES:
                AlertDialog.showError("Currently not available.");
                break;
            case AES256:
                Aes256.generateKey();
                break;
            case SimpleCipher:
                AlertDialog.showError("Currently not available.");
                break;
        }
    }

    public static String getKey() {
        switch (getCurrentCipher()) {
            case AES:
                AlertDialog.showError("Currently not available.");
                break;
            case AES256:
                return Aes256.getKey();
            case SimpleCipher:
                AlertDialog.showError("Currently not available.");
                break;
        }
        return null;
    }

    public static boolean setKey(String newKey) {
        switch (getCurrentCipher()) {
            case AES:
                AlertDialog.showError("Currently not available.");
                break;
            case AES256:
                if (newKey.length()!=16) {
                    AlertDialog.showError("AES-256 key length must be at least 16 symbols!");
                } else {
                    Aes256.setKey(newKey);
                    return true;
                }
                break;
            case SimpleCipher:
                AlertDialog.showError("Currently not available.");
                break;
        }
        return false;
    }
}

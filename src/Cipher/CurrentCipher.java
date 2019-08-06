package Cipher;

import GUI.AlertDialog;

import java.io.File;

public class CurrentCipher {

    private static int currentCipher;

    public static final int AES128 = 0;
    public static final int AES256 = 1;
    public static final int SimpleCipher = 2;
    public static final int DES = 3;
    public static final int _DES3 = 4;
    public static final int MD2 = 5;
    public static final int MD5 = 6;

    private static Aes128 aes128 = new Aes128();
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
            case AES128:
                return "AES-128";
            case AES256:
                return "AES-256";
            case SimpleCipher:
                return "SimpleCipher";
        }
        return null;
    }

    public static String encrypt(String text) {
        switch (getCurrentCipher()) {
            case AES128:
                return aes128.encryptString(text);
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
            case AES128:
                return aes128.decryptString(encryptedText);
            case AES256:
                return aes256.decryptString(encryptedText);
            case SimpleCipher:
                AlertDialog.showError("Currently not available.");
                break;
        }
        return null;
    }

    public static void encryptFile(File enc, File res) {
        switch (getCurrentCipher()) {
            case AES128:
                aes128.encryptFile(enc, res);
                break;
            case AES256:
                aes256.encryptFile(enc, res);
                break;
            case SimpleCipher:
                AlertDialog.showError("Not available.");
                break;
        }
    }

    public static void decryptFile(File dec, File res) {
        switch (getCurrentCipher()) {
            case AES128:
                aes128.decryptFile(dec, res);
                break;
            case AES256:
                aes256.decryptFile(dec, res);
                break;
            case SimpleCipher:
                AlertDialog.showError("Not available.");
                break;
        }
    }
    public static void generateKey() {
        switch (getCurrentCipher()) {
            case AES128:
                aes128.generateKey();
                break;
            case AES256:
                aes256.generateKey();
                break;
            case SimpleCipher:
                AlertDialog.showError("Currently not available.");
                break;
        }
    }

    public static String getKey() {
        switch (getCurrentCipher()) {
            case AES128:
                return aes128.getKey();
            case AES256:
                return aes256.getKey();
            case SimpleCipher:
                AlertDialog.showError("Currently not available.");
                break;
        }
        return null;
    }

    public static boolean setKey(String newKey) {
        switch (getCurrentCipher()) {
            case AES128:
                aes128.setKey(newKey);
                return true;
            case AES256:
                aes256.setKeyBase64(newKey);
//                if (newKey.length()!=16) {
//                    AlertDialog.showError("AES-256 key length must be at least 16 symbols!");
//                } else {
//                    aes256.setKey(newKey);
//                    return true;
//                }
                break;
            case SimpleCipher:
                AlertDialog.showError("Currently not available.");
                break;
        }
        return false;
    }
}

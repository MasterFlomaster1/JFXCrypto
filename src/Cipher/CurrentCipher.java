package Cipher;

import GUI.Main.AlertDialog;

import java.io.File;

public class CurrentCipher {

    private static int currentCipher;

    public static final int AES128 = 0;
    public static final int AES256 = 1;
    public static final int SimpleCipher = 2;
    public static final int DES = 3;
    public static final int DES3 = 4;

    private static Aes128 aes128 = new Aes128();
    private static Aes256 aes256 = new Aes256();
    private static Des des = new Des();
    private static Des3 des3 = new Des3();
    private static SimpleCipher simpleCipher;

    public static void setCurrentCipher(int a) {
        currentCipher = a;
    }

    private static int getCurrentCipher() {
        return currentCipher;
    }

    public static String getCurrentCipherName() {
        switch (getCurrentCipher()) {
            case AES128:
                return "AES-128";
            case AES256:
                return "AES-256";
            case DES:
                return "DES";
            case DES3:
                return "3DES";
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
            case DES:
                return des.encryptString(text);
            case DES3:
                return des3.encryptString(text);
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
            case DES:
                return des.decryptString(encryptedText);
            case DES3:
                return des3.decryptString(encryptedText);
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
            case DES:
                des.encryptFile(enc, res);
                break;
            case DES3:
                des3.encryptFile(enc, res);
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
            case DES:
                des.decryptFile(dec, res);
                break;
            case DES3:
                des3.decryptFile(dec, res);
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
            case DES:
                des.generateKey();
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
            case DES:
                return des.getKey();
            case DES3:
                return des3.getKey();
        }
        return null;
    }

    public static void setKey(String newKey) {
        switch (getCurrentCipher()) {
            case AES128:
                aes128.setKey(newKey);
                break;
            case AES256:
                aes256.setKeyBase64(newKey);
                break;
            case SimpleCipher:
                AlertDialog.showError("Currently not available.");
                break;
            case DES:
                des.setKey(newKey);
                break;
            case DES3:
                des3.setKey(newKey);
                break;
        }
    }
}

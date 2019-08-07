package Cipher;

import GUI.AlertDialog;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

class Aes128 {

    private SecretKey key;
    private Cipher cipher;

    Aes128() {
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
            AlertDialog.showError("AES-128 init error", e.toString());
            return;
        }
        generateKey();
    }

    String encryptString(String text) {
        try {
            cipher.  init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes()));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

    String decryptString(String encryptedText) {
        byte[] data = Base64.getDecoder().decode(encryptedText.getBytes());
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(data));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

    void encryptFile(File in, File out) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            AlertDialog.showError("AES-128 init error!", e.toString());
            return;
        }
        byte[] buffer = new byte[2048];
        try {
            FileOutputStream fileOut = new FileOutputStream(out);
            FileInputStream fileIn = new FileInputStream(in);
            CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher);
            while (fileIn.read(buffer, 0, buffer.length)!=-1) {
                cipherOut.write(buffer, 0, buffer.length);
            }
            fileIn.close();
            fileOut.close();
            cipherOut.close();
            AlertDialog.showInfo("File was successfully encrypted!");
        } catch (IOException e) {
            e.printStackTrace();
            AlertDialog.showError("AES-128 file encryption error!", e.toString());
        }
    }

    void decryptFile(File in, File out) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            AlertDialog.showError("AES-128 init error!", e.toString());
            return;
        }
        byte[] iv = cipher.getIV();
        byte[] buffer = new byte[2048];
        try {
            FileOutputStream fileOut = new FileOutputStream(out);
            FileInputStream fileIn = new FileInputStream(in);
            CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher);
            fileOut.write(iv);
            while (fileIn.read(buffer, 0, buffer.length)!=-1) {
                cipherOut.write(buffer, 0, buffer.length);
            }
            fileIn.close();
            fileOut.close();
            cipherOut.close();
            AlertDialog.showInfo("File was successfully encrypted!");
        } catch (IOException e) {
            e.printStackTrace();
            AlertDialog.showError("AES-128 file encryption error!", e.toString());
        }
    }

    void generateKey() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128);
            key = generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    void setKey(String stringKey) {
        key = new SecretKeySpec(stringKey.getBytes(), "AES");
        System.out.println("New key was set");
    }

    String getKey() {
        return new String(key.getEncoded());
    }

    private boolean checkKey(String inputKey) {
        return true;
    }

}

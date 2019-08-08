package Cipher;

import GUI.AlertDialog;
import Utils.RepairString;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

class Aes128 {

    private SecretKey key;
    private Cipher cipher;
    private IvParameterSpec ivParameterSpec;

    Aes128() {
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        generateIV();
        generateKey();
    }

    String encryptString(String text) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            AlertDialog.showError("AES-128 text encryption error", e.toString());
            return null;
        }
    }

    String decryptString(String encryptedText) {
        byte[] data;
        try {
            data = Base64.getDecoder().decode(encryptedText.getBytes(StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException caught");
            data = Base64.getDecoder().decode(RepairString.repairString(encryptedText).getBytes(StandardCharsets.UTF_8));
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
            return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            AlertDialog.showError("Error occurred while decrypting text", e.toString());
            return null;
        }
    }

    void encryptFile(File in, File out) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
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

    void decryptFile(File in, File out) {
        IvParameterSpec parameterSpec;
        try {
            byte[] iv = new byte[16];
            FileInputStream temp = new FileInputStream(in);
            temp.read(iv);
            parameterSpec = new IvParameterSpec(iv);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            AlertDialog.showError("AES-128 file decryption error!", e.toString());
            return;
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return;
            //perform file decryption without initialization vector
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
            AlertDialog.showInfo("File was successfully decrypted!");
        } catch (IOException e) {
            e.printStackTrace();
            AlertDialog.showError("AES-128 file decryption error!", e.toString());
        }
    }

    void generateKey() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128);
            key = generator.generateKey();
            System.out.println("AES-128 key generated");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void generateIV() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);
        ivParameterSpec = new IvParameterSpec(iv);
    }

    void setKey(String stringKey) {
        key = new SecretKeySpec(stringKey.getBytes(), "AES");
        System.out.println("AES-128 key set");
    }

    void setKeyBase64(String keyBase64) {
        key = new SecretKeySpec(Base64.getDecoder().decode(keyBase64), "AES");
        System.out.println("AES-128 key set");
    }

    String getKey() {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    private boolean checkKey(String inputKey) {
        return true;
    }

}

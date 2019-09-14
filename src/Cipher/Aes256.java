package Cipher;

import GUI.Main.AlertDialog;
import Utils.RepairString;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

class Aes256 {

    private SecretKey key;
    private Cipher cipher;
    private IvParameterSpec ivParameterSpec;
    private final int BLOCK_SIZE = 1024;

    Aes256() {
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
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
            AlertDialog.showError("AES-256 text encryption error", e.toString());
            return null;
        }
    }

    String decryptString(String encryptedText) {
        byte[] data;
        try {
            data = Base64.getDecoder().decode(encryptedText.getBytes(StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException caught");
            try {
                data = Base64.getDecoder().decode(RepairString.repairString(encryptedText).getBytes(StandardCharsets.UTF_8));
            } catch (IllegalArgumentException ex) {
                AlertDialog.showError("Can't decode Base64");
                ex.printStackTrace();
                return null;
            }
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
            cipher.init(Cipher.ENCRYPT_MODE, key, writeIV(out));
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            AlertDialog.showError("AES-256 init error!", e.toString());
            return;
        }
        byte[] buffer = new byte[BLOCK_SIZE];
        writeIV(out);
        try {
            FileOutputStream fileOut = new FileOutputStream(out);
            FileInputStream fileIn = new FileInputStream(in);
            CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher);
            int read;
            while ((read = fileIn.read(buffer))!=-1) {
                cipherOut.write(buffer, 0, read);
            }
            fileIn.close();
            fileOut.close();
            cipherOut.close();
            AlertDialog.showInfo("File was successfully encrypted!");
        } catch (IOException e) {
            e.printStackTrace();
            AlertDialog.showError("AES-256 file encryption error!", e.toString());
        }
    }

    void decryptFile(File in, File out) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, readIV(in));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            AlertDialog.showError("AES-256 file decryption error!", e.toString());
            return;
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
            return;
            //perform file decryption without initialization vector
        }
        byte[] buffer = new byte[BLOCK_SIZE];
        try {
            FileInputStream fileIn = new FileInputStream(in);
            //skip iv bytes
            fileIn.getChannel().position(cipher.getBlockSize());
            FileOutputStream fileOut = new FileOutputStream(out);

            CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
            int read;
            while ((read = cipherIn.read(buffer))!=-1) {
                fileOut.write(buffer, 0, read);
            }
            fileIn.close();
            fileOut.close();
            cipherIn.close();
            AlertDialog.showInfo("File was successfully decrypted!");
        } catch (IOException e) {
            e.printStackTrace();
            AlertDialog.showError("AES-256 file decryption error!", e.toString());
        }
    }

    void setKey(String stringKey) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] bytes = stringKey.getBytes(StandardCharsets.UTF_8);
            bytes = sha.digest(bytes);
            bytes = Arrays.copyOf(bytes, 16);
            key = new SecretKeySpec(bytes, "AES");
            System.out.println("AES-256 key set");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    void setKeyBase64(String keyBase64) {
        key = new SecretKeySpec(Base64.getDecoder().decode(keyBase64), "AES");
        System.out.println("AES-256 key set");
    }

    private void generateIV() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);
        ivParameterSpec = new IvParameterSpec(iv);
    }

    private IvParameterSpec writeIV(File file) {
        byte[] iv = new byte[cipher.getBlockSize()];
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.nextBytes(iv);
            FileOutputStream out = new FileOutputStream(file);
            out.write(iv);
            out.close();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(iv);
    }

    private IvParameterSpec readIV(File file) {
        byte[] iv = new byte[cipher.getBlockSize()];
        try {
            FileInputStream in = new FileInputStream(file);
            if (in.read(iv) != cipher.getBlockSize())
                throw new IOException("Cannot read the IV values");
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(iv);
    }

    void generateKey() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(256);
            key = generator.generateKey();
            System.out.println("AES-256 key generated");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    String getKey() {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    private boolean checkKey() {
        return true;
    }

}

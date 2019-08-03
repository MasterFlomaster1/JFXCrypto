package Cipher;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

class Aes128 {

    private SecretKey key;
    private Cipher cipher;

    Aes128() {
        generateKey();
    }

    String encryptString(String text) {
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes()));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

    String decryptString(String encryptedText) {
        byte[] data = Base64.getDecoder().decode(encryptedText.getBytes());
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(data));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

    void encryptFile(File in, File out) {

    }

    void decryptFile(File in, File out) {

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

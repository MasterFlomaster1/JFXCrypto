package dev.masterflomaster1.sjc.crypto;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BlockCipherImpl {

    public static byte[] encrypt(String algorithm, byte[] inputData, byte[] key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, algorithm);
//            Cipher cipher = Cipher.getInstance(algorithm, "BC");
            Cipher cipher = Cipher.getInstance(algorithm+"/ECB/PKCS5Padding", "BC");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(inputData);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypt(String algorithm, byte[] inputData, byte[] key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, algorithm);
//            Cipher cipher = Cipher.getInstance(algorithm, "BC");
            Cipher cipher = Cipher.getInstance(algorithm+"/ECB/PKCS5Padding", "BC");

            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(inputData);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Integer> getAvailableKeyLengths(String algorithm) {
        return switch (algorithm) {
            case "AES", "CAMELLIA", "RC6", "RIJNDAEL", "Serpent", "Tnepres", "Twofish" -> List.of(128, 192, 256);
            case "BLOWFISH" -> List.of(128, 256);
            case "CAST5", "IDEA", "NOEKEON", "RC2", "RC5", "SEED", "SM4", "TEA", "XTEA" -> List.of(128);
            case "CAST6" -> List.of(128, 160, 192, 224, 256);
            case "DES" -> List.of(64);
            case "DESEDE" -> List.of(128, 192);
            case "DSTU7624" -> List.of(128, 256, 512);
            case "GOST28147", "GOST3412-2015", "Threefish-256" -> List.of(256);
            case "SHACAL-2" -> List.of(128, 192, 256, 512);
            case "SKIPJACK" -> List.of(80);
            case "Threefish-1024" -> List.of(1024);
            case "Threefish-512" -> List.of(512);
            default -> Collections.emptyList();
        };
    }

    public static byte[] generateKey(String algorithm, int keySize) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(algorithm, "BC");
            keyGen.init(keySize);
            return keyGen.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] generatePasswordBasedKey(char[] password, int keySize) {
        var salt = Base64.getDecoder().decode("4WHuOVNv8nIwjrPhLpyPwA==");
        var f = PbeImpl.asyncHash("PBKDF2", password, salt, 10000, keySize);

        try {
            return f.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}

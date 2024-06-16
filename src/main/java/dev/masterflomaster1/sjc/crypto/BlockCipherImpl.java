package dev.masterflomaster1.sjc.crypto;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Collections;
import java.util.Set;

public class BlockCipherImpl {

    public static byte[] encrypt(String algorithm, byte[] inputData, byte[] key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, algorithm);
            Cipher cipher = Cipher.getInstance(algorithm, "BC");

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
            Cipher cipher = Cipher.getInstance(algorithm, "BC");

            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(inputData);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Set<String> getAvailableKeyLengths(String algorithm) {
        return switch (algorithm) {
            case "AES", "CAMELLIA", "RC6", "RIJNDAEL", "Serpent", "Tnepres", "Twofish" -> Set.of("128", "192", "256");
            case "BLOWFISH" -> Set.of("128", "256");
            case "CAST5", "IDEA", "NOEKEON", "RC2", "RC5", "SEED", "SM4", "TEA", "XTEA" -> Set.of("128");
            case "CAST6" -> Set.of("128", "160", "192", "224", "256");
            case "DES" -> Set.of("64");
            case "DESEDE" -> Set.of("128", "192");
            case "DSTU7624" -> Set.of("128", "256", "512");
            case "GOST28147", "GOST3412-2015", "Threefish-256" -> Set.of("256");
            case "SHACAL-2" -> Set.of("128", "192", "256", "512");
            case "SKIPJACK" -> Set.of("80");
            case "Threefish-1024" -> Set.of("1024");
            case "Threefish-512" -> Set.of("512");
            default -> Collections.emptySet();
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

}

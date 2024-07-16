package dev.masterflomaster1.sjc.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.Optional;

public final class StreamCipherImpl {

    private StreamCipherImpl() { }

    public static byte[] encrypt(String algorithm, byte[] iv, byte[] inputData, byte[] key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, algorithm);
            Cipher cipher = Cipher.getInstance(algorithm, "BC");

            if (getCorrespondingIvLengthBits(algorithm).isPresent())
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
            else
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            return cipher.doFinal(inputData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypt(String algorithm, byte[] iv, byte[] inputData, byte[] key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, algorithm);
            Cipher cipher = Cipher.getInstance(algorithm, "BC");

            if (getCorrespondingIvLengthBits(algorithm).isPresent())
                cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            else
                cipher.init(Cipher.DECRYPT_MODE, secretKey);

            return cipher.doFinal(inputData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Integer> getCorrespondingKeyLengths(String algorithm) {
        return switch (algorithm) {
            case "ARC4", "Grain128", "HC128", "ZUC-128", "VMPC", "VMPC-KSA3" -> List.of(128);
            case "CHACHA", "CHACHA7539", "CHACHA20-POLY1305", "HC256", "ZUC-256", "XSALSA20" -> List.of(256);
            case "SALSA20" -> List.of(128, 256);
            case "Grainv1" -> List.of(80);
            default -> throw new RuntimeException("Unsupported algorithm: " + algorithm);
        };
    }

    public static Optional<List<Integer>> getCorrespondingIvLengthBits(String algorithm) {
        return switch (algorithm) {
            case "ARC4" -> Optional.empty();
            case "CHACHA", "Grainv1", "SALSA20" -> Optional.of(List.of(64));
            case "CHACHA7539", "CHACHA20-POLY1305", "Grain128" -> Optional.of(List.of(96));
            case "HC128", "ZUC-128" -> Optional.of(List.of(128));
            case "HC256" -> Optional.of(List.of(256));
            case "XSALSA20" -> Optional.of(List.of(192));
            case "ZUC-256" -> Optional.of(List.of(200));
            case "VMPC", "VMPC-KSA3" -> Optional.of(List.of(8, 16, 32, 64, 128, 256, 512));
            default -> throw new RuntimeException("Unsupported algorithm: " + algorithm);
        };
    }
}

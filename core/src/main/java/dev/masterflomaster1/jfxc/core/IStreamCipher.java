package dev.masterflomaster1.jfxc.core;

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

@SuppressWarnings("SpellCheckingInspection")
public interface IStreamCipher {

    static Cipher of(String algorithm, int opmode, byte[] key, byte[] iv) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, algorithm);
            Cipher cipher = Cipher.getInstance(algorithm, "BC");

            if (getSupportedIvLength(algorithm).isPresent())
                cipher.init(opmode, secretKey, new IvParameterSpec(iv));
            else
                cipher.init(opmode, secretKey);

            return cipher;
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException |
                 InvalidAlgorithmParameterException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    static byte[] doFinal(Cipher cipher, byte[] value) {
        try {
            return cipher.doFinal(value);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    static List<Integer> getSupportedKeyLengths(String algorithm) {
        return switch (algorithm) {
            case "ARC4", "Grain128", "HC128", "ZUC-128", "VMPC", "VMPC-KSA3" -> List.of(128);
            case "CHACHA", "CHACHA7539", "CHACHA20-POLY1305", "HC256", "ZUC-256", "XSALSA20" -> List.of(256);
            case "SALSA20" -> List.of(128, 256);
            case "Grainv1" -> List.of(80);
            default -> throw new RuntimeException("Unsupported algorithm: " + algorithm);
        };
    }

    static Optional<List<Integer>> getSupportedIvLength(String algorithm) {
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

package dev.masterflomaster1.jfxc.core;

import lombok.Getter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public interface BlockCipher {

    /**
     * Creates and initializes a cipher instance with the specified parameters.
     *
     * @param algorithm the encryption algorithm
     * @param opmode the operation mode (e.g., encrypt or decrypt)
     * @param mode the encryption mode (e.g., ECB, CBC)
     * @param padding the padding scheme
     * @param key the encryption key
     * @param iv the initialization vector
     * @return an initialized {@code Cipher} instance
     */
    static Cipher of(String algorithm, int opmode, Mode mode, Padding padding, byte[] key, byte[] iv) {
        var m = mode.mode;
        var p = padding.padding;

        if (mode == Mode.GCM)
            p = "NoPadding";

        try {
            SecretKey secretKey = new SecretKeySpec(key, algorithm);

            Cipher cipher = Cipher.getInstance(algorithm + "/%s/%s".formatted(m, p), "BC");

            if (mode == Mode.ECB) {
                cipher.init(opmode, secretKey);
            } else if (mode == Mode.GCM) {
                GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
                cipher.init(opmode, secretKey, gcmSpec);
            } else {
                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
                cipher.init(opmode, secretKey, ivParameterSpec);
            }

            return cipher;
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException e) {
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

    static byte[] generateKey(String algorithm, int keySize) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(algorithm, "BC");
            keyGen.init(keySize);
            return keyGen.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the predefined block length in bits for a given encryption algorithm.
     *
     * @param algorithm the name of the encryption algorithm
     * @return the block length in bits
     */
    static int getBlockLength(String algorithm) {
        return switch (algorithm) {
            case "DES", "DESEDE", "BLOWFISH", "CAST5", "GOST28147", "IDEA", "RC2", "RC5", "SKIPJACK", "TEA", "XTEA" -> 64;
            case "SHACAL-2", "Threefish-256" -> 256;
            case "Threefish-512" -> 512;
            case "Threefish-1024" -> 1024;
            default -> 128;
        };
    }

    /**
     * Returns the supported encryption modes for a given algorithm.
     *
     * @param algorithm the name of the encryption algorithm
     * @return an array of supported encryption modes
     */
    static Mode[] getSupportedModes(String algorithm) {
        return switch (algorithm) {
            case "AES", "CAST6", "NOEKEON", "RC6", "RIJNDAEL", "SEED", "SM4",
                 "Serpent", "Tnepres", "Twofish" -> Mode.values();
            case "GOST3412-2015" -> new Mode[] {Mode.ECB, Mode.CBC, Mode.CFB, Mode.OFB, Mode.GCM};
            case "GOST28147" -> new Mode[] {Mode.ECB, Mode.CBC, Mode.CFB, Mode.OFB, Mode.CTR};
            default -> new Mode[] {Mode.ECB, Mode.CBC, Mode.CFB, Mode.OFB};
        };
    }

    /**
     * Temporarily removed {@code DSTU7624 512} to fix key generation exception
     */
    static List<Integer> getSupportedKeyLengths(String algorithm) {
        return switch (algorithm) {
            case "AES", "CAMELLIA", "RC6", "RIJNDAEL", "Serpent", "Tnepres", "Twofish" -> List.of(128, 192, 256);
            case "BLOWFISH", "DSTU7624" -> List.of(128, 256);
            case "CAST5", "IDEA", "NOEKEON", "RC2", "RC5", "SEED", "SM4", "TEA", "XTEA" -> List.of(128);
            case "CAST6" -> List.of(128, 160, 192, 224, 256);
            case "DES" -> List.of(64);
            case "DESEDE" -> List.of(128, 192);
            case "GOST28147", "GOST3412-2015", "Threefish-256" -> List.of(256);
            case "SHACAL-2" -> List.of(128, 192, 256, 512);
            case "SKIPJACK" -> List.of(80);
            case "Threefish-1024" -> List.of(1024);
            case "Threefish-512" -> List.of(512);
            default -> Collections.emptyList();
        };
    }

    @Getter
    enum Mode {
        ECB("ECB"),
        CBC("CBC"),
        CFB("CFB"),
        OFB("OFB"),
        CTR("CTR"),
        GCM("GCM");

        private final String mode;

        Mode(String mode) {
            this.mode = mode;
        }

        @SuppressWarnings("unused")
        public static Mode fromString(String value) {
            for (Mode m : Mode.values()) {
                if (m.getMode().equalsIgnoreCase(value))
                    return m;
            }

            throw new IllegalArgumentException(value);
        }
    }

    @Getter
    enum Padding {

        PKCS5("PKCS5Padding"),
        PKCS7("PKCS7Padding"),
        ISO10126("ISO10126Padding"),
        ZeroByte("ZeroBytePadding");

        private final String padding;

        Padding(String padding) {
            this.padding = padding;
        }

        @SuppressWarnings("unused")
        public static Padding fromString(String value) {
            for (Padding p : Padding.values()) {
                if (p.padding.equalsIgnoreCase(value)) {
                    return p;
                }
            }

            throw new IllegalArgumentException(value);
        }

    }

}

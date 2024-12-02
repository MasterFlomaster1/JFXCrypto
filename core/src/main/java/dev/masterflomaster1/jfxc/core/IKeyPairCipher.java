package dev.masterflomaster1.jfxc.core;

import org.bouncycastle.jce.spec.IESParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.ECGenParameterSpec;
import java.util.List;

public interface IKeyPairCipher {

    static Cipher of(String algorithm, int opmode, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm, "BC");
            cipher.init(opmode, key);

            return cipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    static Cipher of(String algorithm, int opmode, Key key, byte[] derivation, byte[] encoding, byte[] nonce) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm, "BC");
            cipher.init(opmode, key, new IESParameterSpec(derivation, encoding, 128, 128, nonce));

            return cipher;
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException |
                 NoSuchPaddingException | NoSuchProviderException e) {
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

    static KeyPair generateKeyPair(String algorithm, String option) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm, "BC");

            if ("EC".equals(algorithm))
                keyGen.initialize(new ECGenParameterSpec(option));
            else
                keyGen.initialize(Integer.parseInt(option));

            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the supported key options for the specified algorithm.
     *
     * @param algorithm the name of the encryption algorithm
     * @return a list of supported key options, represented as key lengths (in bits)
     *         or curve names for elliptic-curve algorithms
     */
    static List<String> getSupportedKeyOptions(String algorithm) {
        return switch (algorithm) {
            case "DHIES", "DHIESWITHDESEDE-CBC", "DHIESwithAES-CBC", "ELGAMAL", "IES", "IESWITHDESEDE-CBC",
                 "IESwithAES-CBC" -> List.of("512", "1024", "2048");
            case "RSA" -> List.of("512", "1024", "2048", "3072", "4096");
            case "ECIES", "ECIESwithAES-CBC", "ECIESwithDESEDE-CBC", "ECIESwithSHA1", "ECIESwithSHA1andAES-CBC",
                 "ECIESwithSHA1andDESEDE-CBC", "ECIESwithSHA256", "ECIESwithSHA256andAES-CBC",
                 "ECIESwithSHA256andDESEDE-CBC", "ECIESwithSHA384", "ECIESwithSHA384andAES-CBC",
                 "ECIESwithSHA384andDESEDE-CBC", "ECIESwithSHA512", "ECIESwithSHA512andAES-CBC",
                 "ECIESwithSHA512andDESEDE-CBC" -> List.of("secp256r1", "secp384r1", "secp521r1");
            default -> throw new RuntimeException("Unsupported algorithm: " + algorithm);
        };
    }

    /**
     * Returns the key generation algorithm associated with the specified encryption algorithm.
     *
     * @param algorithm the name of the encryption algorithm
     * @return the name of the key generation algorithm (e.g., "DH", "EC", "ElGamal", "RSA")
     */
    static String getSupportedKeyGenAlgorithm(String algorithm) {
        return switch (algorithm) {
            case "DHIES", "DHIESWITHDESEDE-CBC", "DHIESwithAES-CBC" -> "DH";
            case "ECIES", "ECIESwithAES-CBC", "ECIESwithDESEDE-CBC", "ECIESwithSHA1", "ECIESwithSHA1andAES-CBC",
                 "ECIESwithSHA1andDESEDE-CBC", "ECIESwithSHA256", "ECIESwithSHA256andAES-CBC",
                 "ECIESwithSHA256andDESEDE-CBC", "ECIESwithSHA384", "ECIESwithSHA384andAES-CBC",
                 "ECIESwithSHA384andDESEDE-CBC", "ECIESwithSHA512", "ECIESwithSHA512andAES-CBC",
                 "ECIESwithSHA512andDESEDE-CBC" -> "EC";
            case "ELGAMAL", "ELGAMAL/PKCS1" -> "ElGamal";
            case "IES", "IESWITHDESEDE-CBC", "IESwithAES-CBC" -> "DH";
            case "RSA", "RSA/1", "RSA/2", "RSA/ISO9796-1", "RSA/OAEP", "RSA/PKCS1", "RSA/RAW" -> "RSA";
            default -> throw new RuntimeException("Unsupported algorithm: " + algorithm);
        };
    }

    /**
     * Returns the supported nonce length (in bytes) for the specified algorithm.
     *
     * @param algorithm the name of the encryption algorithm
     * @return the nonce length in bytes
     */
    static int getSupportedNonceLength(String algorithm) {
        return switch (algorithm) {
            case "DHIESWITHDESEDE-CBC", "ECIESwithDESEDE-CBC", "ECIESwithSHA1andDESEDE-CBC",
                 "ECIESwithSHA256andDESEDE-CBC", "ECIESwithSHA384andDESEDE-CBC", "ECIESwithSHA512andDESEDE-CBC",
                 "IESWITHDESEDE-CBC" -> 8;
            case "DHIESwithAES-CBC", "ECIESwithAES-CBC", "ECIESwithSHA1andAES-CBC", "ECIESwithSHA256andAES-CBC",
                 "ECIESwithSHA384andAES-CBC", "ECIESwithSHA512andAES-CBC", "IESwithAES-CBC" -> 16;
            default -> throw new RuntimeException("Unsupported algorithm: " + algorithm);
        };
    }

}

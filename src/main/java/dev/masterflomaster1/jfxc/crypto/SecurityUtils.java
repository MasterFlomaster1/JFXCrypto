package dev.masterflomaster1.jfxc.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SecurityUtils {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private static final TreeSet<String> DIGESTS = new TreeSet<>();
    private static final TreeSet<String> HMACS = new TreeSet<>();
    private static final TreeSet<String> PBKDFS = new TreeSet<>();
    private static final TreeSet<String> SIGNATURES = new TreeSet<>();
    private static final TreeSet<String> BLOCK_CIPHERS = new TreeSet<>();
    private static final TreeSet<String> STREAM_CIPHERS = new TreeSet<>();
    private static final TreeSet<String> ASYMMETRIC_CIPHERS = new TreeSet<>();
    private static final TreeSet<String> HYBRID_ASYMMETRIC_CIPHERS = new TreeSet<>();
    private static final Pattern OID_PATTERN = Pattern.compile("^(OID\\.)?(\\d+\\.)+\\d+$");

    private static final PemKeyPairPersistence pemKeyPairPersistence = new PemKeyPairPersistence();

    private SecurityUtils() { }

    public static TreeSet<String> getDigests() {
        return DIGESTS;
    }

    public static TreeSet<String> getHmacs() {
        return HMACS;
    }

    public static TreeSet<String> getPbkdfs() {
        return PBKDFS;
    }

    public static TreeSet<String> getBlockCiphers() {
        return BLOCK_CIPHERS;
    }

    public static TreeSet<String> getSignatures() {
        return SIGNATURES;
    }

    public static TreeSet<String> getStreamCiphers() {
        return STREAM_CIPHERS;
    }

    public static TreeSet<String> getAsymmetricCiphers() {
        return ASYMMETRIC_CIPHERS;
    }

    public static TreeSet<String> getHybridAsymmetricCiphers() {
        return HYBRID_ASYMMETRIC_CIPHERS;
    }

    public static PemKeyPairPersistence getPemKeyPairPersistence() {
        return pemKeyPairPersistence;
    }

    public static void init() {
        // Setup Unlimited Strength Jurisdiction Policy
        Security.setProperty("crypto.policy", "unlimited");
        Security.addProvider(new BouncyCastleProvider());
        Provider p = Security.getProvider("BC");

        p.getServices().stream()
                .filter(s -> !SecurityUtils.isOID(s.getAlgorithm()))
                .forEach(s -> {
                    String type = s.getType();
                    String algorithm = s.getAlgorithm();

                    if ("SecretKeyFactory".equals(type) && algorithm.startsWith("PBKDF2")) {
                        PBKDFS.add(algorithm);
                    } else if ("MessageDigest".equals(type)) {
                        DIGESTS.add(algorithm);
                    } else if ("Mac".equals(type) && algorithm.startsWith("HMAC")) {
                        HMACS.add(algorithm);
                    } else if ("Signature".equals(type)) {
                        SIGNATURES.add(algorithm);
                    }
                });

        SIGNATURES.removeAll(List.of(
                "ECGOST3410-2012-512",
                "GOST3411-2012-512WITHECGOST3410-2012-512",
                "SHA1WITHECDDSA",
                "SHA224WITHECDDSA",
                "SHA256WITHECDDSA",
                "SHA256WITHECNR",
                "SHA3-224WITHECDDSA",
                "SHA3-256WITHECDDSA",
                "SHA3-384WITHECDDSA",
                "SHA3-512WITHECDDSA",
                "SHA384WITHECDDSA",
                "SHA384WITHECNR",
                "SHA512WITHECDDSA",
                "SHA512WITHECNR"
        ));


        BLOCK_CIPHERS.addAll(List.of(
                "AES",
                "BLOWFISH",
                "CAMELLIA",
                "CAST5",
                "CAST6",
                "DES",
                "DESEDE",
                "DSTU7624",
                "GOST28147",
                "GOST3412-2015",
                "IDEA",
                "NOEKEON",
                "RC2",
                "RC5",
                "RC6",
                "RIJNDAEL",
                "SEED",
                "SHACAL-2",
                "SKIPJACK",
                "SM4",
                "Serpent",
                "TEA",
                "Threefish-1024",
                "Threefish-256",
                "Threefish-512",
                "Tnepres",
                "Twofish",
                "XTEA"
        ));

        STREAM_CIPHERS.addAll(List.of(
                "ARC4",
                "CHACHA",
                "CHACHA20-POLY1305",
                "CHACHA7539",
                "Grain128",
                "Grainv1",
                "HC128",
                "HC256",
                "SALSA20",
                "XSALSA20",
                "ZUC-128",
                "ZUC-256",
                "VMPC",
                "VMPC-KSA3"
        ));

        // Algorithms excluded: NTRU, ELGAMAL/PKCS1, RSA/1, RSA/2, RSA/ISO9796-1, RSA/OAEP, RSA/PKCS1, RSA/RAW

        ASYMMETRIC_CIPHERS.addAll(List.of(
                "DHIES",
                "ECIES",
                "ECIESwithSHA1",
                "ECIESwithSHA256",
                "ECIESwithSHA384",
                "ECIESwithSHA512",
                "ELGAMAL",
                "IES",
                "RSA"
        ));

        HYBRID_ASYMMETRIC_CIPHERS.addAll(List.of(
                "DHIESWITHDESEDE-CBC",
                "DHIESwithAES-CBC",
                "ECIESwithAES-CBC",
                "ECIESwithDESEDE-CBC",
                "ECIESwithSHA1andAES-CBC",
                "ECIESwithSHA1andDESEDE-CBC",
                "ECIESwithSHA256andAES-CBC",
                "ECIESwithSHA256andDESEDE-CBC",
                "ECIESwithSHA384andAES-CBC",
                "ECIESwithSHA384andDESEDE-CBC",
                "ECIESwithSHA512andAES-CBC",
                "ECIESwithSHA512andDESEDE-CBC",
                "IESWITHDESEDE-CBC",
                "IESwithAES-CBC"
        ));
    }

    private static boolean isOID(String input) {
        Matcher matcher = OID_PATTERN.matcher(input);
        return matcher.matches();
    }

    public static byte[] generatePasswordBasedKey(char[] password, int keySize, byte[] salt) {
        var f = PbeImpl.asyncHash("PBKDF2", password, salt, 10000, keySize);

        try {
            return f.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] generateIV(int lengthBits) {
        byte[] iv = new byte[lengthBits / 8];
        SECURE_RANDOM.nextBytes(iv);
        return iv;
    }

    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        SECURE_RANDOM.nextBytes(salt);
        return salt;
    }
}

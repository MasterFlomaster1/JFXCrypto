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
    private static final TreeSet<String> BLOCK_CIPHERS = new TreeSet<>();
    private static final TreeSet<String> STREAM_CIPHERS = new TreeSet<>();
    private static final Pattern OID_PATTERN = Pattern.compile("^(OID\\.)?(\\d+\\.)+\\d+$");

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

    public static TreeSet<String> getStreamCiphers() {
        return STREAM_CIPHERS;
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
                    }
                });


        BLOCK_CIPHERS.addAll(List.of("AES", "BLOWFISH", "CAMELLIA", "CAST5", "CAST6", "DES", "DESEDE",
                "DSTU7624", "GOST28147", "GOST3412-2015", "IDEA", "NOEKEON",
                "RC2", "RC5", "RC6", "RIJNDAEL", "SEED", "SHACAL-2", "SKIPJACK", "SM4", "Serpent", "TEA",
                "Threefish-1024", "Threefish-256", "Threefish-512", "Tnepres", "Twofish", "XTEA"));

        STREAM_CIPHERS.addAll(List.of("ARC4", "CHACHA", "CHACHA20-POLY1305", "CHACHA7539", "Grain128", "Grainv1",
                "HC128", "HC256", "SALSA20", "XSALSA20", "ZUC-128", "ZUC-256", "VMPC", "VMPC-KSA3"));
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

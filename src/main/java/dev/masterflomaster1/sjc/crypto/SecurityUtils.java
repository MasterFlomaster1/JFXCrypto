package dev.masterflomaster1.sjc.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecurityUtils {

    private static final TreeSet<String> digests = new TreeSet<>();
    private static final TreeSet<String> hmacs = new TreeSet<>();
    private static final TreeSet<String> pbkdfs = new TreeSet<>();
    private static final TreeSet<String> blockCiphers = new TreeSet<>();
    private static final Pattern oidPattern = Pattern.compile("^(OID\\.)?(\\d+\\.)+\\d+$");

    public static TreeSet<String> getDigests() {
        return digests;
    }

    public static TreeSet<String> getHmacs() {
        return hmacs;
    }

    public static TreeSet<String> getPbkdfs() {
        return pbkdfs;
    }

    public static TreeSet<String> getBlockCiphers() {
        return blockCiphers;
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
                        pbkdfs.add(algorithm);
                    } else if ("MessageDigest".equals(type)) {
                        digests.add(algorithm);
                    } else if ("Mac".equals(type) && algorithm.startsWith("HMAC")) {
                        hmacs.add(algorithm);
                    }
                });


        blockCiphers.addAll(List.of("AES", "BLOWFISH", "CAMELLIA", "CAST5", "CAST6", "DES", "DESEDE",
                "DSTU7624", "GOST28147", "GOST3412-2015", "IDEA", "NOEKEON",
                "RC2", "RC5", "RC6", "RIJNDAEL", "SEED", "SHACAL-2", "SKIPJACK", "SM4", "Serpent", "TEA",
                "Threefish-1024", "Threefish-256", "Threefish-512", "Tnepres", "Twofish", "XTEA"));
    }

    private static boolean isOID(String input) {
        Matcher matcher = oidPattern.matcher(input);
        return matcher.matches();
    }
}

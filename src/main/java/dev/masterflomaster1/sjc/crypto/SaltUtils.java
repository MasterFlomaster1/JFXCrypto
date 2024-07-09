package dev.masterflomaster1.sjc.crypto;

import java.security.SecureRandom;

public class SaltUtils {

    private static final SecureRandom random = new SecureRandom();;

    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

}

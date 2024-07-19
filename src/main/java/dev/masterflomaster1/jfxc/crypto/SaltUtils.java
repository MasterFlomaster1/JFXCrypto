package dev.masterflomaster1.jfxc.crypto;

import java.security.SecureRandom;

public final class SaltUtils {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private SaltUtils() { }

    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        SECURE_RANDOM.nextBytes(salt);
        return salt;
    }

}

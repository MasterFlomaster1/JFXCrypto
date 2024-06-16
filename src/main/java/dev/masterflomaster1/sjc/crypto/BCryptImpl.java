package dev.masterflomaster1.sjc.crypto;

import org.bouncycastle.crypto.generators.BCrypt;

import java.security.SecureRandom;

public class BCryptImpl {

    public byte[] hash(byte[] value, int rounds) {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(new byte[16]);

        return BCrypt.generate(value, salt, rounds);
    }

}

package dev.masterflomaster1.sjc.crypto.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AffineCipherImplTest {

    private final String text = "The quick brown fox jumps over 13 lazy dogs.";

    @Test
    void shouldEncryptAndDecryptAffineCipher() {
        var a = AffineCipherImpl.encrypt(text, 5, 8);
        var b = AffineCipherImpl.decrypt(a, 5, 8);

        assertEquals(text.toUpperCase(), b);
    }

    @Test
    void shouldEncryptAndDecryptAffineCipherWithAllOptions() {
        for (int a = 1; a <= 25; a+=2) {

            if (a == 13)
                continue;

            for (int b = 1; b <= 25; b++) {
                var encrypted = AffineCipherImpl.encrypt(text, a, b);
                var decrypted = AffineCipherImpl.decrypt(encrypted, a, b);

                assertEquals(text.toUpperCase(), decrypted);
            }
        }
    }

}
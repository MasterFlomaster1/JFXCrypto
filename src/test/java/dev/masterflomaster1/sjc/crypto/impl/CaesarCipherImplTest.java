package dev.masterflomaster1.sjc.crypto.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CaesarCipherImplTest {

    @Test
    void shouldEncryptAndDecryptCaesarCipher() {
        var text = "attacking tonight";

        var a = CaesarCipherImpl.encrypt(text, 5);
        var b = CaesarCipherImpl.decrypt(a, 5);

        assertEquals(text, b);
    }


}
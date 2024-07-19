package dev.masterflomaster1.jfxc.crypto.classic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VigenereCipherImplTest {

    @Test
    void shouldEncryptAndDecryptVigenereCipher() {
        var text = "attacking tonight";

        var a = VigenereCipherImpl.encrypt(text, "OCULORHINOLARINGOLOGY");
        var b = VigenereCipherImpl.decrypt(a, "OCULORHINOLARINGOLOGY");

        assertEquals(text.toUpperCase(), b);
    }

}
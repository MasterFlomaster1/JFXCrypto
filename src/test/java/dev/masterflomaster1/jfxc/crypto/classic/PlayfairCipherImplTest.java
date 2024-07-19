package dev.masterflomaster1.jfxc.crypto.classic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayfairCipherImplTest {

    @Test
    void shouldEncryptAndDecryptPlayfairCipher() {
        var text = "Attack at dawn";

        var a = PlayfairCipherImpl.encrypt(text, "CODE");
        var b = PlayfairCipherImpl.decrypt(a, "CODE");

        assertEquals(text.replace(" ", "").toUpperCase(), b);
    }

}
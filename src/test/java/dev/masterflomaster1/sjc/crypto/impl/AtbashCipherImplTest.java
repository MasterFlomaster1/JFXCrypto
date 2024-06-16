package dev.masterflomaster1.sjc.crypto.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AtbashCipherImplTest {

    @Test
    void shouldEncryptAndDecryptAtbash() {
        var a = AtbashCipherImpl.encrypt("Hello world!");
        var b = AtbashCipherImpl.decrypt(a);

        assertEquals("Hello world!".toUpperCase(), b);
    }

}
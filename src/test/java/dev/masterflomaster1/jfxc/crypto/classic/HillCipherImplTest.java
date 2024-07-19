package dev.masterflomaster1.jfxc.crypto.classic;

import org.junit.jupiter.api.Test;

class HillCipherImplTest {

    @Test
    void shouldEncryptAndDecryptHillCipher() {
        var a = HillCipherImpl.encrypt("Hello world", new int[][]{{6, 24, 1}, {13, 16, 10}, {20, 17, 15}});

        System.out.println(a);
    }

}
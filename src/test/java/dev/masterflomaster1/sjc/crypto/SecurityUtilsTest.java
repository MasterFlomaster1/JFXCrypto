package dev.masterflomaster1.sjc.crypto;

import org.junit.jupiter.api.Test;

class SecurityUtilsTest {

    @Test
    void shouldReturnValues() {
        SecurityUtils.init();

        var a = SecurityUtils.getDigests().size();
        a += SecurityUtils.getHmacs().size();
        a += SecurityUtils.getPbkdfs().size();
        System.out.println(a);

        System.out.println(SecurityUtils.getBlockCiphers().size());

        System.out.println(SecurityUtils.getBlockCiphers());
    }

}
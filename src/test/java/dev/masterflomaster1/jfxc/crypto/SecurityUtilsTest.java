package dev.masterflomaster1.jfxc.crypto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SecurityUtilsTest {

    @BeforeAll
    static void beforeAll() {
        SecurityUtils.init();
    }

    @Test
    void shouldAllPrintSupportedAlgorithms() {
        System.out.println("Block ciphers: " + SecurityUtils.getBlockCiphers().size());
        System.out.println("Digests: " + SecurityUtils.getDigests().size());
        System.out.println("Hmacs: " + SecurityUtils.getHmacs().size());
        System.out.println("Pbkdfs: " + SecurityUtils.getPbkdfs().size());
        System.out.println("Signatures: " + SecurityUtils.getSignatures());
    }

}
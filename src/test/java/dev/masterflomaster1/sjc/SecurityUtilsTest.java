package dev.masterflomaster1.sjc;

import dev.masterflomaster1.sjc.crypto.SecurityUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.Security;
import java.util.stream.Collectors;

class SecurityUtilsTest {

    @BeforeAll
    static void beforeAll() {
        SecurityUtils.init();
    }

    @Test
    void test() {
        System.out.println(SecurityUtils.getDigests());
        System.out.println(SecurityUtils.getDigests().size());
    }

    @Test
    void shouldPrintAllAlgorithmsForBlockCiphers() {
        var a = SecurityUtils.getBlockCiphers().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        System.out.println(a);
    }

}
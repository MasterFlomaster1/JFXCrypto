package dev.masterflomaster1.sjc;

import dev.masterflomaster1.sjc.crypto.SecurityUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import java.security.Security;

class SecurityUtilsTest {

    @Test
    void test() {
        Security.setProperty("crypto.policy", "unlimited");
        Security.addProvider(new BouncyCastleProvider());
        SecurityUtils.init();

        System.out.println(SecurityUtils.getDigests());
        System.out.println(SecurityUtils.getDigests().size());
    }

}
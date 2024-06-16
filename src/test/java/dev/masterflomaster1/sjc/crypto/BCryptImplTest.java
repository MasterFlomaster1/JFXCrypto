package dev.masterflomaster1.sjc.crypto;

import org.bouncycastle.crypto.generators.BCrypt;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.Security;

import static org.junit.jupiter.api.Assertions.*;

class BCryptImplTest {

    @BeforeAll
    static void beforeAll() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    void test() {
        BCryptImpl bCrypt = new BCryptImpl();

        byte[] res = bCrypt.hash("hello world".getBytes(StandardCharsets.UTF_8), 12);

        System.out.println(Utils.base64(res));
        System.out.println(Utils.convertToHexString(res));
    }

}
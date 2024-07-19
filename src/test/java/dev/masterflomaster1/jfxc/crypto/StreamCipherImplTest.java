package dev.masterflomaster1.jfxc.crypto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class StreamCipherImplTest {

    @BeforeAll
    static void beforeAll() {
        SecurityUtils.init();
    }

    @Test
    void shouldEncryptAndDecryptUsingAllAlgorithmsAndIvs() {
        char[] pwd = "test_secret_password".toCharArray();
        byte[] data = "Payload".getBytes(StandardCharsets.UTF_8);
        byte[] salt = Base64.getDecoder().decode("4WHuOVNv8nIwjrPhLpyPwA==");

        SecurityUtils.getStreamCiphers().forEach(cipher -> {
            System.out.println(cipher);

            var len = StreamCipherImpl.getCorrespondingKeyLengths(cipher).get(0);
            byte[] pass = SecurityUtils.generatePasswordBasedKey(pwd, len, salt);

            var ivLenOptional = StreamCipherImpl.getCorrespondingIvLengthBits(cipher);
            if (ivLenOptional.isPresent()) {
                ivLenOptional.get().forEach(ivLen -> {
                    System.out.println(ivLen);
                    byte[] iv = SecurityUtils.generateIV(ivLen);

                    var encrypted = StreamCipherImpl.encrypt(cipher, iv, data, pass);
                    var decrypted = StreamCipherImpl.decrypt(cipher, iv, encrypted, pass);

                    assertArrayEquals(data, decrypted);
                });

                return;
            }

            var encrypted = StreamCipherImpl.encrypt(cipher, new byte[] {}, data, pass);
            var decrypted = StreamCipherImpl.decrypt(cipher, new byte[] {}, encrypted, pass);

            assertArrayEquals(data, decrypted);
        });
    }

}
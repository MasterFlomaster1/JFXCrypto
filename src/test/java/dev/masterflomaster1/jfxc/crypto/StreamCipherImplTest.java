package dev.masterflomaster1.jfxc.crypto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

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

    @Test
    void shouldEncryptAndDecryptFileUsingNio() throws IOException, InterruptedException, ExecutionException {
        Path input = Paths.get(System.getProperty("user.home"), "Desktop", "a.mp4");
        Path output = Paths.get(System.getProperty("user.home"), "Desktop", "enc");
        Path decrypted = Paths.get(System.getProperty("user.home"), "Desktop", "result.mp4");

        assumeTrue(Files.exists(input), "Target file does not exist");
        Files.write(output, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.write(decrypted, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        String algo = "SALSA20";
        var len = StreamCipherImpl.getCorrespondingIvLengthBits(algo).get().get(0);
        var iv = SecurityUtils.generateIV(len);
        char[] pwd = "test_secret_password".toCharArray();
        byte[] salt = Base64.getDecoder().decode("4WHuOVNv8nIwjrPhLpyPwA==");
        byte[] pass = SecurityUtils.generatePasswordBasedKey(pwd, 128, salt);

        StreamCipherImpl.nioEncrypt(
                input,
                output,
                algo,
                iv,
                pass
        );

        StreamCipherImpl.nioDecrypt(
                output,
                decrypted,
                algo,
                iv,
                pass
        );

        var h1 = UnkeyedCryptoHash.asyncHash("SHA-256", input.toAbsolutePath().toString()).get();
        var h2 = UnkeyedCryptoHash.asyncHash("SHA-256", decrypted.toAbsolutePath().toString()).get();
        assertArrayEquals(h1, h2);

    }

}
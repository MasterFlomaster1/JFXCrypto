package dev.masterflomaster1.jfxc.core;

import dev.masterflomaster1.jfxc.core.io.CipherNIO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
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

class IStreamCipherTest {

    @BeforeAll
    static void beforeAll() {
        SecurityUtils.init();
    }

    @Test
    void shouldEncryptAndDecryptUsingAllAlgorithmsAndIvs() {
        char[] pwd = "test_secret_password".toCharArray();
        byte[] data = "Payload".getBytes(StandardCharsets.UTF_8);
        byte[] salt = Base64.getDecoder().decode("4WHuOVNv8nIwjrPhLpyPwA==");

        SecurityUtils.getStreamCiphers().forEach(algo -> {
            System.out.println(algo);

            var len = IStreamCipher.getSupportedKeyLengths(algo).getLast();
            byte[] pass = SecurityUtils.generatePasswordBasedKey(pwd, len, salt);

            var ivLenOptional = IStreamCipher.getSupportedIvLength(algo);
            if (ivLenOptional.isPresent()) {
                ivLenOptional.get().forEach(ivLen -> {
                    byte[] iv = SecurityUtils.generateIV(ivLen);

                    var enc = IStreamCipher.of(algo, Cipher.ENCRYPT_MODE, pass, iv);
                    var dec = IStreamCipher.of(algo, Cipher.DECRYPT_MODE, pass, iv);

                    var a = IStreamCipher.doFinal(enc, data);
                    var b = IStreamCipher.doFinal(dec, a);

                    assertArrayEquals(data, b);
                });

                return;
            }

            var enc = IStreamCipher.of(algo, Cipher.ENCRYPT_MODE, pass, null);
            var dec = IStreamCipher.of(algo, Cipher.DECRYPT_MODE, pass, null);
            var a = IStreamCipher.doFinal(enc, data);
            var b = IStreamCipher.doFinal(dec, a);

            assertArrayEquals(data, b);
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

        var nio = new CipherNIO();

        String algo = "SALSA20";
        var len = IStreamCipher.getSupportedIvLength(algo).get().getFirst();
        var iv = SecurityUtils.generateIV(len);
        char[] pwd = "test_secret_password".toCharArray();
        byte[] salt = Base64.getDecoder().decode("4WHuOVNv8nIwjrPhLpyPwA==");
        byte[] pass = SecurityUtils.generatePasswordBasedKey(pwd, 128, salt);

        var enc = IStreamCipher.of(algo, Cipher.ENCRYPT_MODE, pass, iv);
        var dec = IStreamCipher.of(algo, Cipher.DECRYPT_MODE, pass, iv);

        nio.encrypt(enc, input, output);
        nio.decrypt(dec, output, decrypted);

        var h1 = HashImpl.asyncHash("SHA-256", input.toAbsolutePath().toString()).get();
        var h2 = HashImpl.asyncHash("SHA-256", decrypted.toAbsolutePath().toString()).get();
        assertArrayEquals(h1, h2);

    }

}
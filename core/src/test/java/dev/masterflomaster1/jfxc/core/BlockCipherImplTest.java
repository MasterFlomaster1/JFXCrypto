package dev.masterflomaster1.jfxc.core;

import dev.masterflomaster1.jfxc.core.io.CipherBlockingIO;
import dev.masterflomaster1.jfxc.core.io.CipherIO;
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
import java.util.HexFormat;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class BlockCipherImplTest {

    private final CipherIO bio = new CipherBlockingIO();
    private final CipherIO nio = new CipherNIO();

    @BeforeAll
    static void beforeAll() {
        SecurityUtils.init();
    }

    @Test
    void shouldGenerateKeysForAllAlgorithms() {
        SecurityUtils.getBlockCiphers().forEach(cipher -> {
            var list = IBlockCipher.getSupportedKeyLengths(cipher);

            list.forEach(len -> {
                var key = IBlockCipher.generateKey(cipher, len);
                System.out.printf("%s key (%d): %s\n", cipher, key.length*8, HexFormat.of().formatHex(key));
            });
        });
    }

    @Test
    void shouldGeneratePasswordBasedKeys() {
        char[] pwd = "test_secret_password".toCharArray();

        SecurityUtils.getBlockCiphers().forEach(cipher -> {
            var list = IBlockCipher.getSupportedKeyLengths(cipher);

            list.forEach(len -> {
                var key = generatePasswordBasedKey(pwd, len);
                System.out.printf("%s key (%d): %s\n", cipher, key.length*8, HexFormat.of().formatHex(key));
            });
        });
    }

    @Test
    void shouldEncryptAndDecryptWithAllModes() {
        char[] pwd = "test_secret_password".toCharArray();
        byte[] data = "Payload".getBytes(StandardCharsets.UTF_8);

        final var padding = IBlockCipher.Padding.PKCS5;

        SecurityUtils.getBlockCiphers().forEach(algo -> {
            var keyLen = IBlockCipher.getSupportedKeyLengths(algo).get(0);
            var key = generatePasswordBasedKey(pwd, keyLen);

            for (var mode: IBlockCipher.getSupportedModes(algo)) {
                System.out.printf("%s %d: %s\n", algo, keyLen, mode);

                byte[] a, b;

                if (mode == IBlockCipher.Mode.ECB)  {
                    a = IBlockCipher.doFinal(IBlockCipher.of(algo, Cipher.ENCRYPT_MODE, mode, padding, key, null), data);
                    b = IBlockCipher.doFinal(IBlockCipher.of(algo, Cipher.DECRYPT_MODE, mode, padding, key, null), a);
                } else {
                    var iv = SecurityUtils.generateIV(IBlockCipher.getBlockLength(algo));

                    a = IBlockCipher.doFinal(IBlockCipher.of(algo, Cipher.ENCRYPT_MODE, mode, padding, key, iv), data);
                    b = IBlockCipher.doFinal(IBlockCipher.of(algo, Cipher.DECRYPT_MODE, mode, padding, key, iv), a);
                }

                assertArrayEquals(data, b);
            }
        });
    }

    @Test
    void shouldEncryptAndDecryptWithAllPaddings() {
        char[] pwd = "test_secret_password".toCharArray();
        byte[] data = "Payload".getBytes(StandardCharsets.UTF_8);

        final var mode = IBlockCipher.Mode.ECB;

        SecurityUtils.getBlockCiphers().forEach(algo -> {
            var lengths = IBlockCipher.getSupportedKeyLengths(algo);

            lengths.forEach(len -> {
                var key = generatePasswordBasedKey(pwd, len);

                for (var padding : IBlockCipher.Padding.values()) {
                    System.out.printf("%s %d %s %s\n", algo, key.length*8, padding, mode);

                    byte[] a, b;

                    var enc = IBlockCipher.of(algo, Cipher.ENCRYPT_MODE, mode, padding, key, null);
                    var dec = IBlockCipher.of(algo, Cipher.DECRYPT_MODE, mode, padding, key, null);

                    a = IBlockCipher.doFinal(enc, data);
                    b = IBlockCipher.doFinal(dec, a);

                    assertArrayEquals(data, b);
                }
            });
        });
    }

    @Test
    void shouldEncryptAndDecryptFile() throws IOException, ExecutionException, InterruptedException {
        Path input = Paths.get(System.getProperty("user.home"), "Desktop", "a.mp4");
        Path output = Paths.get(System.getProperty("user.home"), "Desktop", "enc");
        Path decrypted = Paths.get(System.getProperty("user.home"), "Desktop", "result.mp4");

        assumeTrue(Files.exists(input), "Target file does not exist");
        Files.write(output, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.write(decrypted, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        var key = generatePasswordBasedKey(new char[] {'c', 'o', 'd', 'e'}, 128);
        var iv = SecurityUtils.generateIV(IBlockCipher.getBlockLength("AES"));
        var enc = IBlockCipher.of("AES", Cipher.ENCRYPT_MODE, IBlockCipher.Mode.CBC, IBlockCipher.Padding.PKCS7, key, iv);
        var dec = IBlockCipher.of("AES", Cipher.DECRYPT_MODE, IBlockCipher.Mode.CBC, IBlockCipher.Padding.PKCS7, key, iv);

        bio.encrypt(enc, input, output);
        bio.decrypt(dec, output, decrypted);

        var h1 = HashImpl.asyncHash("SHA-256", input.toAbsolutePath().toString()).get();
        var h2 = HashImpl.asyncHash("SHA-256", decrypted.toAbsolutePath().toString()).get();
        assertArrayEquals(h1, h2);
    }

    @Test
    void shouldEncryptAndDecryptFileUsingNio() throws IOException, InterruptedException, ExecutionException {
        Path input = Paths.get(System.getProperty("user.home"), "Desktop", "a.mp4");
        Path output = Paths.get(System.getProperty("user.home"), "Desktop", "enc");
        Path decrypted = Paths.get(System.getProperty("user.home"), "Desktop", "result.mp4");

        assumeTrue(Files.exists(input), "Target file does not exist");
        Files.write(output, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.write(decrypted, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        var key = generatePasswordBasedKey(new char[] {'c', 'o', 'd', 'e'}, 128);
        var iv = SecurityUtils.generateIV(IBlockCipher.getBlockLength("AES"));
        var enc = IBlockCipher.of("AES", Cipher.ENCRYPT_MODE, IBlockCipher.Mode.CBC, IBlockCipher.Padding.PKCS7, key, iv);
        var dec = IBlockCipher.of("AES", Cipher.DECRYPT_MODE, IBlockCipher.Mode.CBC, IBlockCipher.Padding.PKCS7, key, iv);

        nio.encrypt(enc, input, output);
        nio.decrypt(dec, output, decrypted);

        var h1 = HashImpl.asyncHash("SHA-256", input.toAbsolutePath().toString()).get();
        var h2 = HashImpl.asyncHash("SHA-256", decrypted.toAbsolutePath().toString()).get();
        assertArrayEquals(h1, h2);

    }

    private byte[] generatePasswordBasedKey(char[] password, int keySize) {
        var salt = Base64.getDecoder().decode("4WHuOVNv8nIwjrPhLpyPwA==");
        return SecurityUtils.generatePasswordBasedKey(password, keySize, salt);
    }

}
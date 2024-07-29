package dev.masterflomaster1.jfxc.crypto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HexFormat;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class BlockCipherImplTest {

    @BeforeAll
    static void beforeAll() {
        SecurityUtils.init();
    }

    @Test
    void shouldGenerateKeysForAllAlgorithms() {
        SecurityUtils.getBlockCiphers().forEach(cipher -> {
            var list = BlockCipherImpl.getAvailableKeyLengths(cipher);

            list.forEach(len -> {
                var key = BlockCipherImpl.generateKey(cipher, len);
                System.out.printf("%s key (%d): %s\n", cipher, key.length*8, HexFormat.of().formatHex(key));
            });
        });
    }

    @Test
    void shouldGeneratePasswordBasedKeys() {
        char[] pwd = "test_secret_password".toCharArray();

        SecurityUtils.getBlockCiphers().forEach(cipher -> {
            var list = BlockCipherImpl.getAvailableKeyLengths(cipher);

            list.forEach(len -> {
                var key = BlockCipherImpl.generatePasswordBasedKey(pwd, len);
                System.out.printf("%s key (%d): %s\n", cipher, key.length*8, HexFormat.of().formatHex(key));
            });
        });
    }

    @Test
    void shouldEncryptAndDecryptWithAllModes() {
        char[] pwd = "test_secret_password".toCharArray();
        byte[] data = "Payload".getBytes(StandardCharsets.UTF_8);

        SecurityUtils.getBlockCiphers().forEach(cipher -> {
            var lengths = BlockCipherImpl.getAvailableKeyLengths(cipher);

            lengths.forEach(len -> {
                var key = BlockCipherImpl.generatePasswordBasedKey(pwd, len);

                for (var mode: BlockCipherImpl.Mode.values()) {
                    System.out.printf("%s key (%d): %s\n", cipher, key.length*8, mode);
                    byte[] a, b;

                    if (mode == BlockCipherImpl.Mode.ECB)  {
                        a = BlockCipherImpl.encrypt(cipher, mode, BlockCipherImpl.Padding.PKCS5Padding, null, data, key);
                        b = BlockCipherImpl.decrypt(cipher, mode, BlockCipherImpl.Padding.PKCS5Padding, null, a, key);
                    } else {
                        var iv = BlockCipherImpl.generateIV(cipher);
                        System.out.printf("IV: %s\n".formatted(HexFormat.of().formatHex(iv)));
                        a = BlockCipherImpl.encrypt(cipher, mode, BlockCipherImpl.Padding.PKCS5Padding, iv, data, key);
                        b = BlockCipherImpl.decrypt(cipher, mode, BlockCipherImpl.Padding.PKCS5Padding, iv, a, key);
                    }

                    assertArrayEquals(data, b);
                }
            });
        });
    }

    @Test
    void shouldEncryptAndDecryptWithAllPaddings() {
        char[] pwd = "test_secret_password".toCharArray();
        byte[] data = "Payload".getBytes(StandardCharsets.UTF_8);

        SecurityUtils.getBlockCiphers().forEach(cipher -> {
            var lengths = BlockCipherImpl.getAvailableKeyLengths(cipher);

            lengths.forEach(len -> {
                var key = BlockCipherImpl.generatePasswordBasedKey(pwd, len);

                for (var padding : BlockCipherImpl.Padding.values()) {
                    var a = BlockCipherImpl.encrypt(cipher, BlockCipherImpl.Mode.ECB, padding, null, data, key);

                    System.out.printf("%s key (%d) %s: %s\n",
                            cipher,
                            key.length*8,
                            padding.getPadding(),
                            HexFormat.of().formatHex(a)
                            );

                    var b = BlockCipherImpl.decrypt(cipher, BlockCipherImpl.Mode.ECB, padding, null, a, key);

                    assertArrayEquals(data, b);
                }
            });
        });
    }

    @Test
    void shouldEncryptAndDecryptFile() throws IOException, ExecutionException, InterruptedException {
        Path input = Paths.get(System.getProperty("user.home"), "Desktop", "a.webm");
        Path output = Paths.get(System.getProperty("user.home"), "Desktop", "enc");
        Path decrypted = Paths.get(System.getProperty("user.home"), "Desktop", "result.webm");

        assumeTrue(Files.exists(input), "Target file does not exist");
        Files.write(output, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.write(decrypted, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        var key = BlockCipherImpl.generatePasswordBasedKey(new char[] {'c', 'o', 'd', 'e'}, 128);

        BlockCipherImpl.encrypt(
                input.toAbsolutePath().toString(),
                output.toAbsolutePath().toString(),
                "AES",
                BlockCipherImpl.Mode.ECB,
                BlockCipherImpl.Padding.PKCS7Padding,
                new byte[] {},
                key
        );

        BlockCipherImpl.decrypt(
                output.toAbsolutePath().toString(),
                decrypted.toAbsolutePath().toString(),
                "AES",
                BlockCipherImpl.Mode.ECB,
                BlockCipherImpl.Padding.PKCS7Padding,
                new byte[] {},
                key
        );

        var h1 = UnkeyedCryptoHash.asyncHash("SHA-256", input.toAbsolutePath().toString()).get();
        var h2 = UnkeyedCryptoHash.asyncHash("SHA-256", decrypted.toAbsolutePath().toString()).get();
        assertArrayEquals(h1, h2);
    }

    @Test
    void shouldEncryptAndDecryptFileUsingNio() throws IOException, InterruptedException, ExecutionException {
        Path input = Paths.get(System.getProperty("user.home"), "Desktop", "a.webm");
        Path output = Paths.get(System.getProperty("user.home"), "Desktop", "enc");
        Path decrypted = Paths.get(System.getProperty("user.home"), "Desktop", "result.webm");

        assumeTrue(Files.exists(input), "Target file does not exist");
        Files.write(output, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.write(decrypted, new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        var key = BlockCipherImpl.generatePasswordBasedKey(new char[] {'c', 'o', 'd', 'e'}, 128);

        BlockCipherImpl.nioEncrypt(
                input.toAbsolutePath().toString(),
                output.toAbsolutePath().toString(),
                "AES",
                BlockCipherImpl.Mode.ECB,
                BlockCipherImpl.Padding.PKCS7Padding,
                new byte[] {},
                key
        );

        BlockCipherImpl.nioDecrypt(
                output.toAbsolutePath().toString(),
                decrypted.toAbsolutePath().toString(),
                "AES",
                BlockCipherImpl.Mode.ECB,
                BlockCipherImpl.Padding.PKCS7Padding,
                new byte[] {},
                key
        );

        var h1 = UnkeyedCryptoHash.asyncHash("SHA-256", input.toAbsolutePath().toString()).get();
        var h2 = UnkeyedCryptoHash.asyncHash("SHA-256", decrypted.toAbsolutePath().toString()).get();
        assertArrayEquals(h1, h2);

    }

}
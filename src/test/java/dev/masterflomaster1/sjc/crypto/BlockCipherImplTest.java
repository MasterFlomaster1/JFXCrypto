package dev.masterflomaster1.sjc.crypto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HexFormat;

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
    void shouldEncryptWithAsyncFunction() {
        Path file1 = Paths.get(System.getProperty("user.home"), "Desktop", "file1");
        Path file2 = Paths.get(System.getProperty("user.home"), "Desktop", "file2");

        assumeTrue(Files.exists(file1), "Target file does not exist");
        assumeTrue(Files.exists(file2), "Destination file does not exist");

        var key = BlockCipherImpl.generatePasswordBasedKey(new char[] {'c', 'o', 'd', 'e'}, 128);

        BlockCipherImpl.encryptFile(
                file1.toAbsolutePath().toString(),
                file2.toAbsolutePath().toString(),
                "AES",
                BlockCipherImpl.Mode.ECB,
                BlockCipherImpl.Padding.PKCS7Padding,
                new byte[] {},
                key
        );
    }

    @Test
    void shouldDecryptWithAsyncFunction() {
        Path file2 = Paths.get(System.getProperty("user.home"), "Desktop", "file2");
        Path file3 = Paths.get(System.getProperty("user.home"), "Desktop", "file3");

        assumeTrue(Files.exists(file2), "Target file does not exist");

        var key = BlockCipherImpl.generatePasswordBasedKey(new char[] {'c', 'o', 'd', 'e'}, 128);

        BlockCipherImpl.decryptFile(
                file2.toAbsolutePath().toString(),
                file3.toAbsolutePath().toString(),
                "AES",
                BlockCipherImpl.Mode.ECB,
                BlockCipherImpl.Padding.PKCS7Padding,
                new byte[] {},
                key
        );
    }

}
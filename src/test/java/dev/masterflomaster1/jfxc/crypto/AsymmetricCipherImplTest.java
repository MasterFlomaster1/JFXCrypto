package dev.masterflomaster1.jfxc.crypto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.*;

class AsymmetricCipherImplTest {

    @BeforeAll
    static void beforeAll() {
        SecurityUtils.init();
    }

    @Test
    void shouldEncryptAndDecryptUsingRSA() {
        var keyPair = AsymmetricCipherImpl.generateKeyPair("RSA");

        System.out.println(HexFormat.of().formatHex(keyPair.getPublic().getEncoded()));
        System.out.println(keyPair.getPublic().getFormat());
        System.out.println(HexFormat.of().formatHex(keyPair.getPrivate().getEncoded()));
        System.out.println(keyPair.getPrivate().getFormat());

        var payload = "Hello World!".getBytes(StandardCharsets.UTF_8);

        var enc = AsymmetricCipherImpl.encrypt("RSA", payload, keyPair.getPublic());
        var dec = AsymmetricCipherImpl.decrypt("RSA", enc, keyPair.getPrivate());

        System.out.println(HexFormat.of().formatHex(enc));
        System.out.println(new String(dec));

        assertArrayEquals(payload, dec);
    }

    @Test
    void shouldEncryptAndDecryptUsingVariousAlgorithms() {
        var payload = "Hello World!".getBytes(StandardCharsets.UTF_8);

        SecurityUtils.getAsymmetricCiphers().forEach(cipher -> {
            var keyPair = AsymmetricCipherImpl.generateKeyPair(AsymmetricCipherImpl.getProperKeyGenAlgorithm(cipher));
            var enc = AsymmetricCipherImpl.encrypt(cipher, payload, keyPair.getPublic());
            var dec = AsymmetricCipherImpl.decrypt(cipher, enc, keyPair.getPrivate());

            assertArrayEquals(payload, dec);
        });
    }

    @Test
    void shouldEncryptAndDecryptUsingVariousAlgorithmsAndKeyOptions() {
        var payload = "Hello World!".getBytes(StandardCharsets.UTF_8);

        SecurityUtils.getAsymmetricCiphers().forEach(cipher -> {

            AsymmetricCipherImpl.getAvailableKeyOptions(cipher).forEach(option -> {
                System.out.println(cipher + " with " + option + " key option");

                var keyPair = AsymmetricCipherImpl.generateKeyPair(AsymmetricCipherImpl.getProperKeyGenAlgorithm(cipher), option);
                var enc = AsymmetricCipherImpl.encrypt(cipher, payload, keyPair.getPublic());
                var dec = AsymmetricCipherImpl.decrypt(cipher, enc, keyPair.getPrivate());

                assertArrayEquals(payload, dec);
            });
        });
    }

    @Test
    void shouldEncryptAndDecryptHybridUsingVariousAlgorithms() {
        var payload = "Hello World!".getBytes(StandardCharsets.UTF_8);

        SecurityUtils.getHybridAsymmetricCiphers().forEach(cipher -> {
            var keyPair = AsymmetricCipherImpl.generateKeyPair(AsymmetricCipherImpl.getProperKeyGenAlgorithm(cipher));

            byte[] derivation = new byte[16];
            byte[] encoding = new byte[16];
            byte[] nonce = new byte[AsymmetricCipherImpl.getProperNonceLength(cipher)];

            SecureRandom random = new SecureRandom();

            random.nextBytes(derivation);
            random.nextBytes(encoding);
            random.nextBytes(nonce);

            var enc = AsymmetricCipherImpl.encrypt(cipher, payload, keyPair.getPublic(), derivation, encoding, nonce);
            var dec = AsymmetricCipherImpl.decrypt(cipher, enc, keyPair.getPrivate(), derivation, encoding, nonce);

            assertArrayEquals(payload, dec);
        });
    }

    @Test
    void shouldEncryptAndDecryptHybridUsingVariousAlgorithmsAndOptions() {
        var payload = "Hello World!".getBytes(StandardCharsets.UTF_8);

        SecurityUtils.getHybridAsymmetricCiphers().forEach(cipher -> {
            AsymmetricCipherImpl.getAvailableKeyOptions(cipher).forEach(option -> {
                System.out.println(cipher + " with " + option + " key option");
                var keyPair = AsymmetricCipherImpl.generateKeyPair(AsymmetricCipherImpl.getProperKeyGenAlgorithm(cipher), option);

                byte[] derivation = new byte[16];
                byte[] encoding = new byte[16];
                byte[] nonce = new byte[AsymmetricCipherImpl.getProperNonceLength(cipher)];

                SecureRandom random = new SecureRandom();

                random.nextBytes(derivation);
                random.nextBytes(encoding);
                random.nextBytes(nonce);

                var enc = AsymmetricCipherImpl.encrypt(cipher, payload, keyPair.getPublic(), derivation, encoding, nonce);
                var dec = AsymmetricCipherImpl.decrypt(cipher, enc, keyPair.getPrivate(), derivation, encoding, nonce);

                assertArrayEquals(payload, dec);
            });
        });
    }

}
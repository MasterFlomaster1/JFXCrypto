package dev.masterflomaster1.jfxc.core;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class IKeyPairCipherTest {

    @BeforeAll
    static void beforeAll() {
        SecurityUtils.init();
    }

    @Test
    void shouldEncryptAndDecryptUsingVariousAlgorithmsAndKeyOptions() {
        var payload = "Hello World!".getBytes(StandardCharsets.UTF_8);

        SecurityUtils.getAsymmetricCiphers().forEach(algo ->
                IKeyPairCipher.getSupportedKeyOptions(algo).forEach(option -> {
                    System.out.println("Testing " + algo + " " + option);

                    var pair = IKeyPairCipher.generateKeyPair(IKeyPairCipher.getSupportedKeyGenAlgorithm(algo), option);
                    var enc = IKeyPairCipher.of(algo, Cipher.ENCRYPT_MODE, pair.getPublic());
                    var dec = IKeyPairCipher.of(algo, Cipher.DECRYPT_MODE, pair.getPrivate());

                    var a = IKeyPairCipher.doFinal(enc, payload);
                    var b = IKeyPairCipher.doFinal(dec, a);

                    assertArrayEquals(payload, b);
                }));
    }

    @Test
    void shouldEncryptAndDecryptHybridUsingVariousAlgorithmsAndOptions() {
        var payload = "Hello World!".getBytes(StandardCharsets.UTF_8);

        SecurityUtils.getHybridAsymmetricCiphers().forEach(algo -> {
            IKeyPairCipher.getSupportedKeyOptions(algo).forEach(option -> {
                System.out.println("Testing " + algo + " " + option);
                var pair = IKeyPairCipher.generateKeyPair(IKeyPairCipher.getSupportedKeyGenAlgorithm(algo), option);

                byte[] derivation = new byte[16];
                byte[] encoding = new byte[16];
                byte[] nonce = new byte[IKeyPairCipher.getSupportedNonceLength(algo)];

                SecureRandom random = new SecureRandom();

                random.nextBytes(derivation);
                random.nextBytes(encoding);
                random.nextBytes(nonce);

                var enc = IKeyPairCipher.of(algo, Cipher.ENCRYPT_MODE, pair.getPublic(), derivation, encoding, nonce);
                var dec = IKeyPairCipher.of(algo, Cipher.DECRYPT_MODE, pair.getPrivate(), derivation, encoding, nonce);

                var a = IKeyPairCipher.doFinal(enc, payload);
                var b = IKeyPairCipher.doFinal(dec, a);

                assertArrayEquals(payload, b);
            });
        });
    }

}
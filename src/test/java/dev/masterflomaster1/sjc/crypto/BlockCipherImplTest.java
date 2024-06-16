package dev.masterflomaster1.sjc.crypto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.*;

class BlockCipherImplTest {

    @BeforeAll
    static void beforeAll() {
        SecurityUtils.init();
    }

    @Test
    void demo() {
        String test = "Test sentence";

        String alg = SecurityUtils.getBlockCiphers().last();

        byte[] key = new byte[16];
        for (int i = 0; i < key.length; i++)
            key[i] = (byte) i;

        var b = BlockCipherImpl.encrypt(alg, test.getBytes(StandardCharsets.UTF_8), key);

        System.out.println(HexFormat.of().formatHex(b));

        var val = BlockCipherImpl.decrypt(alg, b, key);

        System.out.println(new String(val));

    }

    @Test
    void shouldGenerateKeysForAllAlgorithms() {
        SecurityUtils.getBlockCiphers().forEach(cipher -> {
            var set = BlockCipherImpl.getAvailableKeyLengths(cipher);

            set.forEach(len -> {
                var key = BlockCipherImpl.generateKey(cipher, Integer.parseInt(len));
                System.out.printf("%s key (%d): %s\n", cipher, key.length*8, HexFormat.of().formatHex(key));
            });
        });
    }

}
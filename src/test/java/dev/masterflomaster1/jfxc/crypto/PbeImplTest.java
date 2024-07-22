package dev.masterflomaster1.jfxc.crypto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HexFormat;
import java.util.concurrent.ExecutionException;

class PbeImplTest {

    @BeforeAll
    static void beforeAll() {
        SecurityUtils.init();
    }

    @Test
    void shouldGeneratePbkdf2WithAllAlgorithmsDefaultSettings() {
        var iter = 10000;
        var len = 128;
        char[] password = "testPassword".toCharArray();

        SecurityUtils.getPbkdfs().forEach(alg -> {
            var future = PbeImpl.asyncHash(alg, password, SecurityUtils.generateSalt(), iter, len);

            try {
                byte[] b = future.get();

                System.out.println(alg + ": " + HexFormat.of().formatHex(b));
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
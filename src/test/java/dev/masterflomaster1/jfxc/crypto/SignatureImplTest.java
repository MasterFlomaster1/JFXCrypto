package dev.masterflomaster1.jfxc.crypto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SignatureImplTest {

    @BeforeAll
    static void beforeAll() {
        SecurityUtils.init();
    }

    private static final Map<String, KeyPair> generateKeyPairs = new HashMap<>();

    @Test
    @Order(1)
    void shouldGenerateKeysForAllAlgorithms() {
        SecurityUtils.getSignatures().forEach(algo -> {
            try {
                KeyPair keyPair = SignatureImpl.generateKey(algo);
                generateKeyPairs.put(algo, keyPair);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    @Order(2)
    void shouldSignStringWithAllAlgorithms() {
        var data = "Payload".getBytes(StandardCharsets.UTF_8);

        assumeFalse(generateKeyPairs.isEmpty());

        SecurityUtils.getSignatures().forEach(algo -> {
            try {
                KeyPair keyPair = generateKeyPairs.get(algo);

                byte[] out = SignatureImpl.sign(algo, keyPair.getPrivate(), data);

                assertTrue(SignatureImpl.verify(algo, keyPair.getPublic(), out, data));
            } catch (Exception e) {
                System.err.println(e.getClass() + " " + e.getMessage() + " " + algo);
            }
        });
    }

}
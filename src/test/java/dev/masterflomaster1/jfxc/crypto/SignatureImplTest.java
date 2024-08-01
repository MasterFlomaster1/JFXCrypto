package dev.masterflomaster1.jfxc.crypto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HexFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SignatureImplTest {

    @BeforeAll
    static void beforeAll() {
        SecurityUtils.init();
    }

    @Test
    void shouldSignStringWithAllAlgorithms() {
        var data = "Payload".getBytes(StandardCharsets.UTF_8);

        List<String> list = new ArrayList<>();

        SecurityUtils.getSignatures().forEach(algo -> {
//            System.out.println(algo);

            try {
                byte[] out = SignatureImpl.sign(algo, data);
//                System.out.println(HexFormat.of().formatHex(out));
            } catch (Exception e) {
                System.err.println(e.getClass() + " " + e.getMessage());
                list.add(algo);
            }
        });

        System.out.println(list);
        System.out.println(list.size());
    }

}
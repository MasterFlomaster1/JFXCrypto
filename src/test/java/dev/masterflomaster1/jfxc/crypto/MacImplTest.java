package dev.masterflomaster1.jfxc.crypto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class MacImplTest {

    @BeforeAll
    static void beforeAll() {
        SecurityUtils.init();
    }

    @Test
    void shouldMacWithAllAlgorithms() {
        var key = "test_secret_password".getBytes(StandardCharsets.UTF_8);
        var data = "Payload".getBytes(StandardCharsets.UTF_8);

        SecurityUtils.getHmacs().forEach(hmac -> {
            System.out.println(hmac);
            var bytes = MacImpl.hmac(hmac, key, data);

            System.out.println(HexFormat.of().formatHex(bytes));
        });
    }

    @Test
    void shouldMacFileWithAllAlgorithms() {
        Path input = Paths.get(System.getProperty("user.home"), "Desktop", "a.mp4");
        assumeTrue(Files.exists(input), "Target file does not exist");

        var key = "test_secret_password".getBytes(StandardCharsets.UTF_8);
        var data = "Payload".getBytes(StandardCharsets.UTF_8);

        SecurityUtils.getHmacs().forEach(hmac -> {
            System.out.println(hmac);
            var bytes = MacImpl.hmac(hmac, key, data);

            System.out.println(HexFormat.of().formatHex(bytes));
        });
    }

}
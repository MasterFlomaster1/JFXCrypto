package dev.masterflomaster1.jfxc.crypto.passwords;

import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HaveIBeenPwnedApiClientTest {

    @BeforeAll
    static void beforeAll() {
        SecurityUtils.init();
    }

    @Test
    void shouldReturnTrue() throws IOException, InterruptedException {
        var password = "password1234".getBytes(StandardCharsets.UTF_8);

        assertTrue(HaveIBeenPwnedApiClient.passwordRange(password).isPresent());
    }

    @Test
    void shouldReturnEmpty() throws IOException, InterruptedException {
        var password = "0(D)@#)*(JQ#ADdccLC;a{{Wdd3#)_(JFSA".getBytes(StandardCharsets.UTF_8);

        assertTrue(HaveIBeenPwnedApiClient.passwordRange(password).isEmpty());
    }

}
package dev.masterflomaster1.jfxc.crypto;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Demo {

    @Test
    void demo() {

        var a = "demo test";

        System.out.println(a.getBytes(StandardCharsets.UTF_8).length);

        System.out.println(Arrays.toString(a.getBytes(StandardCharsets.UTF_8)));

    }

}

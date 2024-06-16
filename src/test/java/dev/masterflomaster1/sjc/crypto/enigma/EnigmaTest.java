package dev.masterflomaster1.sjc.crypto.enigma;

import dev.masterflomaster1.sjc.utils.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnigmaTest {

    @Test
    void demo() {

        Enigma enigma = new Enigma(new String[] {"VII", "V", "IV"}, "B", new int[] {10,5,12}, new int[] {1,1,1}, "");

        var a = new String(enigma.encrypt("Hello World"));

        System.out.println(StringUtils.spaceAfterN(a, 5));

    }

}
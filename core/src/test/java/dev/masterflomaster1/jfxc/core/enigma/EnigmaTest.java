package dev.masterflomaster1.jfxc.core.enigma;

import dev.masterflomaster1.jfxc.core.utils.StringUtils;
import org.junit.jupiter.api.Test;

class EnigmaTest {

    @Test
    void demo() {

        Enigma enigma = new Enigma(new String[] {"VII", "V", "IV"}, "B", new int[] {10,5,12}, new int[] {1,1,1}, "");

        var a = new String(enigma.encrypt("Hello World"));

        System.out.println(StringUtils.spaceAfterN(a, 5));

    }

}
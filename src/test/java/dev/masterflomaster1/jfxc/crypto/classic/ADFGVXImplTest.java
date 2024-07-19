package dev.masterflomaster1.jfxc.crypto.classic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ADFGVXImplTest {

    private final String text = "Munitionierung beschleunigen Punkt Soweit nicht eingesehen auch bei Tag";
    private final String code =
            "FFGFDFFAGDAFDFGDGGDFAGDDGDFGDAGDGDDFAGDAADGDAFFGFAGDDAFAAAAAADAFFFDVFAVFXFFVGDDFVDFDVDVVVAFDFAADDFFXDDADVDADFVAVFDFDFAADDFDVDA";
    private final String key = "CODE";

    @Test
    @DisplayName("Should encrypt message with key")
    void shouldEncryptADFGVX() {
        var a = ADFGVXImpl.encrypt(text, key);
        String expected = "FFGFDFFAGDAFDFGDGGDFAGDDGDFGDAGDGDDFAGDAADGDAFFGFAGDDAFAAAAAADAFFFDVFAVFXFFVGDDFVDFDVDVVVAFDFAADDFFXDDADVDADFVAVFDFDFAADDFDVDA";

        assertEquals(expected, a);
    }

    @Test
    @DisplayName("Should decrypt message with key")
    void shouldDecryptADFGVX() {
        var a = ADFGVXImpl.decrypt(code, key);
        String expected = text.replace(" ", "").toUpperCase();

        System.out.println(a);

        assertEquals(expected, a);
    }

    @Test
    void shouldEncryptAndDecryptADFGVX1() {
        var a = ADFGVXImpl.encrypt("Test", key);
        var b = ADFGVXImpl.decrypt(a, key);

        assertEquals("TEST", b);
    }

    @Test
    void shouldEncryptAndDecryptADFGVX2() {
        var a = ADFGVXImpl.encrypt("Attack at once", key);
        var b = ADFGVXImpl.decrypt(a, key);

        assertEquals("Attack at once".toUpperCase().replace(" ", ""), b);
    }

}
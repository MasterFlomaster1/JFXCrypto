package dev.masterflomaster1.jfxc;

import dev.masterflomaster1.jfxc.crypto.SecurityUtils;

public final class Start {

    private Start() { }

    public static void main(String[] args) {
        SecurityUtils.init();
        JFXCrypto.launch(JFXCrypto.class, args);
    }

}

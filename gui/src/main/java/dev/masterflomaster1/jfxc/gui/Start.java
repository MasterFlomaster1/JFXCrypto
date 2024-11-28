package dev.masterflomaster1.jfxc.gui;

import dev.masterflomaster1.jfxc.core.SecurityUtils;

public final class Start {

    private Start() { }

    public static void main(String[] args) {
        SecurityUtils.init();
        JFXCrypto.launch(JFXCrypto.class, args);
    }

}

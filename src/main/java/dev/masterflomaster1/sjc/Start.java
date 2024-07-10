package dev.masterflomaster1.sjc;

import dev.masterflomaster1.sjc.crypto.SecurityUtils;

public final class Start {

    private Start() { }

    public static void main(String[] args) {
        SecurityUtils.init();
        SJC.launch(SJC.class, args);
    }

}

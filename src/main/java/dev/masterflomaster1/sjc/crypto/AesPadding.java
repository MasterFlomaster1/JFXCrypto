package dev.masterflomaster1.sjc.crypto;

public enum AesPadding {

    NO_PADDING ("NoPadding"),
    PKCS5_PADDING ("PKCS5Padding");

    public final String name;

    AesPadding(String name) {
        this.name = name;
    }

}

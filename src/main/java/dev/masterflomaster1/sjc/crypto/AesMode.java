package dev.masterflomaster1.sjc.crypto;

public enum AesMode {

    /**
     * Electronic Code Book mode
     */
    ECB ("ECB"),

    /**
     * Cipher Block Chaining mode
     */
    CBC ("CBC"),

    /**
     * Cipher FeedBack mode
     */
    CFB ("CFB"),

    /**
     * Output FeedBack mode
     */
    OFB ("OFB"),

    /**
     * Counter mode
     */
    CTR ("CTR");

    public final String name;

    AesMode(String name) {
        this.name = name;
    }

}

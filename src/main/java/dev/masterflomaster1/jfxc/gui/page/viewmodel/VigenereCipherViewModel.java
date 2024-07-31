package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.classic.VigenereCipherImpl;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class VigenereCipherViewModel extends AbstractViewModel {

    private final StringProperty inputText = new SimpleStringProperty();
    private final StringProperty outputText = new SimpleStringProperty();
    private final StringProperty keyText = new SimpleStringProperty();

    private Timeline emptyKeyAnimation;

    public StringProperty inputTextProperty() {
        return inputText;
    }

    public StringProperty outputTextProperty() {
        return outputText;
    }

    public StringProperty keyTextProperty() {
        return keyText;
    }

    public void setEmptyKeyAnimation(Timeline emptyKeyAnimation) {
        this.emptyKeyAnimation = emptyKeyAnimation;
    }

    public void action(boolean encrypt) {
        if (inputText.get().isEmpty())
            return;

        if (keyText.get().isEmpty()) {
            emptyKeyAnimation.playFromStart();
            return;
        }

        String value;

        if (encrypt) {
            value = VigenereCipherImpl.encrypt(inputText.get(), keyText.get());
            counterText.set("Encoded %d chars".formatted(value.length()));
        } else {
            value = VigenereCipherImpl.decrypt(inputText.get(), keyText.get());
            counterText.set("Decoded %d chars".formatted(value.length()));
        }

        outputText.set(value);
    }

    @Override
    public void onInit() {
        inputText.set(MemCache.readString("vigenere.input", ""));
        outputText.set(MemCache.readString("vigenere.output", ""));
        keyText.set(MemCache.readString("vigenere.key", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("vigenere.input", inputText.get());
        MemCache.writeString("vigenere.output", outputText.get());
        MemCache.writeString("vigenere.key", keyText.get());
    }
}

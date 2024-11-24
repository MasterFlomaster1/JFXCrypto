package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.classic.VigenereCipherImpl;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("SpellCheckingInspection")
public final class VigenereCipherViewModel extends AbstractViewModel {

    @Getter private final StringProperty inputProperty = new SimpleStringProperty();
    @Getter private final StringProperty outputProperty = new SimpleStringProperty();
    @Getter private final StringProperty keyProperty = new SimpleStringProperty();

    @Setter private Timeline emptyKeyAnimation;

    public void action(boolean encrypt) {
        if (inputProperty.get().isEmpty())
            return;

        if (keyProperty.get().isEmpty()) {
            emptyKeyAnimation.playFromStart();
            return;
        }

        String value;

        if (encrypt) {
            value = VigenereCipherImpl.encrypt(inputProperty.get(), keyProperty.get());
            counterText.set("Encoded %d chars".formatted(value.length()));
        } else {
            value = VigenereCipherImpl.decrypt(inputProperty.get(), keyProperty.get());
            counterText.set("Decoded %d chars".formatted(value.length()));
        }

        outputProperty.set(value);
    }

    @Override
    public void onInit() {
        inputProperty.set(MemCache.readString("vigenere.input", ""));
        outputProperty.set(MemCache.readString("vigenere.output", ""));
        keyProperty.set(MemCache.readString("vigenere.key", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("vigenere.input", inputProperty.get());
        MemCache.writeString("vigenere.output", outputProperty.get());
        MemCache.writeString("vigenere.key", keyProperty.get());
    }
}

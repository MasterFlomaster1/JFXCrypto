package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.classic.PlayfairCipherImpl;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

public final class PlayfairCipherViewModel extends AbstractViewModel {

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
            value = PlayfairCipherImpl.encrypt(inputProperty.get(), keyProperty.get());
            counterText.set("Encoded %d chars".formatted(value.length()));
        } else {
            value = PlayfairCipherImpl.decrypt(inputProperty.get(), keyProperty.get());
            counterText.set("Decoded %d chars".formatted(value.length()));
        }

        outputProperty.set(value);
    }

    @Override
    public void onInit() {
        inputProperty.set(MemCache.readString("playfair.input", ""));
        outputProperty.set(MemCache.readString("playfair.output", ""));
        keyProperty.set(MemCache.readString("playfair.key", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("playfair.input", inputProperty.get());
        MemCache.writeString("playfair.output", outputProperty.get());
        MemCache.writeString("playfair.key", keyProperty.get());
    }
}

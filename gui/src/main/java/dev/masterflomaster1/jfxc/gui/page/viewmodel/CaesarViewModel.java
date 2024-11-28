package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.core.classic.CaesarCipherImpl;
import dev.masterflomaster1.jfxc.gui.MemCache;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

@Getter
public final class CaesarViewModel extends AbstractViewModel {

    private final StringProperty inputProperty = new SimpleStringProperty();
    private final StringProperty outputProperty = new SimpleStringProperty();
    private final IntegerProperty shiftProperty = new SimpleIntegerProperty();

    public void action(boolean encrypt) {
        if (inputProperty.get().isEmpty())
            return;

        String value;

        if (encrypt) {
            value = CaesarCipherImpl.encrypt(inputProperty.get(), shiftProperty.get());
            counterText.set("Encoded %d chars".formatted(value.length()));
        } else {
            value = CaesarCipherImpl.decrypt(inputProperty.get(), shiftProperty.get());
            counterText.set("Decoded %d chars".formatted(value.length()));
        }

        outputProperty.set(value);
    }

    @Override
    public void onInit() {
        inputProperty.set(MemCache.readString("caesar.input", ""));
        outputProperty.set(MemCache.readString("caesar.output", ""));
        shiftProperty.set(MemCache.readInteger("caesar.shift", 3));
    }

    @Override
    public void onReset() {
        MemCache.writeString("caesar.input", inputProperty.get());
        MemCache.writeString("caesar.output", outputProperty.get());
        MemCache.writeInteger("caesar.shift", shiftProperty.get());
    }
}

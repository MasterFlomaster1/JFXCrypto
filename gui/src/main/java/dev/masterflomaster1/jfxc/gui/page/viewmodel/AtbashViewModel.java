package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.core.classic.AtbashCipherImpl;
import dev.masterflomaster1.jfxc.gui.MemCache;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

@Getter
@SuppressWarnings("SpellCheckingInspection")
public final class AtbashViewModel extends AbstractViewModel {

    private final StringProperty inputProperty = new SimpleStringProperty();
    private final StringProperty outputProperty = new SimpleStringProperty();

    public void action(boolean encrypt) {
        if (inputProperty.get().isEmpty())
            return;

        String value;

        if (encrypt) {
            value = AtbashCipherImpl.encrypt(inputProperty.get());
            counterText.set("Encoded %d chars".formatted(value.length()));
        } else {
            value = AtbashCipherImpl.decrypt(inputProperty.get());
            counterText.set("Decoded %d chars".formatted(value.length()));
        }

        outputProperty.set(value);
    }

    @Override
    public void onInit() {
        inputProperty.set(MemCache.readString("atbash.input", ""));
        outputProperty.set(MemCache.readString("atbash.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("atbash.input", inputProperty.get());
        MemCache.writeString("atbash.output", outputProperty.get());
    }
}

package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.classic.CaesarCipherImpl;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CaesarViewModel extends AbstractViewModel {

    private final StringProperty inputText = new SimpleStringProperty();
    private final StringProperty outputText = new SimpleStringProperty();
    private final IntegerProperty shiftProperty = new SimpleIntegerProperty();
    private final StringProperty counterText = new SimpleStringProperty();

    public StringProperty inputTextProperty() {
        return inputText;
    }

    public StringProperty outputTextProperty() {
        return outputText;
    }

    public IntegerProperty shiftProperty() {
        return shiftProperty;
    }

    public StringProperty counterTextProperty() {
        return counterText;
    }

    public void action(boolean encrypt) {
        if (inputText.get().isEmpty())
            return;

        String value;

        if (encrypt) {
            value = CaesarCipherImpl.encrypt(inputText.get(), shiftProperty.get());
            counterText.set("Encoded %d chars".formatted(value.length()));
        } else {
            value = CaesarCipherImpl.decrypt(inputText.get(), shiftProperty.get());
            counterText.set("Decoded %d chars".formatted(value.length()));
        }

        outputText.set(value);
    }

    @Override
    public void onInit() {
        inputText.set(MemCache.readString("caesar.input", ""));
        outputText.set(MemCache.readString("caesar.output", ""));
        shiftProperty.set(MemCache.readInteger("caesar.shift", 3));
    }

    @Override
    public void onReset() {
        MemCache.writeString("caesar.input", inputText.get());
        MemCache.writeString("caesar.output", outputText.get());
        MemCache.writeInteger("caesar.shift", shiftProperty.get());
    }
}

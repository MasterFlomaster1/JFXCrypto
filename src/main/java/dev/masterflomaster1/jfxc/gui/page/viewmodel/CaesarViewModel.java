package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.crypto.classic.CaesarCipherImpl;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CaesarViewModel {

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

}

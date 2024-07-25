package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.classic.AtbashCipherImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AtbashViewModel extends AbstractViewModel {

    private final StringProperty inputText = new SimpleStringProperty();
    private final StringProperty outputText = new SimpleStringProperty();
    private final StringProperty counterText = new SimpleStringProperty();

    public StringProperty inputTextProperty() {
        return inputText;
    }

    public StringProperty outputTextProperty() {
        return outputText;
    }

    public StringProperty counterTextProperty() {
        return counterText;
    }

    public void action(boolean encrypt) {
        if (inputText.get().isEmpty())
            return;

        String value;

        if (encrypt) {
            value = AtbashCipherImpl.encrypt(inputText.get());
            counterText.set("Encoded %d chars".formatted(value.length()));
        } else {
            value = AtbashCipherImpl.decrypt(inputText.get());
            counterText.set("Decoded %d chars".formatted(value.length()));
        }

        outputText.set(value);
    }

    @Override
    public void onInit() {
        inputText.set(MemCache.readString("atbash.input", ""));
        outputText.set(MemCache.readString("atbash.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("atbash.input", inputText.get());
        MemCache.writeString("atbash.output", outputText.get());
    }
}

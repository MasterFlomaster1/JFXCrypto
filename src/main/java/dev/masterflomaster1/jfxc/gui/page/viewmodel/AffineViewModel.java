package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.crypto.classic.AffineCipherImpl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AffineViewModel {

    private final StringProperty inputText = new SimpleStringProperty();
    private final StringProperty outputText = new SimpleStringProperty();
    private final ObjectProperty<Integer> slopeProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Integer> interceptProperty = new SimpleObjectProperty<>();
    private final StringProperty counterText = new SimpleStringProperty();

    public StringProperty inputTextProperty() {
        return inputText;
    }

    public StringProperty outputTextProperty() {
        return outputText;
    }

    public ObjectProperty<Integer> slopeProperty() {
        return slopeProperty;
    }

    public ObjectProperty<Integer> interceptProperty() {
        return interceptProperty;
    }

    public StringProperty counterTextProperty() {
        return counterText;
    }

    public void action(boolean encrypt) {
        if (inputText.get().isEmpty())
            return;

        var a = slopeProperty.get();
        var b = interceptProperty.get();

        String value;

        if (encrypt) {
            value = AffineCipherImpl.encrypt(inputText.get(), a, b);
            counterText.set("Encoded %d chars".formatted(value.length()));
        } else {
            value = AffineCipherImpl.decrypt(inputText.get(), a, b);
            counterText.set("Decoded %d chars".formatted(value.length()));
        }

        outputText.set(value);
    }

}

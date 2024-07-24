package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.crypto.classic.PlayfairCipherImpl;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlayfairCipherViewModel {

    private final StringProperty inputText = new SimpleStringProperty();
    private final StringProperty outputText = new SimpleStringProperty();
    private final StringProperty keyText = new SimpleStringProperty();
    private final StringProperty counterText = new SimpleStringProperty();

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

    public StringProperty counterTextProperty() {
        return counterText;
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
            value = PlayfairCipherImpl.encrypt(inputText.get(), keyText.get());
            counterText.set("Encoded %d chars".formatted(value.length()));
        } else {
            value = PlayfairCipherImpl.decrypt(inputText.get(), keyText.get());
            counterText.set("Decoded %d chars".formatted(value.length()));
        }

        outputText.set(value);
    }

}

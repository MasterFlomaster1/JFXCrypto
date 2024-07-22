package dev.masterflomaster1.jfxc.gui.viewmodel;

import dev.masterflomaster1.jfxc.crypto.classic.ADFGVXImpl;
import dev.masterflomaster1.jfxc.utils.StringUtils;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Toggle;

public class AdfgvxViewModel {

    private final StringProperty inputText = new SimpleStringProperty();
    private final StringProperty outputText = new SimpleStringProperty();
    private final StringProperty keyText = new SimpleStringProperty();
    private final StringProperty counterText = new SimpleStringProperty();

    private final BooleanProperty unblockedModeToggleButtonProperty = new SimpleBooleanProperty();
    private final BooleanProperty blocksOf2ModeToggleButtonProperty = new SimpleBooleanProperty();
    private final BooleanProperty blocksOf5ModeToggleButtonProperty = new SimpleBooleanProperty();

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

    public BooleanProperty unblockedModeToggleButtonProperty() {
        return unblockedModeToggleButtonProperty;
    }

    public BooleanProperty blocksOf2ModeToggleButtonProperty() {
        return blocksOf2ModeToggleButtonProperty;
    }

    public BooleanProperty blocksOf5ModeToggleButtonProperty() {
        return blocksOf5ModeToggleButtonProperty;
    }

    public void setEmptyKeyAnimation(Timeline emptyKeyAnimation) {
        this.emptyKeyAnimation = emptyKeyAnimation;
    }

    @SuppressWarnings("unused")
    public void onToggleChanged(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        if (newValue == null) {
            if (oldValue != null)
                oldValue.setSelected(true);
            return;
        }

        if (outputText.get().isEmpty())
            return;

        var val = StringUtils.removeSpaces(outputText.get());

        if (unblockedModeToggleButtonProperty.get()) {
            outputText.set(val);
        } else if (blocksOf2ModeToggleButtonProperty.get()) {
            outputText.set(StringUtils.spaceAfterN(val, 2));
        } else if (blocksOf5ModeToggleButtonProperty.get()) {
            outputText.set(StringUtils.spaceAfterN(val, 5));
        }
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
            value = ADFGVXImpl.encrypt(inputText.get(), keyText.get());
            counterText.set("Encoded %d chars".formatted(value.length()));
        } else {
            value = ADFGVXImpl.decrypt(inputText.get(), keyText.get());
            counterText.set("Decoded %d chars".formatted(value.length()));
        }

        if (blocksOf2ModeToggleButtonProperty.get())
            value = StringUtils.spaceAfterN(value, 2);
        else if (blocksOf5ModeToggleButtonProperty.get())
            value = StringUtils.spaceAfterN(value, 5);

        outputText.set(value);
    }

}

package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;

import java.util.Base64;
import java.util.HexFormat;

/**
 * Abstract base class for view models that handle byte data formatting and parsing.
 */
abstract class AbstractByteFormattingViewModel extends AbstractViewModel {

    final StringProperty outputText = new SimpleStringProperty();
    final BooleanProperty hexModeToggleButtonProperty = new SimpleBooleanProperty();
    final BooleanProperty b64ModeToggleButtonProperty = new SimpleBooleanProperty();

    public StringProperty outputTextProperty() {
        return outputText;
    }

    public BooleanProperty hexModeToggleButtonProperty() {
        return hexModeToggleButtonProperty;
    }

    public BooleanProperty b64ModeToggleButtonProperty() {
        return b64ModeToggleButtonProperty;
    }

    public void onToggleChanged(@SuppressWarnings("unused") ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        if (newValue == null) {
            if (oldValue != null)
                oldValue.setSelected(true);
            return;
        }

        var selectedButton = (ToggleButton) newValue;

        // bypass unpredictable behavior of ToggleButtonProperty.get(). Temporary solution
        if (selectedButton.getText().equalsIgnoreCase("Hex")) {
            hexModeToggleButtonProperty.set(true);
            b64ModeToggleButtonProperty.set(false);
        } else if (selectedButton.getText().equalsIgnoreCase("Base64")) {
            b64ModeToggleButtonProperty.set(true);
            hexModeToggleButtonProperty.set(false);
        }
    }

    String formatOutput(byte[] value) {
        if (hexModeToggleButtonProperty.get()) {
            return HexFormat.of().formatHex(value);
        } else if (b64ModeToggleButtonProperty.get()) {
            return Base64.getEncoder().encodeToString(value);
        }

        return "";
    }

}

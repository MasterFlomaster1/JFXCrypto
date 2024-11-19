package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import lombok.Getter;

import java.util.Base64;
import java.util.HexFormat;

/**
 * Abstract base class for view models that handle byte data formatting and parsing.
 */
@Getter
abstract class ByteFormattingViewModel extends AbstractViewModel {

    final StringProperty outputProperty = new SimpleStringProperty();
    final BooleanProperty hexModeProperty = new SimpleBooleanProperty();
    final BooleanProperty b64ModeProperty = new SimpleBooleanProperty();

    public void onToggleChanged(@SuppressWarnings("unused") ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        if (newValue == null) {
            if (oldValue != null)
                oldValue.setSelected(true);
            return;
        }

        var selectedButton = (ToggleButton) newValue;

        // bypass unpredictable behavior of ToggleButtonProperty.get(). Temporary solution
        if (selectedButton.getText().equalsIgnoreCase("Hex")) {
            hexModeProperty.set(true);
            b64ModeProperty.set(false);
        } else if (selectedButton.getText().equalsIgnoreCase("Base64")) {
            b64ModeProperty.set(true);
            hexModeProperty.set(false);
        }
    }

    String formatOutput(byte[] value) {
        if (hexModeProperty.get()) {
            return HexFormat.of().formatHex(value);
        } else if (b64ModeProperty.get()) {
            return Base64.getEncoder().encodeToString(value);
        }

        return "";
    }

}

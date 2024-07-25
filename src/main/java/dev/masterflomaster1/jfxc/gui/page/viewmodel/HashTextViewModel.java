package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.crypto.UnkeyedCryptoHash;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;

public class HashTextViewModel extends AbstractViewModel {

    private final StringProperty inputText = new SimpleStringProperty();

    private final Map<String, StringProperty> hashOutputMap = new HashMap<>();

    private final BooleanProperty hexModeToggleButtonProperty = new SimpleBooleanProperty();
    private final BooleanProperty b64ModeToggleButtonProperty = new SimpleBooleanProperty();

    public HashTextViewModel() {
        inputText.addListener((observable, oldValue, newValue) -> {
            action();
        });
    }

    public StringProperty inputTextProperty() {
        return inputText;
    }

    public Map<String, StringProperty> getHashOutputMap() {
        return hashOutputMap;
    }

    public BooleanProperty hexModeToggleButtonPropertyProperty() {
        return hexModeToggleButtonProperty;
    }

    public BooleanProperty b64ModeToggleButtonPropertyProperty() {
        return b64ModeToggleButtonProperty;
    }

    @SuppressWarnings("unused")
    public void onToggleChanged(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        if (newValue == null) {
            if (oldValue != null)
                oldValue.setSelected(true);
            return;
        }

        var selectedButton = (ToggleButton) newValue;

        // bypass unpredictable behavior of ToggleButtonProperty.get()
        if (selectedButton.getText().equalsIgnoreCase("Hex")) {
            hexModeToggleButtonProperty.set(true);
            b64ModeToggleButtonProperty.set(false);
        } else if (selectedButton.getText().equalsIgnoreCase("Base64")) {
            b64ModeToggleButtonProperty.set(true);
            hexModeToggleButtonProperty.set(false);
        }
    }

    public void action() {
        if (inputText.get().isEmpty())
            return;

        byte[] value = inputText.get().getBytes(StandardCharsets.UTF_8);

        hashOutputMap.forEach((k, v) -> {
            try {
                v.set(formatOutput(UnkeyedCryptoHash.hash(k, value)));
            } catch (Exception e) { }
        });
    }

    private String formatOutput(byte[] value) {
        if (hexModeToggleButtonProperty.get()) {
            return HexFormat.of().formatHex(value);
        } else if (b64ModeToggleButtonProperty.get()) {
            return Base64.getEncoder().encodeToString(value);
        }

        return "";
    }

    @Override
    public void onInit() { }

    @Override
    public void onReset() { }
}

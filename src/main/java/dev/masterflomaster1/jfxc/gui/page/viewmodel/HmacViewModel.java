package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.MacImpl;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;

public class HmacViewModel extends AbstractViewModel {

    private final StringProperty inputText = new SimpleStringProperty();
    private final StringProperty outputText = new SimpleStringProperty();
    private final StringProperty keyText = new SimpleStringProperty();
    private final StringProperty counterText = new SimpleStringProperty();

    private final ObjectProperty<String> hmacComboBoxProperty = new SimpleObjectProperty<>();
    private final ObservableList<String> hmacAlgorithmsList = FXCollections.observableArrayList();

    private final BooleanProperty hexModeToggleButtonProperty = new SimpleBooleanProperty();
    private final BooleanProperty b64ModeToggleButtonProperty = new SimpleBooleanProperty();

    private Timeline emptyKeyAnimation;

    public HmacViewModel() {
        hmacAlgorithmsList.setAll(SecurityUtils.getHmacs());
    }

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

    public ObservableList<String> getHmacAlgorithmsList() {
        return hmacAlgorithmsList;
    }

    public ObjectProperty<String> hmacComboBoxProperty() {
        return hmacComboBoxProperty;
    }

    public BooleanProperty hexModeToggleButtonPropertyProperty() {
        return hexModeToggleButtonProperty;
    }

    public BooleanProperty b64ModeToggleButtonPropertyProperty() {
        return b64ModeToggleButtonProperty;
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

        if (keyText.get().isEmpty()) {
            emptyKeyAnimation.playFromStart();
            return;
        }

        var value = MacImpl.hmac(hmacComboBoxProperty.get(),
                keyText.get().getBytes(StandardCharsets.UTF_8),
                inputText.get().getBytes(StandardCharsets.UTF_8));

        counterText.set("Encoded %d bytes".formatted(value.length));

        outputText.set(formatOutput(value));
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
    public void onInit() {
        inputText.set(MemCache.readString("hmac.input", ""));
        outputText.set(MemCache.readString("hmac.output", ""));
        keyText.set(MemCache.readString("hmac.key", ""));
        hmacComboBoxProperty.set(hmacAlgorithmsList.get(MemCache.readInteger("hmac.algo", 0)));
    }

    @Override
    public void onReset() {
        MemCache.writeString("hmac.input", inputText.get());
        MemCache.writeString("hmac.output", outputText.get());
        MemCache.writeString("hmac.key", keyText.get());
        MemCache.writeInteger("hmac.algo", hmacAlgorithmsList.indexOf(hmacComboBoxProperty.get()));
    }
}

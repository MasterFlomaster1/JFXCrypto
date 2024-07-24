package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import dev.masterflomaster1.jfxc.crypto.UnkeyedCryptoHash;
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

import java.io.File;
import java.util.Base64;
import java.util.HexFormat;

public class HashFilesViewModel {

    private final StringProperty outputText = new SimpleStringProperty();
    private final ObjectProperty<String> hashComboBoxProperty = new SimpleObjectProperty<>();
    private final ObservableList<String> hashAlgorithmsList = FXCollections.observableArrayList();

    private final BooleanProperty hexModeToggleButtonProperty = new SimpleBooleanProperty();
    private final BooleanProperty b64ModeToggleButtonProperty = new SimpleBooleanProperty();

    private File selectedFile;

    public HashFilesViewModel() {
        hashAlgorithmsList.setAll(SecurityUtils.getDigests());
    }

    public StringProperty outputTextProperty() {
        return outputText;
    }

    public ObjectProperty<String> hashComboBoxPropertyProperty() {
        return hashComboBoxProperty;
    }

    public ObservableList<String> hashAlgorithmsList() {
        return hashAlgorithmsList;
    }

    public BooleanProperty hexModeToggleButtonPropertyProperty() {
        return hexModeToggleButtonProperty;
    }

    public BooleanProperty b64ModeToggleButtonPropertyProperty() {
        return b64ModeToggleButtonProperty;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
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
        if (selectedFile == null)
            return;

        var completableFuture = UnkeyedCryptoHash.asyncHash(hashComboBoxProperty.get(), selectedFile.getAbsolutePath());
        completableFuture
                .thenAccept(hash -> outputText.set(formatOutput(hash)))
                .exceptionally(ex -> {
                    System.out.println(ex.getMessage());
                    return null;
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

}

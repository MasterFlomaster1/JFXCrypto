package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.MacImpl;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.nio.charset.StandardCharsets;

public final class HmacFilesViewModel extends AbstractByteFormattingViewModel {

    private final StringProperty outputText = new SimpleStringProperty();
    private final StringProperty keyText = new SimpleStringProperty();

    private final ObjectProperty<String> hmacComboBoxProperty = new SimpleObjectProperty<>();
    private final ObservableList<String> hmacAlgorithmsList = FXCollections.observableArrayList();

    private Timeline emptyKeyAnimation;

    private File selectedFile;

    public HmacFilesViewModel() {
        hmacAlgorithmsList.setAll(SecurityUtils.getHmacs());
    }

    public StringProperty outputTextProperty() {
        return outputText;
    }

    public StringProperty keyTextProperty() {
        return keyText;
    }

    public ObservableList<String> getHmacAlgorithmsList() {
        return hmacAlgorithmsList;
    }

    public ObjectProperty<String> hmacComboBoxProperty() {
        return hmacComboBoxProperty;
    }

    public void setEmptyKeyAnimation(Timeline emptyKeyAnimation) {
        this.emptyKeyAnimation = emptyKeyAnimation;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    public void action() {
        if (selectedFile == null)
            return;

        if (keyText.get().isEmpty()) {
            emptyKeyAnimation.playFromStart();
            return;
        }

        var value = MacImpl.hmac(
                selectedFile.toPath(),
                hmacComboBoxProperty.get(),
                keyText.get().getBytes(StandardCharsets.UTF_8)
        );

        counterText.set("Encoded %d bytes".formatted(value.length));

        outputText.set(formatOutput(value));
    }

    @Override
    public void onInit() {
        outputText.set(MemCache.readString("hmac.files.output", ""));
        keyText.set(MemCache.readString("hmac.files.key", ""));
        hmacComboBoxProperty.set(hmacAlgorithmsList.get(MemCache.readInteger("hmac.files.algo", 0)));
    }

    @Override
    public void onReset() {
        MemCache.writeString("hmac.files.output", outputText.get());
        MemCache.writeString("hmac.files.key", keyText.get());
        MemCache.writeInteger("hmac.files.algo", hmacAlgorithmsList.indexOf(hmacComboBoxProperty.get()));
    }
}

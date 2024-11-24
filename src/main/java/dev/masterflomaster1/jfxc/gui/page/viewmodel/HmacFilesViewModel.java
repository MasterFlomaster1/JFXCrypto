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
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.nio.charset.StandardCharsets;

public final class HmacFilesViewModel extends ByteFormattingViewModel {

    @Getter private final StringProperty keyProperty = new SimpleStringProperty();
    @Getter private final ObjectProperty<String> hmacComboBoxProperty = new SimpleObjectProperty<>();
    @Getter private final ObservableList<String> hmacAlgorithmsList = FXCollections.observableArrayList();

    @Setter private Timeline emptyKeyAnimation;
    @Setter private File selectedFile;

    public HmacFilesViewModel() {
        hmacAlgorithmsList.setAll(SecurityUtils.getHmacs());
    }

    public void action() {
        if (selectedFile == null)
            return;

        if (keyProperty.get().isEmpty()) {
            emptyKeyAnimation.playFromStart();
            return;
        }

        var value = MacImpl.hmac(
                selectedFile.toPath(),
                hmacComboBoxProperty.get(),
                keyProperty.get().getBytes(StandardCharsets.UTF_8)
        );

        counterText.set("Encoded %d bytes".formatted(value.length));

        outputProperty.set(formatOutput(value));
    }

    @Override
    public void onInit() {
        outputProperty.set(MemCache.readString("hmac.files.output", ""));
        keyProperty.set(MemCache.readString("hmac.files.key", ""));
        hmacComboBoxProperty.set(hmacAlgorithmsList.get(MemCache.readInteger("hmac.files.algo", 0)));
    }

    @Override
    public void onReset() {
        MemCache.writeString("hmac.files.output", outputProperty.get());
        MemCache.writeString("hmac.files.key", keyProperty.get());
        MemCache.writeInteger("hmac.files.algo", hmacAlgorithmsList.indexOf(hmacComboBoxProperty.get()));
    }
}

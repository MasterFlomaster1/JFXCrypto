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

import java.nio.charset.StandardCharsets;

public final class HmacViewModel extends ByteFormattingViewModel {

    @Getter private final StringProperty inputProperty = new SimpleStringProperty();
    @Getter private final StringProperty keyProperty = new SimpleStringProperty();
    @Getter private final ObjectProperty<String> hmacComboBoxProperty = new SimpleObjectProperty<>();
    @Getter private final ObservableList<String> hmacAlgorithmsList = FXCollections.observableArrayList();

    @Setter private Timeline emptyKeyAnimation;

    public HmacViewModel() {
        hmacAlgorithmsList.setAll(SecurityUtils.getHmacs());
    }

    public void action() {
        if (inputProperty.get().isEmpty())
            return;

        if (keyProperty.get().isEmpty()) {
            emptyKeyAnimation.playFromStart();
            return;
        }

        var value = MacImpl.hmac(hmacComboBoxProperty.get(),
                keyProperty.get().getBytes(StandardCharsets.UTF_8),
                inputProperty.get().getBytes(StandardCharsets.UTF_8));

        counterText.set("Encoded %d bytes".formatted(value.length));

        outputProperty.set(formatOutput(value));
    }

    @Override
    public void onInit() {
        inputProperty.set(MemCache.readString("hmac.input", ""));
        outputProperty.set(MemCache.readString("hmac.output", ""));
        keyProperty.set(MemCache.readString("hmac.key", ""));
        hmacComboBoxProperty.set(hmacAlgorithmsList.get(MemCache.readInteger("hmac.algo", 0)));
    }

    @Override
    public void onReset() {
        MemCache.writeString("hmac.input", inputProperty.get());
        MemCache.writeString("hmac.output", outputProperty.get());
        MemCache.writeString("hmac.key", keyProperty.get());
        MemCache.writeInteger("hmac.algo", hmacAlgorithmsList.indexOf(hmacComboBoxProperty.get()));
    }
}

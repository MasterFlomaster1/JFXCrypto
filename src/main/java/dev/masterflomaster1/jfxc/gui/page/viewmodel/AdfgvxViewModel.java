package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.classic.ADFGVXImpl;
import dev.masterflomaster1.jfxc.utils.StringUtils;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Toggle;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("SpellCheckingInspection")
public final class AdfgvxViewModel extends AbstractViewModel {

    @Getter private final StringProperty inputProperty = new SimpleStringProperty();
    @Getter private final StringProperty outputProperty = new SimpleStringProperty();
    @Getter private final StringProperty keyProperty = new SimpleStringProperty();

    @Getter private final BooleanProperty unblockedModeToggleButtonProperty = new SimpleBooleanProperty();
    @Getter private final BooleanProperty blocksOf2ModeToggleButtonProperty = new SimpleBooleanProperty();
    @Getter private final BooleanProperty blocksOf5ModeToggleButtonProperty = new SimpleBooleanProperty();

    @Setter private Timeline emptyKeyAnimation;

    @SuppressWarnings("unused")
    public void onToggleChanged(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        if (newValue == null) {
            if (oldValue != null)
                oldValue.setSelected(true);
            return;
        }

        if (outputProperty.get().isEmpty())
            return;

        var val = StringUtils.removeSpaces(outputProperty.get());

        if (unblockedModeToggleButtonProperty.get()) {
            outputProperty.set(val);
        } else if (blocksOf2ModeToggleButtonProperty.get()) {
            outputProperty.set(StringUtils.spaceAfterN(val, 2));
        } else if (blocksOf5ModeToggleButtonProperty.get()) {
            outputProperty.set(StringUtils.spaceAfterN(val, 5));
        }
    }

    public void action(boolean encrypt) {
        if (inputProperty.get().isEmpty())
            return;

        if (keyProperty.get().isEmpty()) {
            emptyKeyAnimation.playFromStart();
            return;
        }

        String value;

        if (encrypt) {
            value = ADFGVXImpl.encrypt(inputProperty.get(), keyProperty.get());
            counterText.set("Encoded %d chars".formatted(value.length()));
        } else {
            value = ADFGVXImpl.decrypt(inputProperty.get(), keyProperty.get());
            counterText.set("Decoded %d chars".formatted(value.length()));
        }

        if (blocksOf2ModeToggleButtonProperty.get())
            value = StringUtils.spaceAfterN(value, 2);
        else if (blocksOf5ModeToggleButtonProperty.get())
            value = StringUtils.spaceAfterN(value, 5);

        outputProperty.set(value);
    }

    @Override
    public void onInit() {
        inputProperty.set(MemCache.readString("adfgvx.input", ""));
        outputProperty.set(MemCache.readString("adfgvx.output", ""));
        keyProperty.set(MemCache.readString("adfgvx.key", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("adfgvx.input", inputProperty.get());
        MemCache.writeString("adfgvx.output", outputProperty.get());
        MemCache.writeString("adfgvx.key", keyProperty.get());
    }
}

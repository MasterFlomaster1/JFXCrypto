package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.crypto.HashImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class HashTextViewModel extends ByteFormattingViewModel {

    private final StringProperty inputProperty = new SimpleStringProperty();

    private final Map<String, StringProperty> hashOutputMap = new HashMap<>();

    public HashTextViewModel() {
        inputProperty.addListener((observable, oldValue, newValue) -> {
            action();
        });
    }

    public void action() {
        if (inputProperty.get().isEmpty())
            return;

        byte[] value = inputProperty.get().getBytes(StandardCharsets.UTF_8);

        hashOutputMap.forEach((k, v) -> {
            try {
                v.set(formatOutput(HashImpl.hash(k, value)));
            } catch (Exception e) { }
        });
    }

    @Override
    public void onInit() { }

    @Override
    public void onReset() { }
}

package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.crypto.HashImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class HashTextViewModel extends AbstractByteFormattingViewModel {

    private final StringProperty inputText = new SimpleStringProperty();

    private final Map<String, StringProperty> hashOutputMap = new HashMap<>();

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

    public void action() {
        if (inputText.get().isEmpty())
            return;

        byte[] value = inputText.get().getBytes(StandardCharsets.UTF_8);

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

package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;

public class InputOutputAreaComponentViewModel {

    @Getter final StringProperty inputProperty = new SimpleStringProperty();
    @Getter final StringProperty outputProperty = new SimpleStringProperty();
    @Getter final StringProperty inputLengthProperty = new SimpleStringProperty();
    @Getter final StringProperty outputLengthProperty = new SimpleStringProperty();
    @Getter final ObjectProperty<String> inputDisplayModeProperty = new SimpleObjectProperty<>();
    @Getter final ObjectProperty<String> outputDisplayModeProperty = new SimpleObjectProperty<>();

    private byte[] outputBuffer = new byte[0];

    public void onOutputDisplayModeChanged(ActionEvent event) {
        if (outputBuffer.length == 0)
            return;

        setOutput(outputBuffer);
    }

    byte[] getOutput() {
        var bytes = outputProperty.get().getBytes(StandardCharsets.UTF_8);

        return switch (outputDisplayModeProperty.get()) {
            case "String" -> bytes;
            case "Hex" -> HexFormat.of().parseHex(outputProperty.get());
            case "Base64" -> Base64.getEncoder().encode(bytes);
            default -> throw new IllegalStateException("Unexpected value: " + outputDisplayModeProperty.get());
        };
    }

    void setOutput(byte[] value) {
        outputBuffer = value;
        outputLengthProperty.set("bit length: %d".formatted(value.length));

        switch (outputDisplayModeProperty.get()) {
            case "String" -> outputProperty.set(new String(value, StandardCharsets.UTF_8));
            case "Hex" -> outputProperty.set(HexFormat.of().formatHex(value));
            case "Base64" -> outputProperty.set(Base64.getEncoder().encodeToString(value));
        }
    }


}

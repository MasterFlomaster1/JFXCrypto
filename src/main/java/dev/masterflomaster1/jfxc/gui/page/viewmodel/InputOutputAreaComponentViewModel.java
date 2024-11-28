package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;

@Slf4j
public class InputOutputAreaComponentViewModel {

    @Getter final StringProperty inputProperty = new SimpleStringProperty();
    @Getter final StringProperty outputProperty = new SimpleStringProperty();
    @Getter final StringProperty inputLengthProperty = new SimpleStringProperty();
    @Getter final StringProperty outputLengthProperty = new SimpleStringProperty();
    @Getter final ObjectProperty<String> inputDisplayModeProperty = new SimpleObjectProperty<>();
    @Getter final ObjectProperty<String> outputDisplayModeProperty = new SimpleObjectProperty<>();

    private byte[] inputBuffer = new byte[0];
    private byte[] outputBuffer = new byte[0];

    public InputOutputAreaComponentViewModel() {
        inputProperty.addListener((observable, oldValue, newValue) -> {
            var bytes = switch (inputDisplayModeProperty.get()) {
                case "String" -> newValue.getBytes(StandardCharsets.UTF_8);
                case "Hex" -> HexFormat.of().parseHex(newValue);
                case "Base64" -> Base64.getDecoder().decode(newValue.getBytes(StandardCharsets.UTF_8));
                default -> throw new IllegalStateException("Unexpected value: " + inputDisplayModeProperty.get());
            };

            inputLengthProperty.set("bit length: %d".formatted(bytes.length));
        });

        outputProperty.addListener((observable, oldValue, newValue) -> {
            var bytes = switch (outputDisplayModeProperty.get()) {
                case "String" -> newValue.getBytes(StandardCharsets.UTF_8);
                case "Hex" -> HexFormat.of().parseHex(newValue);
                case "Base64" -> Base64.getDecoder().decode(newValue.getBytes(StandardCharsets.UTF_8));
                default -> throw new IllegalStateException("Unexpected value: " + outputDisplayModeProperty.get());
            };

            log.info("output: {}", bytes.length);

//            outputBuffer = bytes;
            outputLengthProperty.set("bit length: %d".formatted(bytes.length));
        });

    }

    public void onInputDisplayModeChanged(ActionEvent event) {
        if (inputBuffer.length == 0)
            return;

        setInput(inputBuffer);
    }

    public void onOutputDisplayModeChanged(ActionEvent event) {
        if (outputBuffer.length == 0)
            return;

        setOutput(outputBuffer);
    }

    byte[] getInput() {
        var bytes = inputProperty.get().getBytes(StandardCharsets.UTF_8);

        return switch (inputDisplayModeProperty.get()) {
            case "String" -> bytes;
            case "Hex" -> HexFormat.of().parseHex(inputProperty.get());
            case "Base64" -> Base64.getDecoder().decode(bytes);
            default -> throw new IllegalStateException("Unexpected value: " + inputDisplayModeProperty.get());
        };
    }

    void setInput(byte[] value) {
        inputBuffer = value;
//        inputLengthProperty.set("bit length: %d".formatted(value.length));

        switch (inputDisplayModeProperty.get()) {
            case "String" -> inputProperty.set(new String(value, StandardCharsets.UTF_8));
            case "Hex" -> inputProperty.set(HexFormat.of().formatHex(value));
            case "Base64" -> inputProperty.set(Base64.getEncoder().encodeToString(value));
        }
    }

    byte[] getOutput() {
        return switch (outputDisplayModeProperty.get()) {
            case "String" -> outputBuffer;
            case "Hex" -> HexFormat.of().parseHex(outputProperty.get());
            case "Base64" -> Base64.getDecoder().decode(outputProperty.get());
            default -> throw new IllegalStateException("Unexpected value: " + outputDisplayModeProperty.get());
        };
    }

    void setOutput(byte[] value) {
        outputBuffer = value;

        log.info("setOutput: {}", value.length);
        var a = new String(value);
        System.out.println(a.getBytes().length);

        switch (outputDisplayModeProperty.get()) {
            case "String" -> outputProperty.set(new String(value, StandardCharsets.UTF_8));
            case "Hex" -> outputProperty.set(HexFormat.of().formatHex(value));
            case "Base64" -> outputProperty.set(Base64.getEncoder().encodeToString(value));
        }
    }


}

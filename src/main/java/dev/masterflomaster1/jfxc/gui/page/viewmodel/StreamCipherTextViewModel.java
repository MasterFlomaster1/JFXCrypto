package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import dev.masterflomaster1.jfxc.crypto.StreamCipherImpl;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

public final class StreamCipherTextViewModel extends AbstractByteFormattingViewModel {

    private final StringProperty inputText = new SimpleStringProperty();
    private final StringProperty outputText = new SimpleStringProperty();
    private final StringProperty keyText = new SimpleStringProperty();
    private final StringProperty ivText = new SimpleStringProperty();
    private final ObjectProperty<String> streamCipherComboBoxProperty = new SimpleObjectProperty<>();
    private final ObservableList<String> streamCipherAlgorithmsList = FXCollections.observableArrayList();
    private final ObjectProperty<Integer> keyLengthComboBoxProperty = new SimpleObjectProperty<>();
    private final ObservableList<Integer> keyLengthList = FXCollections.observableArrayList();

    private Timeline emptyIvAnimation;
    private Timeline emptyKeyAnimation;

    public StreamCipherTextViewModel() {
        streamCipherAlgorithmsList.setAll(SecurityUtils.getStreamCiphers());
        streamCipherComboBoxProperty.set(streamCipherAlgorithmsList.get(0));
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

    public StringProperty ivTextProperty() {
        return ivText;
    }

    public ObjectProperty<String> streamCipherComboBoxProperty() {
        return streamCipherComboBoxProperty;
    }

    public ObservableList<String> getStreamCipherAlgorithmsList() {
        return streamCipherAlgorithmsList;
    }

    public ObjectProperty<Integer> keyLengthComboBoxProperty() {
        return keyLengthComboBoxProperty;
    }

    public ObservableList<Integer> getKeyLengthList() {
        return keyLengthList;
    }

    public void setEmptyIvAnimation(Timeline emptyIvAnimation) {
        this.emptyIvAnimation = emptyIvAnimation;
    }

    public void setEmptyKeyAnimation(Timeline emptyKeyAnimation) {
        this.emptyKeyAnimation = emptyKeyAnimation;
    }

    @SuppressWarnings("unused")
    public void onAlgorithmSelection(ActionEvent e) {
        var algo = streamCipherComboBoxProperty.get();

        keyLengthList.setAll(StreamCipherImpl.getCorrespondingKeyLengths(algo));
        keyLengthComboBoxProperty.set(keyLengthList.get(0)); // Select first element
    }

    @SuppressWarnings("unused")
    public void onIvShuffleAction(ActionEvent e) {
        var ivKeyLenOptional = StreamCipherImpl.getCorrespondingIvLengthBits(streamCipherComboBoxProperty.get());

        if (ivKeyLenOptional.isEmpty())
            return;

        var value = SecurityUtils.generateIV(ivKeyLenOptional.get().get(0));

        ivText.set(HexFormat.of().formatHex(value));
    }

    public boolean isNonIvAlgorithmSelected() {
        return StreamCipherImpl.getCorrespondingIvLengthBits(streamCipherComboBoxProperty.get()).isEmpty();
    }

    public void action(boolean encrypt) {
        if (inputText.get().isEmpty())
            return;

        var algo = streamCipherComboBoxProperty.get();

        if (!isNonIvAlgorithmSelected() && ivText.get().isEmpty()) {
            emptyIvAnimation.playFromStart();
            return;
        }

        if (keyText.get().isEmpty()) {
            emptyKeyAnimation.playFromStart();
            return;
        }

        var text = inputText.get().getBytes(StandardCharsets.UTF_8);
        byte[] key = HexFormat.of().parseHex(keyText.get());
        byte[] value;
        byte[] iv = null;

        if (StreamCipherImpl.getCorrespondingIvLengthBits(algo).isPresent())
            iv = HexFormat.of().parseHex(ivText.get());

        if (encrypt) {
            value = StreamCipherImpl.encrypt(algo, iv, text, key);
            counterText.set("Encoded %d bytes".formatted(value.length));
            outputText.set(formatOutput(value));
        } else {
            var input = HexFormat.of().parseHex(inputText.get());

            value = StreamCipherImpl.decrypt(algo, iv, input, key);
            counterText.set("Decoded %d bytes".formatted(value.length));
            outputText.set(new String(value));
        }

    }

    @Override
    public void onInit() {
        inputText.set(MemCache.readString("stream.input", ""));
        keyText.set(MemCache.readString("stream.key", ""));
        ivText.set(MemCache.readString("stream.iv", ""));
        streamCipherComboBoxProperty.set(streamCipherAlgorithmsList.get(MemCache.readInteger("stream.algo", 0)));
        keyLengthComboBoxProperty.set(keyLengthList.get(MemCache.readInteger("stream.key.length", 0)));
        outputText.set(MemCache.readString("stream.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("stream.input", inputText.get());
        MemCache.writeString("stream.key", keyText.get());
        MemCache.writeString("stream.iv", ivText.get());
        MemCache.writeInteger("stream.algo", streamCipherAlgorithmsList.indexOf(streamCipherComboBoxProperty.get()));
        MemCache.writeInteger("stream.key.length", keyLengthList.indexOf(keyLengthComboBoxProperty.get()));
        MemCache.writeString("stream.output", outputText.get());
    }
}

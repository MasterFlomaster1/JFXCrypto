package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.core.IStreamCipher;
import dev.masterflomaster1.jfxc.core.SecurityUtils;
import dev.masterflomaster1.jfxc.gui.MemCache;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import lombok.Getter;
import lombok.Setter;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

public final class StreamCipherTextViewModel extends ByteFormattingViewModel {

    @Getter private final StringProperty inputProperty = new SimpleStringProperty();
    @Getter private final StringProperty keyProperty = new SimpleStringProperty();
    @Getter private final StringProperty ivProperty = new SimpleStringProperty();
    @Getter private final ObjectProperty<String> streamCipherComboBoxProperty = new SimpleObjectProperty<>();
    @Getter private final ObservableList<String> streamCipherAlgorithmsList = FXCollections.observableArrayList();
    @Getter private final ObjectProperty<Integer> keyLengthComboBoxProperty = new SimpleObjectProperty<>();
    @Getter private final ObservableList<Integer> keyLengthList = FXCollections.observableArrayList();

    @Setter private Timeline emptyIvAnimation;
    @Setter private Timeline emptyKeyAnimation;

    public StreamCipherTextViewModel() {
        streamCipherAlgorithmsList.setAll(SecurityUtils.getStreamCiphers());
        streamCipherComboBoxProperty.set(streamCipherAlgorithmsList.getFirst());
    }

    public void onAlgorithmSelection(@SuppressWarnings("unused") ActionEvent e) {
        var algo = streamCipherComboBoxProperty.get();

        keyLengthList.setAll(IStreamCipher.getSupportedKeyLengths(algo));
        keyLengthComboBoxProperty.set(keyLengthList.getFirst());
    }

    public void onIvShuffleAction(@SuppressWarnings("unused") ActionEvent e) {
        var ivKeyLenOptional = IStreamCipher.getSupportedIvLength(streamCipherComboBoxProperty.get());

        if (ivKeyLenOptional.isEmpty())
            return;

        var value = SecurityUtils.generateIV(ivKeyLenOptional.get().getFirst());

        ivProperty.set(HexFormat.of().formatHex(value));
    }

    public boolean isNonIvAlgorithmSelected() {
        return IStreamCipher.getSupportedIvLength(streamCipherComboBoxProperty.get()).isEmpty();
    }

    public void action(boolean encrypt) {
        if (inputProperty.get().isEmpty())
            return;

        var algo = streamCipherComboBoxProperty.get();

        if (!isNonIvAlgorithmSelected() && ivProperty.get().isEmpty()) {
            emptyIvAnimation.playFromStart();
            return;
        }

        if (keyProperty.get().isEmpty()) {
            emptyKeyAnimation.playFromStart();
            return;
        }

        var text = inputProperty.get().getBytes(StandardCharsets.UTF_8);
        byte[] key = HexFormat.of().parseHex(keyProperty.get());
        byte[] value;
        byte[] iv = null;

        if (IStreamCipher.getSupportedIvLength(algo).isPresent())
            iv = HexFormat.of().parseHex(ivProperty.get());

        if (encrypt) {
            var enc = IStreamCipher.of(algo, Cipher.ENCRYPT_MODE, key, iv);

            value = IStreamCipher.doFinal(enc, text);
            counterText.set("Encoded %d bytes".formatted(value.length));
            outputProperty.set(formatOutput(value));
        } else {
            var input = HexFormat.of().parseHex(inputProperty.get());

            var dec = IStreamCipher.of(algo, Cipher.DECRYPT_MODE, key, iv);
            value = IStreamCipher.doFinal(dec, input);

            counterText.set("Decoded %d bytes".formatted(value.length));
            outputProperty.set(new String(value));
        }

    }

    @Override
    public void onInit() {
        inputProperty.set(MemCache.readString("stream.input", ""));
        keyProperty.set(MemCache.readString("stream.key", ""));
        ivProperty.set(MemCache.readString("stream.iv", ""));
        streamCipherComboBoxProperty.set(streamCipherAlgorithmsList.get(MemCache.readInteger("stream.algo", 0)));
        keyLengthComboBoxProperty.set(keyLengthList.get(MemCache.readInteger("stream.key.length", 0)));
        outputProperty.set(MemCache.readString("stream.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("stream.input", inputProperty.get());
        MemCache.writeString("stream.key", keyProperty.get());
        MemCache.writeString("stream.iv", ivProperty.get());
        MemCache.writeInteger("stream.algo", streamCipherAlgorithmsList.indexOf(streamCipherComboBoxProperty.get()));
        MemCache.writeInteger("stream.key.length", keyLengthList.indexOf(keyLengthComboBoxProperty.get()));
        MemCache.writeString("stream.output", outputProperty.get());
    }
}

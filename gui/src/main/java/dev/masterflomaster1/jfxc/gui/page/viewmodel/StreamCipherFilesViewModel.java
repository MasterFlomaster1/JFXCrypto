package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.core.IStreamCipher;
import dev.masterflomaster1.jfxc.core.SecurityUtils;
import dev.masterflomaster1.jfxc.core.io.CipherIO;
import dev.masterflomaster1.jfxc.core.io.CipherNIO;
import dev.masterflomaster1.jfxc.core.utils.StringUtils;
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
import java.io.File;
import java.util.HexFormat;

public final class StreamCipherFilesViewModel extends AbstractViewModel {

    @Getter private final StringProperty keyProperty = new SimpleStringProperty();
    @Getter private final StringProperty ivProperty = new SimpleStringProperty();
    @Getter private final ObjectProperty<String> streamCipherComboBoxProperty = new SimpleObjectProperty<>();
    @Getter private final ObservableList<String> streamCipherAlgorithmsList = FXCollections.observableArrayList();
    @Getter private final ObjectProperty<Integer> keyLengthComboBoxProperty = new SimpleObjectProperty<>();
    @Getter private final ObservableList<Integer> keyLengthList = FXCollections.observableArrayList();

    @Setter private Timeline emptyIvAnimation;
    @Setter private Timeline emptyTargetFileAnimation;
    @Setter private Timeline emptyDestinationFileAnimation;

    @Setter private File targetFile;
    @Setter private File destinationFile;

    private final CipherIO cipherIO = new CipherNIO();

    public StreamCipherFilesViewModel() {
        streamCipherAlgorithmsList.setAll(SecurityUtils.getStreamCiphers());
        streamCipherComboBoxProperty.set(streamCipherAlgorithmsList.getFirst());
    }

    @SuppressWarnings("unused")
    public void onAlgorithmSelection(ActionEvent e) {
        var algo = streamCipherComboBoxProperty.get();

        keyLengthList.setAll(IStreamCipher.getSupportedKeyLengths(algo));
        keyLengthComboBoxProperty.set(keyLengthList.getFirst());
    }

    @SuppressWarnings("unused")
    public void onIvShuffleAction(ActionEvent e) {
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
        if (targetFile == null) {
            emptyTargetFileAnimation.playFromStart();
            return;
        }

        if (destinationFile == null) {
            emptyDestinationFileAnimation.playFromStart();
            return;
        }

        var algo = streamCipherComboBoxProperty.get();

        if (IStreamCipher.getSupportedIvLength(algo).isPresent() && ivProperty.get().isEmpty()) {
            emptyIvAnimation.playFromStart();
            return;
        }

        byte[] key = HexFormat.of().parseHex(keyProperty.get());
        var iv = HexFormat.of().parseHex(ivProperty.get());

        if (encrypt) {
            var enc = IStreamCipher.of(algo, Cipher.ENCRYPT_MODE, key, iv);
            cipherIO.encrypt(enc, targetFile.toPath(), destinationFile.toPath());
            counterText.set("Encoded %s".formatted(StringUtils.convert(destinationFile.length())));
        } else {
            var enc = IStreamCipher.of(algo, Cipher.DECRYPT_MODE, key, iv);
            cipherIO.encrypt(enc, targetFile.toPath(), destinationFile.toPath());
            counterText.set("Decoded %s".formatted(StringUtils.convert(destinationFile.length())));
        }

    }

    @Override
    public void onInit() {
        streamCipherComboBoxProperty.set(streamCipherAlgorithmsList.get(MemCache.readInteger("stream.files.algo", 0)));
        keyLengthComboBoxProperty.set(keyLengthList.get(MemCache.readInteger("stream.files.key.len", 0)));
        keyProperty.set(MemCache.readString("stream.files.key", ""));
        ivProperty.set(MemCache.readString("stream.files.iv", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeInteger("stream.files.algo", streamCipherAlgorithmsList.indexOf(streamCipherComboBoxProperty.get()));
        MemCache.writeInteger("stream.files.key.len", keyLengthList.indexOf(keyLengthComboBoxProperty.get()));
        MemCache.writeString("stream.files.key", keyProperty.get());
        MemCache.writeString("stream.files.iv", ivProperty.get());
    }
}

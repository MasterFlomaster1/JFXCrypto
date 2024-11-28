package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.core.SecurityUtils;
import dev.masterflomaster1.jfxc.core.StreamCipherImpl;
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

    public StreamCipherFilesViewModel() {
        streamCipherAlgorithmsList.setAll(SecurityUtils.getStreamCiphers());
        streamCipherComboBoxProperty.set(streamCipherAlgorithmsList.get(0));
    }

    @SuppressWarnings("unused")
    public void onAlgorithmSelection(ActionEvent e) {
        var algo = streamCipherComboBoxProperty.get();

        keyLengthList.setAll(StreamCipherImpl.getCorrespondingKeyLengths(algo));
        keyLengthComboBoxProperty.set(keyLengthList.get(0));
    }

    @SuppressWarnings("unused")
    public void onIvShuffleAction(ActionEvent e) {
        var ivKeyLenOptional = StreamCipherImpl.getCorrespondingIvLengthBits(streamCipherComboBoxProperty.get());

        if (ivKeyLenOptional.isEmpty())
            return;

        var value = SecurityUtils.generateIV(ivKeyLenOptional.get().get(0));

        ivProperty.set(HexFormat.of().formatHex(value));
    }

    public boolean isNonIvAlgorithmSelected() {
        return StreamCipherImpl.getCorrespondingIvLengthBits(streamCipherComboBoxProperty.get()).isEmpty();
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

        if (StreamCipherImpl.getCorrespondingIvLengthBits(algo).isPresent() && ivProperty.get().isEmpty()) {
            emptyIvAnimation.playFromStart();
            return;
        }

        byte[] key = HexFormat.of().parseHex(keyProperty.get());
        var iv = HexFormat.of().parseHex(ivProperty.get());

        if (encrypt) {
            StreamCipherImpl.nioEncrypt(
                    targetFile.toPath(),
                    destinationFile.toPath(),
                    algo,
                    iv,
                    key
            );

            counterText.set("Encoded %s".formatted(StringUtils.convert(destinationFile.length())));
        } else {
            StreamCipherImpl.nioDecrypt(
                    targetFile.toPath(),
                    destinationFile.toPath(),
                    algo,
                    iv,
                    key
            );

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

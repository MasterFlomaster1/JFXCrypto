package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import dev.masterflomaster1.jfxc.crypto.StreamCipherImpl;
import dev.masterflomaster1.jfxc.utils.StringUtils;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.File;
import java.util.HexFormat;

public final class StreamCipherFilesViewModel extends AbstractViewModel {

    private final StringProperty keyText = new SimpleStringProperty();
    private final StringProperty ivText = new SimpleStringProperty();
    private final ObjectProperty<String> streamCipherComboBoxProperty = new SimpleObjectProperty<>();
    private final ObservableList<String> streamCipherAlgorithmsList = FXCollections.observableArrayList();
    private final ObjectProperty<Integer> keyLengthComboBoxProperty = new SimpleObjectProperty<>();
    private final ObservableList<Integer> keyLengthList = FXCollections.observableArrayList();

    private Timeline emptyIvAnimation;
    private Timeline emptyTargetFileAnimation;
    private Timeline emptyDestinationFileAnimation;

    private File targetFile;
    private File destinationFile;

    public StreamCipherFilesViewModel() {
        streamCipherAlgorithmsList.setAll(SecurityUtils.getStreamCiphers());
        streamCipherComboBoxProperty.set(streamCipherAlgorithmsList.get(0));
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

    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }

    public void setDestinationFile(File destinationFile) {
        this.destinationFile = destinationFile;
    }

    public void setEmptyIvAnimation(Timeline emptyIvAnimation) {
        this.emptyIvAnimation = emptyIvAnimation;
    }

    public void setEmptyTargetFileAnimation(Timeline emptyTargetFileAnimation) {
        this.emptyTargetFileAnimation = emptyTargetFileAnimation;
    }

    public void setEmptyDestinationFileAnimation(Timeline emptyDestinationFileAnimation) {
        this.emptyDestinationFileAnimation = emptyDestinationFileAnimation;
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

        ivText.set(HexFormat.of().formatHex(value));
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

        if (StreamCipherImpl.getCorrespondingIvLengthBits(algo).isPresent() && ivText.get().isEmpty()) {
            emptyIvAnimation.playFromStart();
            return;
        }

        byte[] key = HexFormat.of().parseHex(keyText.get());
        var iv = HexFormat.of().parseHex(ivText.get());

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
        keyText.set(MemCache.readString("stream.files.key", ""));
        ivText.set(MemCache.readString("stream.files.iv", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeInteger("stream.files.algo", streamCipherAlgorithmsList.indexOf(streamCipherComboBoxProperty.get()));
        MemCache.writeInteger("stream.files.key.len", keyLengthList.indexOf(keyLengthComboBoxProperty.get()));
        MemCache.writeString("stream.files.key", keyText.get());
        MemCache.writeString("stream.files.iv", ivText.get());
    }
}

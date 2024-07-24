package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.crypto.BlockCipherImpl;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
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

public class BlockCipherFilesViewModel {

    private final StringProperty keyText = new SimpleStringProperty();
    private final StringProperty ivText = new SimpleStringProperty();
    private final ObjectProperty<String> blockCipherComboBoxProperty = new SimpleObjectProperty<>();
    private final ObservableList<String> blockCipherAlgorithmsList = FXCollections.observableArrayList();
    private final ObjectProperty<String> modesComboBoxProperty = new SimpleObjectProperty<>();
    private final ObservableList<String> modesList = FXCollections.observableArrayList();
    private final ObjectProperty<String> paddingsComboBoxProperty = new SimpleObjectProperty<>();
    private final ObservableList<String> paddingsList = FXCollections.observableArrayList();
    private final ObjectProperty<Integer> keyLengthComboBoxProperty = new SimpleObjectProperty<>();
    private final ObservableList<Integer> keyLengthList = FXCollections.observableArrayList();
    private final StringProperty counterText = new SimpleStringProperty();

    private Timeline emptyIvAnimation;
    private Timeline emptyTargetFileAnimation;
    private Timeline emptyDestinationFileAnimation;

    private File targetFile;
    private File destinationFile;

    public BlockCipherFilesViewModel() {
        blockCipherAlgorithmsList.setAll(SecurityUtils.getBlockCiphers());

        for (BlockCipherImpl.Padding p: BlockCipherImpl.Padding.values()) {
            paddingsList.add(p.getPadding());
        }
        paddingsComboBoxProperty.set(paddingsList.get(0));

        for (BlockCipherImpl.Mode m: BlockCipherImpl.Mode.values()) {
            modesList.add(m.getMode());
        }
        modesComboBoxProperty.set(modesList.get(0));
    }

    public StringProperty keyTextProperty() {
        return keyText;
    }

    public StringProperty ivTextProperty() {
        return ivText;
    }

    public StringProperty counterTextProperty() {
        return counterText;
    }

    public ObjectProperty<String> blockCipherComboBoxProperty() {
        return blockCipherComboBoxProperty;
    }

    public ObservableList<String> getBlockCipherAlgorithmsList() {
        return blockCipherAlgorithmsList;
    }

    public ObjectProperty<String> modesComboBoxProperty() {
        return modesComboBoxProperty;
    }

    public ObservableList<String> getModesList() {
        return modesList;
    }

    public ObjectProperty<String> paddingsComboBoxProperty() {
        return paddingsComboBoxProperty;
    }

    public ObservableList<String> getPaddingsList() {
        return paddingsList;
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
        var algo = blockCipherComboBoxProperty.get();
        keyLengthList.setAll(BlockCipherImpl.getAvailableKeyLengths(algo));
        keyLengthComboBoxProperty.set(keyLengthList.get(0)); // Select first element
    }

    @SuppressWarnings("unused")
    public void onIvShuffleAction(ActionEvent e) {
        var value = BlockCipherImpl.generateIV(blockCipherComboBoxProperty.get());

        ivText.set(HexFormat.of().formatHex(value));
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

        var algo = blockCipherComboBoxProperty.get();
        var mode = BlockCipherImpl.Mode.fromString(modesComboBoxProperty.get());

        if (mode != BlockCipherImpl.Mode.ECB && ivText.get().isEmpty()) {
            emptyIvAnimation.playFromStart();
            return;
        }

        byte[] key = HexFormat.of().parseHex(keyText.get());
        var padding = BlockCipherImpl.Padding.fromString(paddingsComboBoxProperty.get());
        var iv = HexFormat.of().parseHex(ivText.get());

        if (encrypt) {
            BlockCipherImpl.encryptFile(
                    targetFile.getAbsolutePath(),
                    destinationFile.getAbsolutePath(),
                    algo,
                    mode,
                    padding,
                    iv,
                    key
            );

            counterText.set("Encoded %d bytes".formatted(destinationFile.length()));
        } else {
            BlockCipherImpl.decryptFile(
                    targetFile.getAbsolutePath(),
                    destinationFile.getAbsolutePath(),
                    algo,
                    mode,
                    padding,
                    iv,
                    key
            );

            counterText.set("Decoded %d bytes".formatted(destinationFile.length()));
        }
    }

}

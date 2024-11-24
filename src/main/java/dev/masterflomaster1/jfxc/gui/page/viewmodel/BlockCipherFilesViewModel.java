package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.BlockCipherImpl;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import dev.masterflomaster1.jfxc.utils.StringUtils;
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

public final class BlockCipherFilesViewModel extends AbstractViewModel {

    @Getter private final StringProperty keyProperty = new SimpleStringProperty();
    @Getter private final StringProperty ivProperty = new SimpleStringProperty();
    @Getter private final ObjectProperty<String> blockCipherComboBoxProperty = new SimpleObjectProperty<>();
    @Getter private final ObservableList<String> blockCipherAlgorithmsList = FXCollections.observableArrayList();
    @Getter private final ObjectProperty<String> modesComboBoxProperty = new SimpleObjectProperty<>();
    @Getter private final ObservableList<String> modesList = FXCollections.observableArrayList();
    @Getter private final ObjectProperty<String> paddingsComboBoxProperty = new SimpleObjectProperty<>();
    @Getter private final ObservableList<String> paddingsList = FXCollections.observableArrayList();
    @Getter private final ObjectProperty<Integer> keyLengthComboBoxProperty = new SimpleObjectProperty<>();
    @Getter private final ObservableList<Integer> keyLengthList = FXCollections.observableArrayList();

    @Setter private Timeline emptyIvAnimation;
    @Setter private Timeline emptyTargetFileAnimation;
    @Setter private Timeline emptyDestinationFileAnimation;

    @Setter private File targetFile;
    @Setter private File destinationFile;

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

    @SuppressWarnings("unused")
    public void onAlgorithmSelection(ActionEvent e) {
        var algo = blockCipherComboBoxProperty.get();
        keyLengthList.setAll(BlockCipherImpl.getAvailableKeyLengths(algo));
        keyLengthComboBoxProperty.set(keyLengthList.get(0)); // Select first element
    }

    @SuppressWarnings("unused")
    public void onIvShuffleAction(ActionEvent e) {
        var value = BlockCipherImpl.generateIV(blockCipherComboBoxProperty.get());

        ivProperty.set(HexFormat.of().formatHex(value));
    }

    public boolean isNonIvModeSelected() {
        return BlockCipherImpl.Mode.fromString(modesComboBoxProperty.get()) == BlockCipherImpl.Mode.ECB;
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

        if (mode != BlockCipherImpl.Mode.ECB && ivProperty.get().isEmpty()) {
            emptyIvAnimation.playFromStart();
            return;
        }

        byte[] key = HexFormat.of().parseHex(keyProperty.get());
        var padding = BlockCipherImpl.Padding.fromString(paddingsComboBoxProperty.get());
        var iv = HexFormat.of().parseHex(ivProperty.get());

        if (encrypt) {
            BlockCipherImpl.nioEncrypt(
                    targetFile.getAbsolutePath(),
                    destinationFile.getAbsolutePath(),
                    algo,
                    mode,
                    padding,
                    iv,
                    key
            );

            counterText.set("Encoded %s".formatted(StringUtils.convert(destinationFile.length())));
        } else {
            BlockCipherImpl.nioDecrypt(
                    targetFile.getAbsolutePath(),
                    destinationFile.getAbsolutePath(),
                    algo,
                    mode,
                    padding,
                    iv,
                    key
            );

            counterText.set("Decoded %s".formatted(StringUtils.convert(destinationFile.length())));
        }
    }

    @Override
    public void onInit() {
        blockCipherComboBoxProperty.set(blockCipherAlgorithmsList.get(MemCache.readInteger("block.files.algo", 0)));
        keyLengthComboBoxProperty.set(keyLengthList.get(MemCache.readInteger("block.files.key.len", 0)));
        modesComboBoxProperty.set(modesList.get(MemCache.readInteger("block.files.mode", 0)));
        paddingsComboBoxProperty.set(paddingsList.get(MemCache.readInteger("block.files.padding", 0)));
        ivProperty.set(MemCache.readString("block.files.iv", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeInteger("block.files.algo", blockCipherAlgorithmsList.indexOf(blockCipherComboBoxProperty.get()));
        MemCache.writeInteger("block.files.key.len", keyLengthList.indexOf(keyLengthComboBoxProperty.get()));
        MemCache.writeInteger("block.files.mode", modesList.indexOf(modesComboBoxProperty.get()));
        MemCache.writeInteger("block.files.padding", paddingsList.indexOf(paddingsComboBoxProperty.get()));
        MemCache.writeString("block.files.iv", ivProperty.get());
    }
}

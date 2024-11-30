package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.core.BlockCipher;
import dev.masterflomaster1.jfxc.core.SecurityUtils;
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

    private final CipherNIO nio = new CipherNIO();

    public BlockCipherFilesViewModel() {
        blockCipherAlgorithmsList.setAll(SecurityUtils.getBlockCiphers());

        for (BlockCipher.Padding p: BlockCipher.Padding.values()) {
            paddingsList.add(p.getPadding());
        }
        paddingsComboBoxProperty.set(paddingsList.getFirst());

        for (BlockCipher.Mode m: BlockCipher.Mode.values()) {
            modesList.add(m.getMode());
        }
        modesComboBoxProperty.set(modesList.getFirst());
    }

    @SuppressWarnings("unused")
    public void onAlgorithmSelection(ActionEvent e) {
        var algo = blockCipherComboBoxProperty.get();
        keyLengthList.setAll(BlockCipher.getSupportedKeyLengths(algo));
        keyLengthComboBoxProperty.set(keyLengthList.getFirst());
    }

    @SuppressWarnings("unused")
    public void onIvShuffleAction(ActionEvent e) {
        var algo = blockCipherComboBoxProperty.get();
        var value = SecurityUtils.generateIV(BlockCipher.getBlockLength(algo));

        ivProperty.set(HexFormat.of().formatHex(value));
    }

    public boolean isNonIvModeSelected() {
        return BlockCipher.Mode.fromString(modesComboBoxProperty.get()) == BlockCipher.Mode.ECB;
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
        var mode = BlockCipher.Mode.fromString(modesComboBoxProperty.get());

        if (mode != BlockCipher.Mode.ECB && ivProperty.get().isEmpty()) {
            emptyIvAnimation.playFromStart();
            return;
        }

        byte[] key = HexFormat.of().parseHex(keyProperty.get());
        var padding = BlockCipher.Padding.fromString(paddingsComboBoxProperty.get());
        var iv = HexFormat.of().parseHex(ivProperty.get());

        if (encrypt) {
            var enc = BlockCipher.of(algo, Cipher.ENCRYPT_MODE, mode, padding, key, iv);
            nio.encrypt(enc, targetFile.toPath(), destinationFile.toPath());

            counterText.set("Encoded %s".formatted(StringUtils.convert(destinationFile.length())));
        } else {
            var dec = BlockCipher.of(algo, Cipher.DECRYPT_MODE, mode, padding, key, iv);
            nio.decrypt(dec, targetFile.toPath(), destinationFile.toPath());

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

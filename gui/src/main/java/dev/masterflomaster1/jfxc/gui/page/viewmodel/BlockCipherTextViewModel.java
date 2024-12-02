package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.core.IBlockCipher;
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

public final class BlockCipherTextViewModel extends AbstractViewModel {

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
    @Setter private Timeline emptyKeyAnimation;
    @Setter private InputOutputAreaComponentViewModel ioComponentViewModel;

    public BlockCipherTextViewModel() {
        blockCipherAlgorithmsList.setAll(SecurityUtils.getBlockCiphers());

        for (IBlockCipher.Padding p: IBlockCipher.Padding.values()) {
            paddingsList.add(p.getPadding());
        }
        paddingsComboBoxProperty.set(paddingsList.getFirst());
    }

    @SuppressWarnings("unused")
    public void onAlgorithmSelection(ActionEvent e) {
        var algo = blockCipherComboBoxProperty.get();
        keyLengthList.setAll(IBlockCipher.getSupportedKeyLengths(algo));
        keyLengthComboBoxProperty.set(keyLengthList.getFirst());

        modesList.clear();
        for (var a : IBlockCipher.getSupportedModes(algo)) {
            modesList.add(a.getMode());
        }
        modesComboBoxProperty.set(modesList.getFirst());
    }

    @SuppressWarnings("unused")
    public void onIvShuffleAction(ActionEvent e) {
        var algo = blockCipherComboBoxProperty.get();
        var value = SecurityUtils.generateIV(IBlockCipher.getBlockLength(algo));

        ivProperty.set(HexFormat.of().formatHex(value));
    }

    public boolean isGcmModeSelected() {
        if (modesComboBoxProperty.get() == null)
            return false;

        return IBlockCipher.Mode.fromString(modesComboBoxProperty.get()) == IBlockCipher.Mode.GCM;
    }

    public boolean isNonIvModeSelected() {
        if (modesComboBoxProperty.get() == null)
            return false;

        return IBlockCipher.Mode.fromString(modesComboBoxProperty.get()) == IBlockCipher.Mode.ECB;
    }

    public void action(boolean encrypt) {
        if (ioComponentViewModel.getInputProperty().get().isEmpty())
            return;

        var algo = blockCipherComboBoxProperty.get();
        var mode = IBlockCipher.Mode.fromString(modesComboBoxProperty.get());

        if (mode != IBlockCipher.Mode.ECB && ivProperty.get().isEmpty()) {
            emptyIvAnimation.playFromStart();
            return;
        }

        if (keyProperty.get().isEmpty()) {
            emptyKeyAnimation.playFromStart();
            return;
        }

        var text = ioComponentViewModel.inputProperty.get().getBytes(StandardCharsets.UTF_8);
        byte[] key = HexFormat.of().parseHex(keyProperty.get());
        byte[] value;

        var padding = IBlockCipher.Padding.fromString(paddingsComboBoxProperty.get());
        var iv = HexFormat.of().parseHex(ivProperty.get());

        var enc = IBlockCipher.of(algo, Cipher.ENCRYPT_MODE, mode, padding, key, iv);
        var dec = IBlockCipher.of(algo, Cipher.DECRYPT_MODE, mode, padding, key, iv);

        if (encrypt) {
            value = IBlockCipher.doFinal(enc, text);
            ioComponentViewModel.setOutput(value);
        } else {
            var input = ioComponentViewModel.getInput();

            value = IBlockCipher.doFinal(dec, input);
            ioComponentViewModel.setOutput(value);
        }
    }

    @Override
    public void onInit() {
        ioComponentViewModel.inputProperty.set(MemCache.readString("block.input", ""));
        blockCipherComboBoxProperty.set(blockCipherAlgorithmsList.get(MemCache.readInteger("block.algo", 0)));
        keyLengthComboBoxProperty.set(keyLengthList.get(MemCache.readInteger("block.key.len", 0)));
        modesComboBoxProperty.set(modesList.get(MemCache.readInteger("block.mode", 0)));
        paddingsComboBoxProperty.set(paddingsList.get(MemCache.readInteger("block.padding", 0)));
        ivProperty.set(MemCache.readString("block.iv", ""));
        ioComponentViewModel.outputLengthProperty.set(MemCache.readString("block.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("block.input", ioComponentViewModel.inputProperty.get());
        MemCache.writeInteger("block.algo", blockCipherAlgorithmsList.indexOf(blockCipherComboBoxProperty.get()));
        MemCache.writeInteger("block.key.len", keyLengthList.indexOf(keyLengthComboBoxProperty.get()));
        MemCache.writeInteger("block.mode", modesList.indexOf(modesComboBoxProperty.get()));
        MemCache.writeInteger("block.padding", paddingsList.indexOf(paddingsComboBoxProperty.getValue()));
        MemCache.writeString("block.iv", ivProperty.get());
        MemCache.writeString("block.output", ioComponentViewModel.outputLengthProperty.get());
    }
}

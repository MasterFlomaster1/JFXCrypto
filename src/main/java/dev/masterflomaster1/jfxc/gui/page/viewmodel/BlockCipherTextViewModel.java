package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.BlockCipherImpl;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import dev.masterflomaster1.jfxc.utils.StringUtils;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;

public class BlockCipherTextViewModel extends AbstractViewModel {

    private final StringProperty inputText = new SimpleStringProperty();
    private final StringProperty outputText = new SimpleStringProperty();
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

    private final BooleanProperty hexModeToggleButtonProperty = new SimpleBooleanProperty();
    private final BooleanProperty b64ModeToggleButtonProperty = new SimpleBooleanProperty();

    private Timeline emptyIvAnimation;
    private Timeline emptyKeyAnimation;

    public BlockCipherTextViewModel() {
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

    public BooleanProperty hexModeToggleButtonPropertyProperty() {
        return hexModeToggleButtonProperty;
    }

    public BooleanProperty b64ModeToggleButtonPropertyProperty() {
        return b64ModeToggleButtonProperty;
    }

    public void setEmptyIvAnimation(Timeline emptyIvAnimation) {
        this.emptyIvAnimation = emptyIvAnimation;
    }

    public void setEmptyKeyAnimation(Timeline emptyKeyAnimation) {
        this.emptyKeyAnimation = emptyKeyAnimation;
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

    public boolean isNonIvModeSelected() {
        return BlockCipherImpl.Mode.fromString(modesComboBoxProperty.get()) == BlockCipherImpl.Mode.ECB;
    }

    @SuppressWarnings("unused")
    public void onToggleChanged(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        if (newValue == null) {
            if (oldValue != null)
                oldValue.setSelected(true);
            return;
        }

        var selectedButton = (ToggleButton) newValue;

        // bypass unpredictable behavior of ToggleButtonProperty.get()
        if (selectedButton.getText().equalsIgnoreCase("Hex")) {
            hexModeToggleButtonProperty.set(true);
            b64ModeToggleButtonProperty.set(false);
        } else if (selectedButton.getText().equalsIgnoreCase("Base64")) {
            b64ModeToggleButtonProperty.set(true);
            hexModeToggleButtonProperty.set(false);
        }
    }

    public void action(boolean encrypt) {
        if (inputText.get().isEmpty())
            return;

        var algo = blockCipherComboBoxProperty.get();
        var mode = BlockCipherImpl.Mode.fromString(modesComboBoxProperty.get());

        if (mode != BlockCipherImpl.Mode.ECB && ivText.get().isEmpty()) {
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

        var padding = BlockCipherImpl.Padding.fromString(paddingsComboBoxProperty.get());
        var iv = HexFormat.of().parseHex(ivText.get());

        if (encrypt) {
            value = BlockCipherImpl.encrypt(algo, mode, padding, iv, text, key);
            counterText.set("Encoded %s".formatted(StringUtils.convert(value.length)));
            outputText.set(formatOutput(value));
        } else {
            var input = HexFormat.of().parseHex(inputText.get());

            value = BlockCipherImpl.decrypt(algo, mode, padding, iv, input, key);
            counterText.set("Decoded %s".formatted(StringUtils.convert(value.length)));
            outputText.set(new String(value));
        }
    }

    private String formatOutput(byte[] value) {
        if (hexModeToggleButtonProperty.get()) {
            return HexFormat.of().formatHex(value);
        } else if (b64ModeToggleButtonProperty.get()) {
            return Base64.getEncoder().encodeToString(value);
        }

        return "";
    }

    @Override
    public void onInit() {
        inputText.set(MemCache.readString("block.input", ""));
        blockCipherComboBoxProperty.set(blockCipherAlgorithmsList.get(MemCache.readInteger("block.algo", 0)));
        keyLengthComboBoxProperty.set(keyLengthList.get(MemCache.readInteger("block.key.len", 0)));
        modesComboBoxProperty.set(modesList.get(MemCache.readInteger("block.mode", 0)));
        paddingsComboBoxProperty.set(paddingsList.get(MemCache.readInteger("block.padding", 0)));
        ivText.set(MemCache.readString("block.iv", ""));
        outputText.set(MemCache.readString("block.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("block.input", inputText.get());
        MemCache.writeInteger("block.algo", blockCipherAlgorithmsList.indexOf(blockCipherComboBoxProperty.get()));
        MemCache.writeInteger("block.key.len", keyLengthList.indexOf(keyLengthComboBoxProperty.get()));
        MemCache.writeInteger("block.mode", modesList.indexOf(modesComboBoxProperty.get()));
        MemCache.writeInteger("block.padding", paddingsList.indexOf(paddingsComboBoxProperty.getValue()));
        MemCache.writeString("block.iv", ivText.get());
        MemCache.writeString("block.output", outputText.get());
    }
}

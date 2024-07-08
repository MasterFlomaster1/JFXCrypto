package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.MemCache;
import dev.masterflomaster1.sjc.crypto.BlockCipherImpl;
import dev.masterflomaster1.sjc.crypto.SecurityUtils;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Set;

public final class BlockCipherPage extends SimplePage {

    public static final String NAME = "Block Cipher";

    private final TextArea inputTextArea = new TextArea();
    private final TextField keyTextField = new TextField();
    private final TextField ivTextField = new TextField();
    private final TextField passwordTextField = new TextField();
    private final TextArea outputTextArea = new TextArea();
    private final ComboBox<String> blockCipherComboBox = new ComboBox<>();
    private final ComboBox<String> modesComboBox = new ComboBox<>();
    private final ComboBox<String> paddingsComboBox = new ComboBox<>();
    private final ComboBox<Integer> keyLengthComboBox = new ComboBox<>();
    private final ToggleButton hexModeToggleBtn = new ToggleButton("Hex");
    private final ToggleButton b64ModeToggleBtn = new ToggleButton("Base64");

    private Timeline emptyKeyAnimation;
    private Timeline emptyIvAnimation;

    private InputGroup ivGroup;

    public BlockCipherPage() {
        super();

        addSection("Block Cipher", mainSection());
        onInit();

        onAlgorithmSelection();
        onModeSelection();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Encryption / Decryption tool"
        );

        inputTextArea.setPromptText("Plaintext to encrypt, hex data to decrypt");
        inputTextArea.setWrapText(true);
        inputTextArea.setMaxHeight(100);

        outputTextArea.setPromptText("Result");
        outputTextArea.setWrapText(true);
        outputTextArea.setEditable(false);
        outputTextArea.setMaxHeight(100);

        Set<String> set = SecurityUtils.getBlockCiphers();
        blockCipherComboBox.getItems().setAll(set);
        blockCipherComboBox.getSelectionModel().selectFirst();

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> action(true));
        decryptButton.setOnAction(event -> action(false));

        blockCipherComboBox.setOnAction(event -> onAlgorithmSelection());

        var keyLenLabel = new Label("Key Length");
        var keyLenGroup = new InputGroup(keyLenLabel, keyLengthComboBox);

        var keyGroup = new InputGroup(keyLabel, keyTextField);

        ObservableList<String> paddingsList = FXCollections.observableArrayList();
        for (BlockCipherImpl.Padding p: BlockCipherImpl.Padding.values()) {
            paddingsList.add(p.getPadding());
        }
        paddingsComboBox.setItems(paddingsList);
        paddingsComboBox.getSelectionModel().selectFirst();
        var paddingsLabel = new Label("Padding");
        var paddingGroup = new InputGroup(paddingsLabel, paddingsComboBox);

        ObservableList<String> modesList = FXCollections.observableArrayList();
        for (BlockCipherImpl.Mode m: BlockCipherImpl.Mode.values()) {
            modesList.add(m.getMode());
        }
        modesComboBox.setItems(modesList);
        modesComboBox.getSelectionModel().selectFirst();
        var modeLabel = new Label("Mode");
        var modeGroup = new InputGroup(modeLabel, modesComboBox);
        modesComboBox.setOnAction(event -> onModeSelection());

        var ivLabel = new Label("IV");
        var ivShuffleButton = new Button("", new FontIcon(BootstrapIcons.SHUFFLE));
        ivGroup = new InputGroup(ivLabel, ivTextField, ivShuffleButton);
        Tooltip ivTooltip = new Tooltip("Initialization Vector (IV). Required for certain encryption modes" +
                " (e.g., CBC). Must match block size.");
        Tooltip.install(ivGroup, ivTooltip);

        ivShuffleButton.setOnAction(event -> onIvShuffleButtonPressed());

        var cipherSettingsContainer = new FlowPane(
                20, 20,
                blockCipherComboBox,
                keyLenGroup,
                modeGroup,
                paddingGroup,
                ivGroup
                );

        var keySettingsContainer = new FlowPane(
                20, 20,
                keyGroup
        );

        var passBasedHBox = new HBox(
                20,
                new Label("From password"),
                passwordTextField
        );

        var controlsHBox2 = new HBox(
                20, encryptButton, decryptButton
        );

        var toggleGroup = new ToggleGroup();
        hexModeToggleBtn.setSelected(true);
        hexModeToggleBtn.setToggleGroup(toggleGroup);
        b64ModeToggleBtn.setToggleGroup(toggleGroup);
        hexModeToggleBtn.getStyleClass().add(Styles.LEFT_PILL);
        b64ModeToggleBtn.getStyleClass().add(Styles.RIGHT_PILL);

        var outputModeHBox = new HBox(hexModeToggleBtn, b64ModeToggleBtn);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                oldValue.setSelected(true);
            }
        });

        var copyHashButton = new Button("Copy");

        copyHashButton.setOnAction(event -> {
            var cc = new ClipboardContent();
            cc.putString(outputTextArea.getText());
            Clipboard.getSystemClipboard().setContent(cc);
        });
        counterLabel.getStyleClass().add(Styles.SUCCESS);

        var footerHBox = new HBox(
                20, copyHashButton, outputModeHBox, counterLabel
        );
        footerHBox.setAlignment(Pos.CENTER_LEFT);

        var keySectionHeader = new Text("Key generation options:");

        emptyIvAnimation = Animations.wobble(ivGroup);

        return new VBox(
                20,
                description,
                inputTextArea,
                cipherSettingsContainer,
                keySectionHeader,
                passBasedHBox,
                keySettingsContainer,
                controlsHBox2,
                outputTextArea,
                footerHBox
        );
    }

    private void onAlgorithmSelection() {
        var algo = blockCipherComboBox.getValue();
        keyLengthComboBox.getItems().setAll(BlockCipherImpl.getAvailableKeyLengths(algo));
        keyLengthComboBox.getSelectionModel().selectFirst();
    }

    private void onModeSelection() {
        var mode = modesComboBox.getValue();

        ivGroup.setDisable(BlockCipherImpl.Mode.fromString(mode) == BlockCipherImpl.Mode.ECB);
    }

    private void onIvShuffleButtonPressed() {
        var value = BlockCipherImpl.generateIV(blockCipherComboBox.getValue());

        ivTextField.setText(HexFormat.of().formatHex(value));
    }

    private void action(boolean encrypt) {
        if (inputTextArea.getText().isEmpty())
            return;

        var algo = blockCipherComboBox.getValue();
        var mode = BlockCipherImpl.Mode.fromString(modesComboBox.getValue());

        if (mode != BlockCipherImpl.Mode.ECB && ivTextField.getText().isEmpty()) {
            emptyIvAnimation.playFromStart();
            return;
        }

        var text = inputTextArea.getText().getBytes(StandardCharsets.UTF_8);
        char[] pass = keyTextField.getText().toCharArray();
        var encKey = BlockCipherImpl.generatePasswordBasedKey(pass, keyLengthComboBox.getValue());
        byte[] value;

        var padding = BlockCipherImpl.Padding.fromString(paddingsComboBox.getValue());
        var iv = HexFormat.of().parseHex(ivTextField.getText());

        if (encrypt) {
            value = BlockCipherImpl.encrypt(algo, mode, padding, iv, text, encKey);
            counterLabel.setText("Encoded %d bytes".formatted(value.length));
            outputTextArea.setText(output(value));
        } else {
            var input = HexFormat.of().parseHex(inputTextArea.getText());

            value = BlockCipherImpl.decrypt(algo, mode, padding, iv, input, encKey);
            counterLabel.setText("Decoded %d bytes".formatted(value.length));
            outputTextArea.setText(new String(value));
        }
    }

    private String output(byte[] value) {
        if (hexModeToggleBtn.isSelected())
            return HexFormat.of().formatHex(value);

        if (b64ModeToggleBtn.isSelected())
            return Base64.getEncoder().encodeToString(value);

        return "";
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onInit() {
        inputTextArea.setText(MemCache.readString("block.input", ""));
        blockCipherComboBox.getSelectionModel().select(MemCache.readInteger("block.algo", 0));
        keyLengthComboBox.getSelectionModel().select(MemCache.readInteger("block.key.len", 0));
        modesComboBox.getSelectionModel().select(MemCache.readInteger("block.mode", 0));
        paddingsComboBox.getSelectionModel().select(MemCache.readInteger("block.padding", 0));
        ivTextField.setText(MemCache.readString("block.iv", ""));
        outputTextArea.setText(MemCache.readString("block.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("block.input", inputTextArea.getText());
        MemCache.writeInteger("block.algo", blockCipherComboBox.getItems().indexOf(blockCipherComboBox.getValue()));
        MemCache.writeInteger("block.key.len", keyLengthComboBox.getItems().indexOf(keyLengthComboBox.getValue()));
        MemCache.writeInteger("block.mode", modesComboBox.getItems().indexOf(modesComboBox.getValue()));
        MemCache.writeInteger("block.padding", paddingsComboBox.getItems().indexOf(paddingsComboBox.getValue()));
        MemCache.writeString("block.iv", ivTextField.getText());
        MemCache.writeString("block.output", outputTextArea.getText());
    }
}

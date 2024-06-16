package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.crypto.BlockCipherImpl;
import dev.masterflomaster1.sjc.crypto.SecurityUtils;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.Set;

public final class BlockCipherPage extends SimplePage {

    public static final String NAME = "Block Cipher";

    private final TextArea inputTextArea = new TextArea();
    private final TextField keyTextField = new TextField();
    private final TextArea outputTextArea = new TextArea();
    private final ComboBox<String> blockCipherComboBox = new ComboBox<>();
    private final Label counterLabel = new Label("", new FontIcon(Material2AL.LABEL));

    public BlockCipherPage() {
        super();

        addSection("Block Cipher", mainSection());
        onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Encryption / Decryption tool"
        );

        inputTextArea.setPromptText("Enter data to encrypt / decrypt");
        inputTextArea.setWrapText(true);

        outputTextArea.setPromptText("Result");
        outputTextArea.setWrapText(true);
        outputTextArea.setEditable(false);

        Set<String> set = SecurityUtils.getBlockCiphers();
        blockCipherComboBox.getItems().setAll(set);
        blockCipherComboBox.getSelectionModel().select(0);

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> action(true));
        decryptButton.setOnAction(event -> action(false));

        var keyGroup = new InputGroup(keyLabel, keyTextField);
        var controlsHBox = new HBox(
                20, blockCipherComboBox, keyGroup, encryptButton, decryptButton
        );

        var copyHashButton = new Button("Copy Hash");

        copyHashButton.setOnAction(event -> {
            var cc = new ClipboardContent();
            cc.putString(outputTextArea.getText());
            Clipboard.getSystemClipboard().setContent(cc);
        });
        counterLabel.getStyleClass().add(Styles.SUCCESS);

        var footerHBox = new HBox(
                20, copyHashButton, counterLabel
        );
        footerHBox.setAlignment(Pos.CENTER_LEFT);

        return new VBox(
                20,
                description,
                inputTextArea,
                controlsHBox,
                outputTextArea,
                footerHBox
        );
    }

    private void action(boolean encrypt) {
        if (inputTextArea.getText().isEmpty())
            return;

        var text = inputTextArea.getText().getBytes(StandardCharsets.UTF_8);
        byte[] key = new byte[16];
        byte[] value;

        if (encrypt) {
            value = BlockCipherImpl.encrypt(blockCipherComboBox.getValue(), text, key);
            counterLabel.setText("Encoded %d bytes".formatted(value.length));
            outputTextArea.setText(HexFormat.of().formatHex(value));
        } else {
            value = BlockCipherImpl.decrypt(blockCipherComboBox.getValue(), text, key);
            counterLabel.setText("Decoded %d bytes".formatted(value.length));
            outputTextArea.setText(new String(value));
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void onReset() {
        super.onReset();
    }
}

package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.crypto.BlockCipherImpl;
import dev.masterflomaster1.sjc.crypto.SecurityUtils;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import javafx.geometry.Insets;
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
    private final ComboBox<Integer> keyLengthComboBox = new ComboBox<>();
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
        blockCipherComboBox.getSelectionModel().selectFirst();
        onAlgorithmSelection();

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> action(true));
        decryptButton.setOnAction(event -> action(false));

        blockCipherComboBox.setOnAction(event -> onAlgorithmSelection());

        Tab textTab = new Tab("Text");
        Tab bytesTab = new Tab("Bytes");
        TabPane tabPane = new TabPane(textTab, bytesTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getStyleClass().add(Styles.TABS_CLASSIC);

        VBox vbox1 = new VBox(inputTextArea);
        vbox1.setPadding(new Insets(2));
        textTab.setContent(vbox1);

        VBox vbox2 = new VBox(inputTextArea);
        vbox2.setPadding(new Insets(2));
        bytesTab.setContent(vbox2);

        var keyLenLabel = new Label("Key Length");
        var keyLenGroup = new InputGroup(keyLenLabel, keyLengthComboBox);

        var keyGroup = new InputGroup(keyLabel, keyTextField);
        var controlsHBox = new HBox(
                20, blockCipherComboBox, keyLenGroup, keyGroup
        );
        var controlsHBox2 = new HBox(
                20, encryptButton, decryptButton
        );

        var copyHashButton = new Button("Copy");

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
                tabPane,
//                inputTextArea,
                controlsHBox,
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

    private void action(boolean encrypt) {
        if (inputTextArea.getText().isEmpty())
            return;

        var text = inputTextArea.getText().getBytes(StandardCharsets.UTF_8);
        var algo = blockCipherComboBox.getValue();
        char[] pass = keyTextField.getText().toCharArray();
        var encKey = BlockCipherImpl.generatePasswordBasedKey(pass, keyLengthComboBox.getValue());
        byte[] value;

        byte[] buf = new byte[] {};

        if (encrypt) {
            value = BlockCipherImpl.encrypt(algo, text, encKey);
            buf = value;
            counterLabel.setText("Encoded %d bytes".formatted(value.length));
            outputTextArea.setText(HexFormat.of().formatHex(value));
        } else {
            value = BlockCipherImpl.decrypt(algo, buf, encKey);
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

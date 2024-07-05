package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.MemCache;
import dev.masterflomaster1.sjc.crypto.impl.CaesarCipherImpl;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class CaesarPage extends SimplePage {

    public static final String NAME = "Caesar Cipher";

    private Spinner<Integer> shiftSpinner;
    private final TextArea inputTextArea = new TextArea();
    private final TextArea outputTextArea = new TextArea();

    public CaesarPage() {
        super();
        addSection("Caesar Cipher", mainSection());

        onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Method in which each letter in the plaintext is replaced by a letter some fixed number of " +
                        "positions down the alphabet. The method is named after Julius Caesar, who used it in his " +
                        "private correspondence."
        );

        inputTextArea.setPromptText("Enter text to encrypt / decrypt");
        inputTextArea.setWrapText(true);
        outputTextArea.setPromptText("Result");
        outputTextArea.setWrapText(true);
        outputTextArea.setEditable(false);

        shiftSpinner = new Spinner<>(1, 26, 1);

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> action(true));
        decryptButton.setOnAction(event -> action(false));

        var controlsHBox = new HBox(
                20, shiftSpinner, encryptButton, decryptButton
        );
        counterLabel.getStyleClass().add(Styles.SUCCESS);

        var copyResultButton = new Button("Copy");
        copyResultButton.setOnAction(event -> {
            var cc = new ClipboardContent();
            cc.putString(outputTextArea.getText());
            Clipboard.getSystemClipboard().setContent(cc);
        });
        var footerHBox = new HBox(
                20, copyResultButton, counterLabel
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

        String value;

        if (encrypt) {
            value = CaesarCipherImpl.encrypt(inputTextArea.getText(), shiftSpinner.getValue());
            counterLabel.setText("Encoded %d chars".formatted(value.length()));
        } else {
            value = CaesarCipherImpl.decrypt(inputTextArea.getText(), shiftSpinner.getValue());
            counterLabel.setText("Decoded %d chars".formatted(value.length()));
        }

        outputTextArea.setText(value);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onInit() {
        inputTextArea.setText(MemCache.readString("caesar.input", ""));
        outputTextArea.setText(MemCache.readString("caesar.output", ""));
        shiftSpinner.getValueFactory().setValue(MemCache.readInteger("caesar.shift", 3));
    }

    @Override
    public void onReset() {
        MemCache.writeString("caesar.input", inputTextArea.getText());
        MemCache.writeString("caesar.output", outputTextArea.getText());
        MemCache.writeInteger("caesar.shift", shiftSpinner.getValue());
    }
}

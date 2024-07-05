package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.MemCache;
import dev.masterflomaster1.sjc.crypto.impl.VigenereCipherImpl;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class VigenereCipherPage extends SimplePage {

    public static final String NAME = "Vigenère Cipher";

    private final TextArea inputTextArea = new TextArea();
    private final TextArea outputTextArea = new TextArea();
    private final TextField keyTextField = new TextField();
    private Timeline emptyKeyAnimation;

    public VigenereCipherPage() {
        super();
        addSection("Vigenère Cipher", mainSection());

        onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Method of encrypting alphabetic text by using a series of interwoven Caesar ciphers based on " +
                        "the letters of a keyword."
        );

        inputTextArea.setPromptText("Enter text to encrypt / decrypt");
        inputTextArea.setWrapText(true);
        outputTextArea.setPromptText("Result");
        outputTextArea.setWrapText(true);
        outputTextArea.setEditable(false);

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> action(true));
        decryptButton.setOnAction(event -> action(false));

        var keyGroup = new InputGroup(keyLabel, keyTextField);
        emptyKeyAnimation = Animations.wobble(keyGroup);

        var controlsHBox = new HBox(
                20, encryptButton, decryptButton, keyGroup
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

        if (keyTextField.getText().isEmpty()) {
            emptyKeyAnimation.playFromStart();
            return;
        }

        String value;

        if (encrypt) {
            value = VigenereCipherImpl.encrypt(inputTextArea.getText(), keyTextField.getText());
            counterLabel.setText("Encoded %d chars".formatted(value.length()));
        } else {
            value = VigenereCipherImpl.decrypt(inputTextArea.getText(), keyTextField.getText());
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
        inputTextArea.setText(MemCache.readString("vigenere.input", ""));
        outputTextArea.setText(MemCache.readString("vigenere.output", ""));
        keyTextField.setText(MemCache.readString("vigenere.key", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("vigenere.input", inputTextArea.getText());
        MemCache.writeString("vigenere.output", outputTextArea.getText());
        MemCache.writeString("vigenere.key", keyTextField.getText());
    }
}

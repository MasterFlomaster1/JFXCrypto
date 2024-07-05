package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.MemCache;
import dev.masterflomaster1.sjc.crypto.impl.AtbashCipherImpl;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class AtbashPage extends SimplePage {

    public static final String NAME = "Atbash Cipher";

    private final TextArea inputTextArea = new TextArea();
    private final TextArea outputTextArea = new TextArea();

    public AtbashPage() {
        super();

        addSection("Atbash Cipher", mainSection());
        onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Originally used to encode the hebrew alphabet, Atbash is formed by mapping an alphabet to its" +
                        " reverse, so that the first letter becomes the last letter. The Atbash cipher can be seen as" +
                        " a special case of the affine cipher."
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

        var controlsHBox = new HBox(
                20, encryptButton, decryptButton
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
            value = AtbashCipherImpl.encrypt(inputTextArea.getText());
            counterLabel.setText("Encoded %d chars".formatted(value.length()));
        } else {
            value = AtbashCipherImpl.decrypt(inputTextArea.getText());
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
        inputTextArea.setText(MemCache.readString("atbash.input", ""));
        outputTextArea.setText(MemCache.readString("atbash.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("atbash.input", inputTextArea.getText());
        MemCache.writeString("atbash.output", outputTextArea.getText());
    }
}

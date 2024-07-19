package dev.masterflomaster1.jfxc.gui.page.components;

import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.classic.CaesarCipherImpl;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class CaesarPage extends SimplePage {

    public static final String NAME = "Caesar Cipher";

    private Spinner<Integer> shiftSpinner;
    private final TextArea inputTextArea = UIElementFactory.createInputTextArea("Enter text to encrypt / decrypt");
    private final TextArea outputTextArea = UIElementFactory.createOuputTextArea("Result");

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

        shiftSpinner = new Spinner<>(1, 26, 1);

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> action(true));
        decryptButton.setOnAction(event -> action(false));

        var controlsHBox = new HBox(
                20, shiftSpinner, encryptButton, decryptButton
        );

        var copyResultButton = UIElementFactory.createCopyButton(outputTextArea);
        var footerHBox = new HBox(
                20,
                copyResultButton,
                counterLabel
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

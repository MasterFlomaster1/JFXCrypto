package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.MemCache;
import dev.masterflomaster1.sjc.crypto.impl.AtbashCipherImpl;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

public final class AtbashPage extends SimplePage {

    public static final String NAME = "Atbash Cipher";

    private final TextArea inputTextArea = new TextArea();
    private final TextArea outputTextArea = new TextArea();
    private final Label counterLabel = new Label("", new FontIcon(Material2AL.LABEL));

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

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.getStyleClass().add(Styles.ACCENT);
        decryptButton.getStyleClass().add(Styles.ACCENT);
        encryptButton.setOnAction(event -> action(true));
        decryptButton.setOnAction(event -> action(false));

        var controlsHBox = new HBox(
                20, encryptButton, decryptButton
        );

        counterLabel.getStyleClass().add(Styles.SUCCESS);

        return new VBox(
                20,
                description,
                inputTextArea,
                controlsHBox,
                outputTextArea,
                counterLabel
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

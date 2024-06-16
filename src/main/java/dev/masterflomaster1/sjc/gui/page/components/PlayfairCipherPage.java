package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.MemCache;
import dev.masterflomaster1.sjc.crypto.impl.PlayfairCipherImpl;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

public final class PlayfairCipherPage extends SimplePage {

    public static final String NAME = "Playfair Cipher";

    private final TextArea inputTextArea = new TextArea();
    private final TextArea outputTextArea = new TextArea();
    private final TextField keyTextField = new TextField();
    private final Label counterLabel = new Label("", new FontIcon(Material2AL.LABEL));
    private Timeline emptyKeyAnimation;

    public PlayfairCipherPage() {
        super();
        addSection("Playfair Cipher", mainSection());

        onInit();
    }

    public Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Playfair is a polygraphic substitution cipher, which encrypts pair of letters instead of" +
                        " single letters. This makes frequency analysis much more difficult, since there are around" +
                        " 600 combinations instead of 26."
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

        var keyGroup = new InputGroup(keyLabel, keyTextField);

        emptyKeyAnimation = Animations.wobble(keyGroup);

        var controlsHBox = new HBox(
                20, encryptButton, decryptButton, keyGroup
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

        if (keyTextField.getText().isEmpty()) {
            emptyKeyAnimation.playFromStart();
            return;
        }

        String value;

        if (encrypt) {
            value = PlayfairCipherImpl.encrypt(inputTextArea.getText(), keyTextField.getText());
            counterLabel.setText("Encoded %d chars".formatted(value.length()));
        } else {
            value = PlayfairCipherImpl.decrypt(inputTextArea.getText(), keyTextField.getText());
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
        inputTextArea.setText(MemCache.readString("playfair.input", ""));
        outputTextArea.setText(MemCache.readString("playfair.output", ""));
        keyTextField.setText(MemCache.readString("playfair.key", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("playfair.input", inputTextArea.getText());
        MemCache.writeString("playfair.output", outputTextArea.getText());
        MemCache.writeString("playfair.key", keyTextField.getText());
    }

}

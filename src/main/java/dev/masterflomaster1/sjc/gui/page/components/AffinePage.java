package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.MemCache;
import dev.masterflomaster1.sjc.crypto.impl.AffineCipherImpl;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

public final class AffinePage extends SimplePage {

    public static final String NAME = "Affine Cipher";

    private final TextArea inputTextArea = new TextArea();
    private final TextArea outputTextArea = new TextArea();
    private final ComboBox<Integer> slopeComboBox = new ComboBox<>();
    private final ComboBox<Integer> interceptComboBox = new ComboBox<>();
    private final Label counterLabel = new Label("", new FontIcon(Material2AL.LABEL));

    public AffinePage() {
        super();
        addSection("Affine Cipher", section());

        onInit();
    }

    private Node section() {
        var description = BBCodeParser.createFormattedText(
                "The Affine cipher is a monoalphabetic substitution cipher, where each letter in the alphabet" +
                        " is mapped to another letter through a simple mathematical formula: [code](ax + b) mod 26[/code]." +
                        " The number 26 represents the length of the alphabet and will be different for different languages. "
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

        slopeComboBox.getItems().setAll(1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25);
        var slopeLabel = new Label("Slope (A)");
        var slopeGroup = new InputGroup(slopeLabel, slopeComboBox);

        interceptComboBox.getItems().setAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25);
        var interceptLabel = new Label("Intercept (B)");
        var interceptGroup = new InputGroup(interceptLabel, interceptComboBox);

        var controlsHBox = new HBox(
                20, encryptButton, decryptButton, slopeGroup, interceptGroup
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

        var a = slopeComboBox.getValue();
        var b = interceptComboBox.getValue();

        String value;

        if (encrypt) {
            value = AffineCipherImpl.encrypt(inputTextArea.getText(), a, b);
            counterLabel.setText("Encoded %d chars".formatted(value.length()));
        } else {
            value = AffineCipherImpl.decrypt(inputTextArea.getText(), a, b);
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
        inputTextArea.setText(MemCache.readString("affine.input", ""));
        outputTextArea.setText(MemCache.readString("affine.output", ""));
        slopeComboBox.getSelectionModel().select(MemCache.readInteger("affine.slope", 0));
        interceptComboBox.getSelectionModel().select(MemCache.readInteger("affine.intercept", 0));
    }

    @Override
    public void onReset() {
        MemCache.writeString("affine.input", inputTextArea.getText());
        MemCache.writeString("affine.output", outputTextArea.getText());
        MemCache.writeInteger("affine.slope", slopeComboBox.getItems().indexOf(slopeComboBox.getValue()));
        MemCache.writeInteger("affine.intercept", interceptComboBox.getItems().indexOf(interceptComboBox.getValue()));
    }
}

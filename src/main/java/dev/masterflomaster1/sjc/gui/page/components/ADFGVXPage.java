package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.MemCache;
import dev.masterflomaster1.sjc.crypto.impl.ADFGVXImpl;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import dev.masterflomaster1.sjc.utils.StringUtils;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class ADFGVXPage extends SimplePage {

    public static final String NAME = "ADFGVX Cipher";

    private final TextArea inputTextArea = new TextArea();
    private final TextArea outputTextArea = new TextArea();
    private final TextField keyTextField = new TextField();

    private final ToggleButton unblockedModeToggleBtn = new ToggleButton("Unblocked");
    private final ToggleButton blocksOf2ModeToggleBtn = new ToggleButton("Blocks of 2");
    private final ToggleButton blocksOf5ModeToggleBtn = new ToggleButton("Blocks of 5");

    private Timeline emptyKeyAnimation;

    public ADFGVXPage() {
        super();

        addSection("ADFGVX", section());
        onInit();
    }

    private Node section() {
        var description = BBCodeParser.createFormattedText("The ADFGX, later extended by ADFGVX, was a field " +
                "cipher used by the German Army during WWI.");

        inputTextArea.setPromptText("Enter text to encrypt / decrypt");
        inputTextArea.setWrapText(true);
        outputTextArea.setPromptText("Result");
        outputTextArea.setWrapText(true);
        outputTextArea.setEditable(false);

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> action(true));
        decryptButton.setOnAction(event -> action(false));

        var toggleGroup = new ToggleGroup();
        unblockedModeToggleBtn.getStyleClass().add(Styles.LEFT_PILL);
        blocksOf2ModeToggleBtn.getStyleClass().add(Styles.CENTER_PILL);
        blocksOf5ModeToggleBtn.getStyleClass().add(Styles.RIGHT_PILL);
        blocksOf5ModeToggleBtn.setSelected(true);

        toggleGroup.getToggles().addAll(unblockedModeToggleBtn, blocksOf2ModeToggleBtn, blocksOf5ModeToggleBtn);
        var boxOf3 = new HBox(unblockedModeToggleBtn, blocksOf2ModeToggleBtn, blocksOf5ModeToggleBtn);

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
                20, copyResultButton, boxOf3, counterLabel
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
        if (inputTextArea.getText().isEmpty() || keyTextField.getText().isEmpty())
            return;

        if (keyTextField.getText().isEmpty()) {
            emptyKeyAnimation.playFromStart();
            return;
        }

        String value;

        if (encrypt) {
            value = ADFGVXImpl.encrypt(inputTextArea.getText(), keyTextField.getText());
            counterLabel.setText("Encoded %d chars".formatted(value.length()));
        } else {
            value = ADFGVXImpl.decrypt(inputTextArea.getText(), keyTextField.getText());
            counterLabel.setText("Decoded %d chars".formatted(value.length()));
        }

        if (blocksOf2ModeToggleBtn.isSelected())
            value = StringUtils.spaceAfterN(value, 2);
        else if (blocksOf5ModeToggleBtn.isSelected())
            value = StringUtils.spaceAfterN(value, 5);

        outputTextArea.setText(value);

    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onInit() {
        inputTextArea.setText(MemCache.readString("adfgvx.input", ""));
        outputTextArea.setText(MemCache.readString("adfgvx.output", ""));
        keyTextField.setText(MemCache.readString("adfgvx.key", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("adfgvx.input", inputTextArea.getText());
        MemCache.writeString("adfgvx.output", outputTextArea.getText());
        MemCache.writeString("adfgvx.key", keyTextField.getText());
    }
}

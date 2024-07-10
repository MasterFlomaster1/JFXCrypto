package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.MemCache;
import dev.masterflomaster1.sjc.crypto.MacImpl;
import dev.masterflomaster1.sjc.crypto.SecurityUtils;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Set;

public final class HmacPage extends SimplePage {

    public static final String NAME = "HMAC";

    private final TextArea inputTextArea = new TextArea();
    private final TextField keyTextField = new TextField();
    private final TextArea outputTextArea = new TextArea();
    private final ComboBox<String> hmacComboBox = new ComboBox<>();

    private final ToggleButton hexModeToggleBtn = new ToggleButton("Hex");
    private final ToggleButton b64ModeToggleBtn = new ToggleButton("Base64");

    private Timeline emptyKeyAnimation;

    public HmacPage() {
        super();

        addSection("HMAC", mainSection());
        onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "HMAC (Hash-based Message Authentication Code) generator supporting over 40 algorithms for " +
                        "secure text authentication."
        );

        inputTextArea.setPromptText("Enter plain text to hash");
        inputTextArea.setWrapText(true);

        outputTextArea.setPromptText("Hashed output");
        outputTextArea.setWrapText(true);
        outputTextArea.setEditable(false);

        Set<String> set = SecurityUtils.getHmacs();
        hmacComboBox.getItems().setAll(set);
        hmacComboBox.getSelectionModel().select(0);

        var runButton = new Button("Run");
        runButton.setOnAction(event -> action());

        var keyGroup = new InputGroup(keyLabel, keyTextField);
        emptyKeyAnimation = Animations.wobble(keyGroup);
        var controlsHBox = new HBox(
                20, hmacComboBox, keyGroup, runButton
        );

        var toggleGroup = new ToggleGroup();
        hexModeToggleBtn.setSelected(true);
        hexModeToggleBtn.setToggleGroup(toggleGroup);
        b64ModeToggleBtn.setToggleGroup(toggleGroup);
        hexModeToggleBtn.getStyleClass().add(Styles.LEFT_PILL);
        b64ModeToggleBtn.getStyleClass().add(Styles.RIGHT_PILL);

        var outputModeHBox = new HBox(hexModeToggleBtn, b64ModeToggleBtn);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                oldValue.setSelected(true);
            }
        });

        var copyHashButton = new Button("Copy Hash");

        copyHashButton.setOnAction(event -> {
            var cc = new ClipboardContent();
            cc.putString(outputTextArea.getText());
            Clipboard.getSystemClipboard().setContent(cc);
        });
        counterLabel.getStyleClass().add(Styles.SUCCESS);

        var footerHBox = new HBox(
                20, copyHashButton, outputModeHBox, counterLabel
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

    private void action() {
        if (inputTextArea.getText().isEmpty())
            return;

        if (keyTextField.getText().isEmpty()) {
            emptyKeyAnimation.playFromStart();
            return;
        }

        var value = MacImpl.hmac(hmacComboBox.getValue(),
                keyTextField.getText().getBytes(StandardCharsets.UTF_8),
                inputTextArea.getText().getBytes(StandardCharsets.UTF_8));

        counterLabel.setText("Encoded %d bytes".formatted(value.length));

        outputTextArea.setText(output(value));
    }

    private String output(byte[] value) {
        if (hexModeToggleBtn.isSelected())
            return HexFormat.of().formatHex(value);

        if (b64ModeToggleBtn.isSelected())
            return Base64.getEncoder().encodeToString(value);

        return "";
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onInit() {
        hmacComboBox.getSelectionModel().select(MemCache.readInteger("hmac.algo", 0));
    }

    @Override
    public void onReset() {
        MemCache.writeInteger("hmac.algo", hmacComboBox.getItems().indexOf(hmacComboBox.getValue()));
    }
}

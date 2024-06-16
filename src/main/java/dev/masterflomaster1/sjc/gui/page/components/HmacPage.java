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

public final class HmacPage extends SimplePage {

    public static final String NAME = "HMAC";

    private final TextArea inputTextArea = new TextArea();
    private final TextField keyTextField = new TextField();
    private final TextArea outputTextArea = new TextArea();
    private final ComboBox<String> hmacComboBox = new ComboBox<>();
    private final Label counterLabel = new Label("", new FontIcon(Material2AL.LABEL));

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

        var copyHashButton = new Button("Copy Hash");

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

        outputTextArea.setText(HexFormat.of().formatHex(value));
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

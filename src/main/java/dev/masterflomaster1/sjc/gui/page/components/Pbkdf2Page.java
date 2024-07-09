package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.MemCache;
import dev.masterflomaster1.sjc.crypto.PbeImpl;
import dev.masterflomaster1.sjc.crypto.SaltUtils;
import dev.masterflomaster1.sjc.crypto.SecurityUtils;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Base64;
import java.util.HexFormat;
import java.util.Set;

public final class Pbkdf2Page extends SimplePage {

    public static final String NAME = "PBKDF2";

    private final TextField passwordInputField = new TextField();
    private final TextField iterationsInputTextField = new TextField();
    private final TextField keyLengthInputTextField = new TextField();
    private final TextField saltInputField = new TextField();
    private final TextArea outputTextArea = new TextArea();

    private final ComboBox<String> pbkdfComboBox = new ComboBox<>();

    private final ToggleButton hexModeToggleBtn = new ToggleButton("Hex");
    private final ToggleButton b64ModeToggleBtn = new ToggleButton("Base64");

    private Timeline emptyPasswordAnimation;
    private Timeline emptyIterationsAnimation;
    private Timeline emptyKeyLengthAnimation;
    private Timeline emptySaltAnimation;

    public Pbkdf2Page() {
        super();

        addSection("PBKDF2", mainSection());
        onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "PBKDF2 applies a pseudorandom function, such as hash-based message authentication code (HMAC)," +
                        " to the input password or passphrase along with a salt value and repeats the process many" +
                        " times to produce a derived key, which can then be used as a cryptographic key in subsequent" +
                        " operations. The added computational work makes password cracking much more difficult, and" +
                        " is known as key stretching."
        );

        var passwordInputGroup = new InputGroup(keyLabel, passwordInputField);

        var saltInputLabel = new Label("Salt");
        var saltInputButton = new Button("", new FontIcon(BootstrapIcons.SHUFFLE));
        var saltInputGroup = new InputGroup(saltInputLabel, saltInputField, saltInputButton);

        saltInputButton.setOnAction((e) -> {
            saltInputField.setText(HexFormat.of().formatHex(SaltUtils.generateSalt()));
        });

        var iterationsInputLabel = new Label("Iterations");
        var iterationsInputGroup = new InputGroup(iterationsInputLabel, iterationsInputTextField);

        var keyLengthInputLabel = new Label("Key bit-length");
        var keyLengthInputGroup = new InputGroup(keyLengthInputLabel, keyLengthInputTextField);

        Set<String> set = SecurityUtils.getPbkdfs();
        pbkdfComboBox.getItems().setAll(set);
        pbkdfComboBox.getSelectionModel().select(0);

        var runButton = new Button("Run");
        runButton.setOnAction(event -> action());

        outputTextArea.setEditable(false);
        outputTextArea.setWrapText(true);

        var container = new FlowPane(
                20, 20,
                pbkdfComboBox,
                passwordInputGroup,
                keyLengthInputGroup,
                iterationsInputGroup,
                saltInputGroup
        );

        var copyButton = new Button("Copy");
        copyButton.setOnAction(event -> {
            var cc = new ClipboardContent();
            cc.putString(outputTextArea.getText());
            Clipboard.getSystemClipboard().setContent(cc);
        });

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

        var footerHBox = new HBox(
                20, copyButton, outputModeHBox, counterLabel
        );
        footerHBox.setAlignment(Pos.CENTER_LEFT);

        emptyPasswordAnimation = Animations.wobble(passwordInputGroup);
        emptyIterationsAnimation = Animations.wobble(iterationsInputGroup);
        emptyKeyLengthAnimation = Animations.wobble(keyLengthInputGroup);
        emptySaltAnimation = Animations.wobble(saltInputGroup);

        return new VBox(
                20,
                description,
                container,
                runButton,
                outputTextArea,
                footerHBox
        );
    }

    private void action() {
        if (passwordInputField.getText().isEmpty()) {
            emptyPasswordAnimation.playFromStart();
            return;
        }

        if (iterationsInputTextField.getText().isEmpty()) {
            emptyIterationsAnimation.playFromStart();
            return;
        }

        if (keyLengthInputTextField.getText().isEmpty()) {
            emptyKeyLengthAnimation.playFromStart();
            return;
        }

        if (saltInputField.getText().isEmpty()) {
            emptySaltAnimation.playFromStart();
            return;
        }

        var algo = pbkdfComboBox.getValue();
        var pass = passwordInputField.getText().toCharArray();
        var salt = HexFormat.of().parseHex(saltInputField.getText());
        var iter = Integer.parseInt(iterationsInputTextField.getText());
        var lKey = Integer.parseInt(keyLengthInputTextField.getText());

        var future = PbeImpl.asyncHash(algo, pass, salt, iter, lKey);

        future
                .thenAccept(bytes -> {
                    outputTextArea.setText(output(bytes));
                })
                .exceptionally(ex -> {
                    System.err.println(ex.getMessage());
                    return null;
                });

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
        pbkdfComboBox.getSelectionModel().select(MemCache.readInteger("pbkdf2.algo", 0));
        passwordInputField.setText(MemCache.readString("pbkdf2.password", ""));
        keyLengthInputTextField.setText(MemCache.readString("pbkdf2.key.len", "128"));
        iterationsInputTextField.setText(MemCache.readString("pbkdf2.iterations", "10000"));
        saltInputField.setText(MemCache.readString("pbkdf2.salt", ""));
        outputTextArea.setText(MemCache.readString("pbkdf2.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeInteger("pbkdf2.algo", pbkdfComboBox.getItems().indexOf(pbkdfComboBox.getValue()));
        MemCache.writeString("pbkdf2.password", passwordInputField.getText());
        MemCache.writeString("pbkdf2.key.len", keyLengthInputTextField.getText());
        MemCache.writeString("pbkdf2.iterations", iterationsInputTextField.getText());
        MemCache.writeString("pbkdf2.salt", saltInputField.getText());
        MemCache.writeString("pbkdf2.output", outputTextArea.getText());
    }

}

package dev.masterflomaster1.jfxc.gui.page.components;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.layout.ModalBox;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import dev.masterflomaster1.jfxc.crypto.StreamCipherImpl;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Set;

public class StreamCipherTextPage extends SimplePage {

    public static final String NAME = "Stream Cipher Text";

    private final TextArea inputTextArea = UIElementFactory.createInputTextArea("Plaintext to encrypt, hex data to decrypt", 100);
    private final TextArea outputTextArea = UIElementFactory.createOuputTextArea("Result", 100);
    private final TextField keyTextField = new TextField();
    private final TextField ivTextField = new TextField();
    private final ComboBox<Integer> keyLengthComboBox = new ComboBox<>();
    private final ComboBox<String> streamCipherComboBox = new ComboBox<>();
    private final ToggleButton hexModeToggleBtn = new ToggleButton("Hex");
    private final ToggleButton b64ModeToggleBtn = new ToggleButton("Base64");

    private Timeline emptyIvAnimation;
    private Timeline emptyKeyAnimation;

    private InputGroup ivGroup;
    ModalPane modalPane = new ModalPane();

    public StreamCipherTextPage() {
        super();

        addSection("Stream Cipher Text Encryption", mainSection());
        onInit();

        onAlgorithmSelection();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Stream ciphers encrypt text by processing data in a continuous stream, ensuring high-speed " +
                        "and secure encryption. They use specific keys and initialization vectors, making them " +
                        "ideal for real-time and resource-constrained environments."
        );

        Set<String> set = SecurityUtils.getStreamCiphers();
        streamCipherComboBox.getItems().setAll(set);
        streamCipherComboBox.getSelectionModel().selectFirst();

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> action(true));
        decryptButton.setOnAction(event -> action(false));

        streamCipherComboBox.setOnAction(event -> onAlgorithmSelection());

        var keyLenLabel = new Label("Key Length");
        var keyLenGroup = new InputGroup(keyLenLabel, keyLengthComboBox);
        var keySettingsButton = new Button("", new FontIcon(BootstrapIcons.GEAR));

        var keyGroup = new InputGroup(keyLabel, keyTextField, keySettingsButton);

        getChildren().add(modalPane);

        var modal = createPasswordModal();
        modal.setPadding(new Insets(20));

        var passwordSettingsModal = new ModalBox(modalPane);
        passwordSettingsModal.addContent(modal);
        passwordSettingsModal.setMaxSize(500, 250);

        keySettingsButton.setOnAction((e) -> modalPane.show(passwordSettingsModal));

        var ivLabel = new Label("IV");
        var ivShuffleButton = new Button("", new FontIcon(BootstrapIcons.SHUFFLE));
        ivGroup = new InputGroup(ivLabel, ivTextField, ivShuffleButton);
        Tooltip ivTooltip = new Tooltip("Initialization Vector (IV). Required for certain encryption modes" +
                " (e.g., CBC). Must match block size.");
        Tooltip.install(ivGroup, ivTooltip);

        ivShuffleButton.setOnAction(event -> onIvShuffleButtonPressed());

        var cipherSettingsContainer = new FlowPane(
                20, 20,
                streamCipherComboBox,
                keyLenGroup,
                ivGroup
        );

        var keySettingsContainer = new FlowPane(
                20, 20,
                keyGroup
        );

        var controlsHBox2 = new HBox(
                20, encryptButton, decryptButton
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

        var copyResultButton = UIElementFactory.createCopyButton(outputTextArea);

        var footerHBox = new HBox(
                20, copyResultButton, outputModeHBox, counterLabel
        );
        footerHBox.setAlignment(Pos.CENTER_LEFT);

        emptyIvAnimation = Animations.wobble(ivGroup);
        emptyKeyAnimation = Animations.wobble(keyGroup);

        return new VBox(
                20,
                description,
                inputTextArea,
                cipherSettingsContainer,
                keySettingsContainer,
                controlsHBox2,
                outputTextArea,
                footerHBox
        );
    }

    private VBox createPasswordModal() {
        var header = new Label("Generate password based key with PBKDF2");
        header.getStyleClass().add(Styles.TITLE_4);

        var passwordTextField = new TextField();
        var passwordLabel = new Label("Password");
        var passwordGroup  = new InputGroup(passwordLabel, passwordTextField);

        var saltTextField = new TextField();
        var saltLabel = new Label("Salt");
        var saltShuffleButton = new Button("", new FontIcon(BootstrapIcons.SHUFFLE));
        var saltGroup = new InputGroup(saltLabel, saltTextField, saltShuffleButton);

        saltShuffleButton.setOnAction((e) -> {
            saltTextField.setText(HexFormat.of().formatHex(SecurityUtils.generateSalt()));
        });

        var generateButton = new Button("Generate");

        generateButton.setOnAction(event -> {
            var key = SecurityUtils.generatePasswordBasedKey(
                    passwordTextField.getText().toCharArray(),
                    keyLengthComboBox.getValue(),
                    HexFormat.of().parseHex(saltTextField.getText())
            );

            keyTextField.setText(HexFormat.of().formatHex(key));
            modalPane.hide();
        });

        return new VBox(
                20,
                header,
                saltGroup,
                passwordGroup,
                generateButton
        );
    }

    private void onAlgorithmSelection() {
        var algo = streamCipherComboBox.getValue();

        ivGroup.setDisable(StreamCipherImpl.getCorrespondingIvLengthBits(algo).isEmpty());
        keyLengthComboBox.getItems().setAll(StreamCipherImpl.getCorrespondingKeyLengths(algo));
        keyLengthComboBox.getSelectionModel().selectFirst();
    }

    private void onIvShuffleButtonPressed() {
        var ivKeyLenOptional = StreamCipherImpl.getCorrespondingIvLengthBits(streamCipherComboBox.getValue());

        if (ivKeyLenOptional.isEmpty())
            return;

        var value = SecurityUtils.generateIV(ivKeyLenOptional.get().get(0));

        ivTextField.setText(HexFormat.of().formatHex(value));
    }

    private void action(boolean encrypt) {
        if (inputTextArea.getText().isEmpty())
            return;

        var algo = streamCipherComboBox.getValue();

        if (!ivTextField.isDisabled() && ivTextField.getText().isEmpty()) {
            emptyIvAnimation.playFromStart();
            return;
        }

        if (keyTextField.getText().isEmpty()) {
            emptyKeyAnimation.playFromStart();
            return;
        }

        var text = inputTextArea.getText().getBytes(StandardCharsets.UTF_8);
        byte[] key = HexFormat.of().parseHex(keyTextField.getText());
        byte[] value;

        byte[] iv = null;

        if (StreamCipherImpl.getCorrespondingIvLengthBits(algo).isPresent())
            iv = HexFormat.of().parseHex(ivTextField.getText());

        if (encrypt) {
            value = StreamCipherImpl.encrypt(algo, iv, text, key);
            counterLabel.setText("Encoded %d bytes".formatted(value.length));
            outputTextArea.setText(output(value));
        } else {
            var input = HexFormat.of().parseHex(inputTextArea.getText());

            value = StreamCipherImpl.decrypt(algo, iv, input, key);
            counterLabel.setText("Decoded %d bytes".formatted(value.length));
            outputTextArea.setText(new String(value));
        }

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
        super.onInit();
    }

    @Override
    public void onReset() {
        super.onReset();
    }
}

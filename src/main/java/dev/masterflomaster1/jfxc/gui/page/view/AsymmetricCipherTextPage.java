package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.AsymmetricCipherTextViewModel;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class AsymmetricCipherTextPage extends SimplePage {

    public static final String NAME = "Asymmetric Cipher Text";

    private final TextArea inputTextArea = UIElementFactory.createInputTextArea("Plaintext to encrypt, hex data to decrypt", 100);
    private final TextArea outputTextArea = UIElementFactory.createOuputTextArea("Result", 100);
    private final ComboBox<String> asymmetricCipherComboBox = new ComboBox<>();
    private final TextField publicKeyTextField = new TextField();
    private final TextField privateKeyTextField = new TextField();
    private final ToggleButton hexModeToggleBtn = new ToggleButton("Hex");
    private final ToggleButton b64ModeToggleBtn = new ToggleButton("Base64");

    private ToggleGroup toggleGroup;
    private final AsymmetricCipherTextViewModel viewModel = new AsymmetricCipherTextViewModel();

    public AsymmetricCipherTextPage() {
        super();

        addSection("Asymmetric Cipher Text Encryption", mainSection());
        bindComponents();

        viewModel.onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Encrypt text using various asymmetric cipher algorithms with configurable key generation"
        );

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> viewModel.action(true));
        decryptButton.setOnAction(event -> viewModel.action(false));

        var generateKeysButton = new Button("Generate Keys");
        generateKeysButton.setOnAction(viewModel::onGenerateKeysAction);

        publicKeyTextField.setPrefWidth(450);
        var pubKeyLabel = new Label("Public Key");
        var pubKeyGroup = new InputGroup(pubKeyLabel, publicKeyTextField);

        privateKeyTextField.setPrefWidth(450);
        var prtKeyLabel = new Label("Private Key");
        var prtKeyGroup = new InputGroup(prtKeyLabel, privateKeyTextField);

        var controlsHBox = new HBox(
                20,
                encryptButton,
                decryptButton,
                generateKeysButton
        );

        toggleGroup = new ToggleGroup();
        hexModeToggleBtn.setToggleGroup(toggleGroup);
        b64ModeToggleBtn.setToggleGroup(toggleGroup);
        hexModeToggleBtn.getStyleClass().add(Styles.LEFT_PILL);
        b64ModeToggleBtn.getStyleClass().add(Styles.RIGHT_PILL);

        var outputModeHBox = new HBox(hexModeToggleBtn, b64ModeToggleBtn);

        var copyButton = UIElementFactory.createCopyButton(outputTextArea);
        var footerHBox = new HBox(
                20,
                copyButton,
                outputModeHBox,
                counterLabel
        );
        footerHBox.setAlignment(Pos.CENTER_LEFT);

        return new VBox(
                20,
                description,
                inputTextArea,
                asymmetricCipherComboBox,
                pubKeyGroup,
                prtKeyGroup,
                controlsHBox,
                outputTextArea,
                footerHBox
        );
    }

    private void bindComponents() {
        inputTextArea.textProperty().bindBidirectional(viewModel.inputTextProperty());
        outputTextArea.textProperty().bindBidirectional(viewModel.outputTextProperty());
        publicKeyTextField.textProperty().bindBidirectional(viewModel.publicKeyTextProperty());
        privateKeyTextField.textProperty().bindBidirectional(viewModel.privateKeyTextProperty());
        counterLabel.textProperty().bindBidirectional(viewModel.counterTextProperty());

        asymmetricCipherComboBox.valueProperty().bindBidirectional(viewModel.asymmetricCipherComboBoxProperty());
        Bindings.bindContent(asymmetricCipherComboBox.getItems(), viewModel.getAsymmetricCipherAlgorithmsList());

        asymmetricCipherComboBox.getSelectionModel().selectFirst();

        hexModeToggleBtn.selectedProperty().bindBidirectional(viewModel.hexModeToggleButtonProperty());
        b64ModeToggleBtn.selectedProperty().bindBidirectional(viewModel.b64ModeToggleButtonProperty());
        toggleGroup.selectedToggleProperty().addListener(viewModel::onToggleChanged);
        hexModeToggleBtn.setSelected(true);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onReset() {
        viewModel.onReset();
    }

}

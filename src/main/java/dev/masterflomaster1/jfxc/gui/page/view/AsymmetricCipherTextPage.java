package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.JFXCrypto;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.AsymmetricCipherTextViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;

public final class AsymmetricCipherTextPage extends SimplePage {

    public static final String NAME = "Asymmetric Cipher Text";

    private final TextArea inputTextArea = UIElementFactory.createInputTextArea("Plaintext to encrypt, hex data to decrypt", 100);
    private final TextArea outputTextArea = UIElementFactory.createOuputTextArea("Result", 100);
    private final ComboBox<String> asymmetricCipherComboBox = new ComboBox<>();
    private final ComboBox<String> keyOptionsComboBox = new ComboBox<>();
    private final TextArea publicKeyTextArea = new TextArea();
    private final TextArea privateKeyTextArea = new TextArea();
    private final ToggleButton hexModeToggleBtn = new ToggleButton("Hex");
    private final ToggleButton b64ModeToggleBtn = new ToggleButton("Base64");

    private Button publicKeyExportButton;
    private Button privateKeyExportButton;

    private ToggleGroup toggleGroup;
    private final AsymmetricCipherTextViewModel viewModel = new AsymmetricCipherTextViewModel();

    public AsymmetricCipherTextPage() {
        super();

        addSection("Asymmetric Cipher Text Encryption", mainSection());
        bindComponents();

        viewModel.onAlgorithmSelection(null);
        viewModel.onInit();
        viewModel.onAlgorithmSelection(null);
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

        var keyOptionLabel = new Label("Key");
        var keyOptionGroup = new InputGroup(keyOptionLabel, keyOptionsComboBox);

        var cipherSettingsHBox = new HBox(
                20,
                asymmetricCipherComboBox,
                keyOptionGroup
        );

        var controlsHBox = new HBox(
                20,
                encryptButton,
                decryptButton,
                generateKeysButton
        );

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label label1 = new Label("Public Key");
        Label label2 = new Label("Private Key");
        label1.setAlignment(Pos.CENTER);
        label2.setAlignment(Pos.CENTER);

        publicKeyTextArea.setWrapText(true);
        privateKeyTextArea.setWrapText(true);

        Button button1 = new Button("Import", new FontIcon(BootstrapIcons.FILE_EARMARK_LOCK2));
        publicKeyExportButton = new Button("Export", new FontIcon(BootstrapIcons.FILE_EARMARK_LOCK2));
        Button button3 = new Button("Import", new FontIcon(BootstrapIcons.FILE_EARMARK_LOCK2));
        privateKeyExportButton = new Button("Export", new FontIcon(BootstrapIcons.FILE_EARMARK_LOCK2));

        FileChooser fileChooser = new FileChooser();
        button1.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(JFXCrypto.getStage());

            if (file == null)
                return;

            viewModel.onPublicKeyImport(file);
        });

        publicKeyExportButton.setOnAction(event -> {
            FileChooser fileChooser1 = new FileChooser();
            var extFilter = new FileChooser.ExtensionFilter("PEM files (*.pem)", "*.pem");
            fileChooser1.getExtensionFilters().add(extFilter);
            fileChooser1.setSelectedExtensionFilter(extFilter);
            fileChooser1.setInitialFileName("public");
            File file = fileChooser1.showSaveDialog(JFXCrypto.getStage());

            if (file == null)
                return;

            viewModel.onPublicKeyExport(file);
        });

        button3.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(JFXCrypto.getStage());

            if (file == null)
                return;

            viewModel.onPrivateKeyImport(file);
        });

        privateKeyExportButton.setOnAction(event -> {
            FileChooser fileChooser1 = new FileChooser();
            var extFilter = new FileChooser.ExtensionFilter("PEM files (*.pem)", "*.pem");
            fileChooser1.getExtensionFilters().add(extFilter);
            fileChooser1.setSelectedExtensionFilter(extFilter);
            fileChooser1.setInitialFileName("private");
            File file = fileChooser1.showSaveDialog(JFXCrypto.getStage());

            if (file == null)
                return;

            viewModel.onPrivateKeyExport(file);
        });

        HBox buttonBox1 = new HBox(5, button1, publicKeyExportButton);
        HBox buttonBox2 = new HBox(5, button3, privateKeyExportButton);
        buttonBox1.setAlignment(Pos.CENTER);
        buttonBox2.setAlignment(Pos.CENTER);

        grid.add(label1, 0, 0);
        grid.add(label2, 1, 0);
        grid.add(publicKeyTextArea, 0, 1);
        grid.add(privateKeyTextArea, 1, 1);
        grid.add(buttonBox1, 0, 2);
        grid.add(buttonBox2, 1, 2);

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
                cipherSettingsHBox,
                grid,
                controlsHBox,
                outputTextArea,
                footerHBox
        );
    }

    private void onPublicKeyChange(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        publicKeyExportButton.setDisable(publicKeyTextArea.getText().isEmpty());
    }

    private void onPrivateKeyChange(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        privateKeyExportButton.setDisable(privateKeyTextArea.getText().isEmpty());
    }

    private void bindComponents() {
        inputTextArea.textProperty().bindBidirectional(viewModel.inputTextProperty());
        outputTextArea.textProperty().bindBidirectional(viewModel.outputTextProperty());
        publicKeyTextArea.textProperty().bindBidirectional(viewModel.publicKeyTextProperty());
        privateKeyTextArea.textProperty().bindBidirectional(viewModel.privateKeyTextProperty());
        counterLabel.textProperty().bindBidirectional(viewModel.counterTextProperty());

        asymmetricCipherComboBox.valueProperty().bindBidirectional(viewModel.asymmetricCipherComboBoxProperty());
        Bindings.bindContent(asymmetricCipherComboBox.getItems(), viewModel.getAsymmetricCipherAlgorithmsList());

        keyOptionsComboBox.valueProperty().bindBidirectional(viewModel.keyOptionsComboBoxProperty());
        Bindings.bindContent(keyOptionsComboBox.getItems(), viewModel.getKeyOptionsList());

        asymmetricCipherComboBox.getSelectionModel().selectFirst();
        keyOptionsComboBox.getSelectionModel().selectFirst();

        hexModeToggleBtn.selectedProperty().bindBidirectional(viewModel.hexModeToggleButtonProperty());
        b64ModeToggleBtn.selectedProperty().bindBidirectional(viewModel.b64ModeToggleButtonProperty());
        toggleGroup.selectedToggleProperty().addListener(viewModel::onToggleChanged);
        hexModeToggleBtn.setSelected(true);

        publicKeyTextArea.textProperty().addListener(this::onPublicKeyChange);
        privateKeyTextArea.textProperty().addListener(this::onPrivateKeyChange);
        publicKeyTextArea.setText("");
        privateKeyTextArea.setText("");
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

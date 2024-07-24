package dev.masterflomaster1.jfxc.gui.page.components;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.layout.ModalBox;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.StreamCipherTextViewModel;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
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

    private ToggleGroup toggleGroup;
    private InputGroup ivGroup;
    ModalPane modalPane = new ModalPane();

    private final StreamCipherTextViewModel viewModel = new StreamCipherTextViewModel();

    public StreamCipherTextPage() {
        super();

        addSection("Stream Cipher Text Encryption", mainSection());
        bindComponents();
        onInit();

        onAlgorithmSelection();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Stream ciphers encrypt text by processing data in a continuous stream, ensuring high-speed " +
                        "and secure encryption. They use specific keys and initialization vectors, making them " +
                        "ideal for real-time and resource-constrained environments."
        );

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> viewModel.action(true));
        decryptButton.setOnAction(event -> viewModel.action(false));

        streamCipherComboBox.setOnAction(event -> onAlgorithmSelection());

        var keyLenLabel = new Label("Key Length");
        var keyLenGroup = new InputGroup(keyLenLabel, keyLengthComboBox);
        var keySettingsButton = new Button("", new FontIcon(BootstrapIcons.GEAR));

        var keyGroup = new InputGroup(keyLabel, keyTextField, keySettingsButton);

        getChildren().add(modalPane);

        var modal = UIElementFactory.createPasswordSettingsModal(keyLengthComboBox, keyTextField, modalPane);
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

        ivShuffleButton.setOnAction(viewModel::onIvShuffleAction);

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

        toggleGroup = new ToggleGroup();
        hexModeToggleBtn.setToggleGroup(toggleGroup);
        b64ModeToggleBtn.setToggleGroup(toggleGroup);
        hexModeToggleBtn.getStyleClass().add(Styles.LEFT_PILL);
        b64ModeToggleBtn.getStyleClass().add(Styles.RIGHT_PILL);

        var outputModeHBox = new HBox(hexModeToggleBtn, b64ModeToggleBtn);

        var copyResultButton = UIElementFactory.createCopyButton(outputTextArea);

        var footerHBox = new HBox(
                20,
                copyResultButton,
                outputModeHBox,
                counterLabel
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

    private void bindComponents() {
        inputTextArea.textProperty().bindBidirectional(viewModel.inputTextProperty());
        outputTextArea.textProperty().bindBidirectional(viewModel.outputTextProperty());
        keyTextField.textProperty().bindBidirectional(viewModel.keyTextProperty());
        ivTextField.textProperty().bindBidirectional(viewModel.ivTextProperty());
        counterLabel.textProperty().bind(viewModel.counterTextProperty());

        streamCipherComboBox.valueProperty().bindBidirectional(viewModel.streamCipherComboBoxProperty());
        Bindings.bindContent(streamCipherComboBox.getItems(), viewModel.getStreamCipherAlgorithmsList());

        keyLengthComboBox.valueProperty().bindBidirectional(viewModel.keyLengthComboBoxProperty());
        Bindings.bindContent(keyLengthComboBox.getItems(), viewModel.getKeyLengthList());

        viewModel.setEmptyIvAnimation(emptyIvAnimation);
        viewModel.setEmptyKeyAnimation(emptyKeyAnimation);

        hexModeToggleBtn.selectedProperty().bindBidirectional(viewModel.hexModeToggleButtonPropertyProperty());
        b64ModeToggleBtn.selectedProperty().bindBidirectional(viewModel.b64ModeToggleButtonPropertyProperty());
        toggleGroup.selectedToggleProperty().addListener(viewModel::onToggleChanged);
        hexModeToggleBtn.setSelected(true);
    }

    private void onAlgorithmSelection() {
        ivGroup.setDisable(viewModel.isNonIvAlgorithmSelected());
        viewModel.onAlgorithmSelection(null);
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

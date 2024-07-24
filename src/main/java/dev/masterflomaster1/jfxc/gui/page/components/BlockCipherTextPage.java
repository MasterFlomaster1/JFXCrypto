package dev.masterflomaster1.jfxc.gui.page.components;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.layout.ModalBox;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.BlockCipherTextViewModel;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
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

public final class BlockCipherTextPage extends SimplePage {

    public static final String NAME = "Block Cipher Text";

    private final TextArea inputTextArea = UIElementFactory.createInputTextArea("Plaintext to encrypt, hex data to decrypt", 100);
    private final TextArea outputTextArea = UIElementFactory.createOuputTextArea("Result", 100);
    private final TextField keyTextField = new TextField();
    private final TextField ivTextField = new TextField();
    private final ComboBox<String> blockCipherComboBox = new ComboBox<>();
    private final ComboBox<String> modesComboBox = new ComboBox<>();
    private final ComboBox<String> paddingsComboBox = new ComboBox<>();
    private final ComboBox<Integer> keyLengthComboBox = new ComboBox<>();
    private final ToggleButton hexModeToggleBtn = new ToggleButton("Hex");
    private final ToggleButton b64ModeToggleBtn = new ToggleButton("Base64");

    private Timeline emptyIvAnimation;
    private Timeline emptyKeyAnimation;

    private ToggleGroup toggleGroup;
    private InputGroup ivGroup;
    ModalPane modalPane = new ModalPane();

    private final BlockCipherTextViewModel viewModel = new BlockCipherTextViewModel();

    public BlockCipherTextPage() {
        super();

        addSection("Block Cipher Text Encryption", mainSection());
        bindComponents();
        onInit();

        viewModel.onAlgorithmSelection(null);
        onModeSelection(null);
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Encrypt text using various block cipher algorithms with configurable key generation, " +
                        "encryption modes, padding, and IV settings."
        );

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> viewModel.action(true));
        decryptButton.setOnAction(event -> viewModel.action(false));

        blockCipherComboBox.setOnAction(viewModel::onAlgorithmSelection);

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

        var paddingsLabel = new Label("Padding");
        var paddingGroup = new InputGroup(paddingsLabel, paddingsComboBox);

        var modeLabel = new Label("Mode");
        var modeGroup = new InputGroup(modeLabel, modesComboBox);
        modesComboBox.setOnAction(this::onModeSelection);

        var ivLabel = new Label("IV");
        var ivShuffleButton = new Button("", new FontIcon(BootstrapIcons.SHUFFLE));
        ivGroup = new InputGroup(ivLabel, ivTextField, ivShuffleButton);
        Tooltip ivTooltip = new Tooltip("Initialization Vector (IV). Required for certain encryption modes" +
                " (e.g., CBC). Must match block size.");
        Tooltip.install(ivGroup, ivTooltip);

        ivShuffleButton.setOnAction(viewModel::onIvShuffleAction);

        var cipherSettingsContainer = new FlowPane(
                20, 20,
                blockCipherComboBox,
                keyLenGroup,
                modeGroup,
                paddingGroup,
                ivGroup
        );

        var keySettingsContainer = new FlowPane(
                20, 20,
                keyGroup
        );

        var controlsHBox2 = new HBox(
                20,
                encryptButton,
                decryptButton
        );

        toggleGroup = new ToggleGroup();
        hexModeToggleBtn.setToggleGroup(toggleGroup);
        b64ModeToggleBtn.setToggleGroup(toggleGroup);
        hexModeToggleBtn.getStyleClass().add(Styles.LEFT_PILL);
        b64ModeToggleBtn.getStyleClass().add(Styles.RIGHT_PILL);

        var outputModeHBox = new HBox(hexModeToggleBtn, b64ModeToggleBtn);

        var copyHashButton = UIElementFactory.createCopyButton(outputTextArea);
        var footerHBox = new HBox(
                20, copyHashButton, outputModeHBox, counterLabel
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

        blockCipherComboBox.valueProperty().bindBidirectional(viewModel.blockCipherComboBoxProperty());
        Bindings.bindContent(blockCipherComboBox.getItems(), viewModel.getBlockCipherAlgorithmsList());

        modesComboBox.valueProperty().bindBidirectional(viewModel.modesComboBoxProperty());
        Bindings.bindContent(modesComboBox.getItems(), viewModel.getModesList());

        paddingsComboBox.valueProperty().bindBidirectional(viewModel.paddingsComboBoxProperty());
        Bindings.bindContent(paddingsComboBox.getItems(), viewModel.getPaddingsList());

        keyLengthComboBox.valueProperty().bindBidirectional(viewModel.keyLengthComboBoxProperty());
        Bindings.bindContent(keyLengthComboBox.getItems(), viewModel.getKeyLengthList());

        viewModel.setEmptyIvAnimation(emptyIvAnimation);
        viewModel.setEmptyKeyAnimation(emptyKeyAnimation);

        blockCipherComboBox.getSelectionModel().selectFirst();
        modesComboBox.getSelectionModel().selectFirst();
        paddingsComboBox.getSelectionModel().selectFirst();
        keyLengthComboBox.getSelectionModel().selectFirst();

        hexModeToggleBtn.selectedProperty().bindBidirectional(viewModel.hexModeToggleButtonPropertyProperty());
        b64ModeToggleBtn.selectedProperty().bindBidirectional(viewModel.b64ModeToggleButtonPropertyProperty());
        toggleGroup.selectedToggleProperty().addListener(viewModel::onToggleChanged);
        hexModeToggleBtn.setSelected(true);
    }

    /**
     * Disable IV input group if ECB cipher mode is selected
     */
    private void onModeSelection(ActionEvent e) {
        ivGroup.setDisable(viewModel.isNonIvModeSelected());
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onInit() {
        inputTextArea.setText(MemCache.readString("block.input", ""));
        blockCipherComboBox.getSelectionModel().select(MemCache.readInteger("block.algo", 0));
        keyLengthComboBox.getSelectionModel().select(MemCache.readInteger("block.key.len", 0));
        modesComboBox.getSelectionModel().select(MemCache.readInteger("block.mode", 0));
        paddingsComboBox.getSelectionModel().select(MemCache.readInteger("block.padding", 0));
        ivTextField.setText(MemCache.readString("block.iv", ""));
        outputTextArea.setText(MemCache.readString("block.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("block.input", inputTextArea.getText());
        MemCache.writeInteger("block.algo", blockCipherComboBox.getItems().indexOf(blockCipherComboBox.getValue()));
        MemCache.writeInteger("block.key.len", keyLengthComboBox.getItems().indexOf(keyLengthComboBox.getValue()));
        MemCache.writeInteger("block.mode", modesComboBox.getItems().indexOf(modesComboBox.getValue()));
        MemCache.writeInteger("block.padding", paddingsComboBox.getItems().indexOf(paddingsComboBox.getValue()));
        MemCache.writeString("block.iv", ivTextField.getText());
        MemCache.writeString("block.output", outputTextArea.getText());
    }
}

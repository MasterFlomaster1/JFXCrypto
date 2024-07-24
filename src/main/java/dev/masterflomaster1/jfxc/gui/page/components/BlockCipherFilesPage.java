package dev.masterflomaster1.jfxc.gui.page.components;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.layout.ModalBox;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.JFXCrypto;
import dev.masterflomaster1.jfxc.crypto.BlockCipherImpl;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.BlockCipherFilesViewModel;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.util.HexFormat;

public class BlockCipherFilesPage extends SimplePage {

    public static final String NAME = "Block Cipher Files";

    private final TextField keyTextField = new TextField();
    private final TextField ivTextField = new TextField();
    private final ComboBox<String> blockCipherComboBox = new ComboBox<>();
    private final ComboBox<String> modesComboBox = new ComboBox<>();
    private final ComboBox<String> paddingsComboBox = new ComboBox<>();
    private final ComboBox<Integer> keyLengthComboBox = new ComboBox<>();

    private Timeline emptyIvAnimation;
    private Timeline emptyTargetFileAnimation;
    private Timeline emptyDestinationFileAnimation;

    private InputGroup ivGroup;

    ModalPane modalPane = new ModalPane();

    private final BlockCipherFilesViewModel viewModel = new BlockCipherFilesViewModel();

    public BlockCipherFilesPage() {
        super();

        addSection("Block Cipher File Encryption", mainSection());
        bindComponents();
        onInit();

        viewModel.onAlgorithmSelection(null);
        onModeSelection(null);
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Encrypt files using various block cipher algorithms with configurable key generation, " +
                        "encryption modes, padding, and IV settings."
        );

        FileChooser fileChooser = new FileChooser();

        var targetFileInputLabel = new Label("", new FontIcon(BootstrapIcons.FILE_EARMARK));
        var targetFileInputTextField = new TextField();
        targetFileInputTextField.setMinWidth(534);
        targetFileInputTextField.setPromptText("Select file to encrypt or decrypt");
        var targetFileInputBrowseButton = new Button("Browse");
        targetFileInputBrowseButton.setOnAction(event -> {
            File targetFile = fileChooser.showOpenDialog(JFXCrypto.getStage());

            if (targetFile == null)
                return;

            viewModel.setTargetFile(targetFile);
            targetFileInputTextField.setText(targetFile.getAbsolutePath());
        });
        var targetFileInputGroup = new InputGroup(
                targetFileInputLabel,
                targetFileInputTextField,
                targetFileInputBrowseButton
        );

        var destinationFileInputLabel = new Label("", new FontIcon(BootstrapIcons.FILE_EARMARK));
        var destinationFileInputTextField = new TextField();
        destinationFileInputTextField.setMinWidth(534);
        destinationFileInputTextField.setPromptText("Save result file as..");
        var destinationFileInputBrowseButton = new Button("Browse");
        destinationFileInputBrowseButton.setOnAction(event -> {
            File destinationFile = fileChooser.showOpenDialog(JFXCrypto.getStage());

            if (destinationFile == null)
                return;

            viewModel.setDestinationFile(destinationFile);
            destinationFileInputTextField.setText(destinationFile.getAbsolutePath());
        });
        var destinationFileInputGroup = new InputGroup(
                destinationFileInputLabel,
                destinationFileInputTextField,
                destinationFileInputBrowseButton
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

        var modal = UIElementFactory.createPasswordSettingsModal(keyLengthComboBox.getValue(), event -> {
            keyTextField.setText(HexFormat.of().formatHex(key));
            modalPane.hide();
        });
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

        var controlsHBox2 = new HBox(
                20,
                encryptButton,
                decryptButton
        );

        var footerHBox = new HBox(
                20,
                counterLabel
        );
        footerHBox.setAlignment(Pos.CENTER_LEFT);

        emptyIvAnimation = Animations.wobble(ivGroup);
        emptyTargetFileAnimation = Animations.wobble(targetFileInputGroup);
        emptyDestinationFileAnimation = Animations.wobble(destinationFileInputGroup);

        return new VBox(
                20,
                description,
                targetFileInputGroup,
                destinationFileInputGroup,
                cipherSettingsContainer,
                keyGroup,
                controlsHBox2,
                footerHBox
        );
    }

    private void bindComponents() {
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
        viewModel.setEmptyTargetFileAnimation(emptyTargetFileAnimation);
        viewModel.setEmptyDestinationFileAnimation(emptyDestinationFileAnimation);

        blockCipherComboBox.getSelectionModel().selectFirst();
        modesComboBox.getSelectionModel().selectFirst();
        paddingsComboBox.getSelectionModel().selectFirst();
        keyLengthComboBox.getSelectionModel().selectFirst();
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

    /**
     * Disable iv input group if ECB selected
     */
    private void onModeSelection(ActionEvent e) {
        var mode = modesComboBox.getValue();

        ivGroup.setDisable(BlockCipherImpl.Mode.fromString(mode) == BlockCipherImpl.Mode.ECB);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onInit() {
        blockCipherComboBox.getSelectionModel().select(MemCache.readInteger("block.files.algo", 0));
        keyLengthComboBox.getSelectionModel().select(MemCache.readInteger("block.files.key.len", 0));
        modesComboBox.getSelectionModel().select(MemCache.readInteger("block.files.mode", 0));
        paddingsComboBox.getSelectionModel().select(MemCache.readInteger("block.files.padding", 0));
        ivTextField.setText(MemCache.readString("block.files.iv", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeInteger("block.files.algo", blockCipherComboBox.getItems().indexOf(blockCipherComboBox.getValue()));
        MemCache.writeInteger("block.files.key.len", keyLengthComboBox.getItems().indexOf(keyLengthComboBox.getValue()));
        MemCache.writeInteger("block.files.mode", modesComboBox.getItems().indexOf(modesComboBox.getValue()));
        MemCache.writeInteger("block.files.padding", paddingsComboBox.getItems().indexOf(paddingsComboBox.getValue()));
        MemCache.writeString("block.files.iv", ivTextField.getText());
    }

}

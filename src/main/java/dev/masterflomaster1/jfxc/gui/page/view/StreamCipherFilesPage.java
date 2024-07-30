package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.layout.ModalBox;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.JFXCrypto;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.StreamCipherFilesViewModel;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
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

public class StreamCipherFilesPage extends SimplePage {

    public static final String NAME = "Stream Cipher Files";

    private final TextField keyTextField = new TextField();
    private final TextField ivTextField = new TextField();
    private final ComboBox<Integer> keyLengthComboBox = new ComboBox<>();
    private final ComboBox<String> streamCipherComboBox = new ComboBox<>();

    private Timeline emptyIvAnimation;
    private Timeline emptyTargetFileAnimation;
    private Timeline emptyDestinationFileAnimation;

    private InputGroup ivGroup;
    ModalPane modalPane = new ModalPane();

    private final StreamCipherFilesViewModel viewModel = new StreamCipherFilesViewModel();

    public StreamCipherFilesPage() {
        super();

        addSection("Stream Cipher File Encryption", mainSection());
        bindComponents();

        onAlgorithmSelection();
        viewModel.onInit();
        onAlgorithmSelection();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Encrypt files using various stream cipher algorithms with configurable key generation."
        );

        var targetFileInputLabel = new Label("", new FontIcon(BootstrapIcons.FILE_EARMARK));
        var targetFileInputTextField = new TextField();
        targetFileInputTextField.setMinWidth(534);
        targetFileInputTextField.setPromptText("Select file to encrypt or decrypt");
        var targetFileInputBrowseButton = new Button("Browse");
        targetFileInputBrowseButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
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
            FileChooser fileChooser1 = new FileChooser();
            fileChooser1.setInitialFileName("encrypted.a");
            File destinationFile = fileChooser1.showSaveDialog(JFXCrypto.getStage());

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

        streamCipherComboBox.setOnAction(event -> onAlgorithmSelection());

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> viewModel.action(true));
        decryptButton.setOnAction(event -> viewModel.action(false));

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

    private void onAlgorithmSelection() {
        ivGroup.setDisable(viewModel.isNonIvAlgorithmSelected());
        viewModel.onAlgorithmSelection(null);
    }

    private void bindComponents() {
        keyTextField.textProperty().bindBidirectional(viewModel.keyTextProperty());
        ivTextField.textProperty().bindBidirectional(viewModel.ivTextProperty());
        counterLabel.textProperty().bind(viewModel.counterTextProperty());

        streamCipherComboBox.valueProperty().bindBidirectional(viewModel.streamCipherComboBoxProperty());
        Bindings.bindContent(streamCipherComboBox.getItems(), viewModel.getStreamCipherAlgorithmsList());

        keyLengthComboBox.valueProperty().bindBidirectional(viewModel.keyLengthComboBoxProperty());
        Bindings.bindContent(keyLengthComboBox.getItems(), viewModel.getKeyLengthList());

        viewModel.setEmptyIvAnimation(emptyIvAnimation);
        viewModel.setEmptyTargetFileAnimation(emptyTargetFileAnimation);
        viewModel.setEmptyDestinationFileAnimation(emptyDestinationFileAnimation);
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

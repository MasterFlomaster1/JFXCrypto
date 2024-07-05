package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.MemCache;
import dev.masterflomaster1.sjc.SJC;
import dev.masterflomaster1.sjc.crypto.BlockCipherImpl;
import dev.masterflomaster1.sjc.crypto.SecurityUtils;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.util.HexFormat;
import java.util.Set;

public class BlockCipherFilesPage extends SimplePage {

    public static final String NAME = "Block Cipher File Encryption";

    private final TextField keyTextField = new TextField();
    private final TextField ivTextField = new TextField();
    private final TextField passwordTextField = new TextField();
    private final ComboBox<String> blockCipherComboBox = new ComboBox<>();
    private final ComboBox<String> modesComboBox = new ComboBox<>();
    private final ComboBox<String> paddingsComboBox = new ComboBox<>();
    private final ComboBox<Integer> keyLengthComboBox = new ComboBox<>();

    private Timeline emptyKeyAnimation;
    private Timeline emptyIvAnimation;
    private Timeline emptyTargetFileAnimation;
    private Timeline emptyDestinationFileAnimation;

    private InputGroup ivGroup;

    private File targetFile;
    private File destinationFile;

    public BlockCipherFilesPage() {
        super();

        addSection(NAME, mainSection());
        onInit();

        onAlgorithmSelection();
        onModeSelection();
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
            targetFile = fileChooser.showOpenDialog(SJC.getStage());

            if (targetFile == null)
                return;

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
            destinationFile = fileChooser.showOpenDialog(SJC.getStage());

            if (destinationFile == null)
                return;

            destinationFileInputTextField.setText(destinationFile.getAbsolutePath());
        });
        var destinationFileInputGroup = new InputGroup(
                destinationFileInputLabel,
                destinationFileInputTextField,
                destinationFileInputBrowseButton
        );

        Set<String> set = SecurityUtils.getBlockCiphers();
        blockCipherComboBox.getItems().setAll(set);
        blockCipherComboBox.getSelectionModel().selectFirst();

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> action(true));
        decryptButton.setOnAction(event -> action(false));

        blockCipherComboBox.setOnAction(event -> onAlgorithmSelection());

        var keyLenLabel = new Label("Key Length");
        var keyLenGroup = new InputGroup(keyLenLabel, keyLengthComboBox);

        var keyGroup = new InputGroup(keyLabel, keyTextField);

        ObservableList<String> paddingsList = FXCollections.observableArrayList();
        for (BlockCipherImpl.Padding p: BlockCipherImpl.Padding.values()) {
            paddingsList.add(p.getPadding());
        }
        paddingsComboBox.setItems(paddingsList);
        paddingsComboBox.getSelectionModel().selectFirst();
        var paddingsLabel = new Label("Padding");
        var paddingGroup = new InputGroup(paddingsLabel, paddingsComboBox);

        ObservableList<String> modesList = FXCollections.observableArrayList();
        for (BlockCipherImpl.Mode m: BlockCipherImpl.Mode.values()) {
            modesList.add(m.getMode());
        }
        modesComboBox.setItems(modesList);
        modesComboBox.getSelectionModel().selectFirst();
        var modeLabel = new Label("Mode");
        var modeGroup = new InputGroup(modeLabel, modesComboBox);
        modesComboBox.setOnAction(event -> onModeSelection());

        var ivLabel = new Label("IV");
        var ivShuffleButton = new Button("", new FontIcon(BootstrapIcons.SHUFFLE));
        ivGroup = new InputGroup(ivLabel, ivTextField, ivShuffleButton);
        Tooltip ivTooltip = new Tooltip("Initialization Vector (IV). Required for certain encryption modes" +
                " (e.g., CBC). Must match block size.");
        Tooltip.install(ivGroup, ivTooltip);

        ivShuffleButton.setOnAction(event -> onIvShuffleButtonPressed());

        var cipherSettingsContainer = new FlowPane(
                20, 20,
                blockCipherComboBox,
                keyLenGroup,
                modeGroup,
                paddingGroup,
                ivGroup
        );

        var controlsHBox2 = new HBox(
                20, encryptButton, decryptButton
        );

        counterLabel.getStyleClass().add(Styles.SUCCESS);

        var footerHBox = new HBox(
                20, counterLabel
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
        var algo = blockCipherComboBox.getValue();
        keyLengthComboBox.getItems().setAll(BlockCipherImpl.getAvailableKeyLengths(algo));
        keyLengthComboBox.getSelectionModel().selectFirst();
    }

    private void onModeSelection() {
        var mode = modesComboBox.getValue();

        ivGroup.setDisable(BlockCipherImpl.Mode.fromString(mode) == BlockCipherImpl.Mode.ECB);
    }

    private void onIvShuffleButtonPressed() {
        var value = BlockCipherImpl.generateIV(blockCipherComboBox.getValue());

        ivTextField.setText(HexFormat.of().formatHex(value));
    }

    private void action(boolean encrypt) {
        if (targetFile == null) {
            emptyTargetFileAnimation.playFromStart();
            return;
        }

        if (destinationFile == null) {
            emptyDestinationFileAnimation.playFromStart();
            return;
        }

        var algo = blockCipherComboBox.getValue();
        var mode = BlockCipherImpl.Mode.fromString(modesComboBox.getValue());

        if (mode != BlockCipherImpl.Mode.ECB && ivTextField.getText().isEmpty()) {
            emptyIvAnimation.playFromStart();
            return;
        }

        char[] pass = keyTextField.getText().toCharArray();
        var encKey = BlockCipherImpl.generatePasswordBasedKey(pass, keyLengthComboBox.getValue());
        var padding = BlockCipherImpl.Padding.fromString(paddingsComboBox.getValue());
        var iv = HexFormat.of().parseHex(ivTextField.getText());

        if (encrypt) {
            var encryptionFuture = BlockCipherImpl.asyncEncrypt(
                    targetFile.getAbsolutePath(),
                    destinationFile.getAbsolutePath(),
                    algo,
                    mode,
                    padding,
                    iv,
                    encKey
            );

            encryptionFuture
                    .thenAccept(length -> counterLabel.setText("Encoded %d bytes".formatted(length)))
                    .exceptionally(e -> {
                        System.out.println(e.getMessage());
                        return null;
                    });
        } else {
            var decryptionFuture = BlockCipherImpl.asyncDecrypt(
                    targetFile.getAbsolutePath(),
                    destinationFile.getAbsolutePath(),
                    algo,
                    mode,
                    padding,
                    iv,
                    encKey
            );

            decryptionFuture
                    .thenAccept(length -> counterLabel.setText("Decoded %d bytes".formatted(length)))
                    .exceptionally(e -> {
                        System.out.println(e.getMessage());
                        return null;
                    });
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onInit() {
        blockCipherComboBox.getSelectionModel().select(MemCache.readInteger("block.algo", 0));
        keyLengthComboBox.getSelectionModel().select(MemCache.readInteger("block.key.len", 0));
        modesComboBox.getSelectionModel().select(MemCache.readInteger("block.mode", 0));
        paddingsComboBox.getSelectionModel().select(MemCache.readInteger("block.padding", 0));
        ivTextField.setText(MemCache.readString("block.iv", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeInteger("block.algo", blockCipherComboBox.getItems().indexOf(blockCipherComboBox.getValue()));
        MemCache.writeInteger("block.key.len", keyLengthComboBox.getItems().indexOf(keyLengthComboBox.getValue()));
        MemCache.writeInteger("block.mode", modesComboBox.getItems().indexOf(modesComboBox.getValue()));
        MemCache.writeInteger("block.padding", paddingsComboBox.getItems().indexOf(paddingsComboBox.getValue()));
        MemCache.writeString("block.iv", ivTextField.getText());
    }

}

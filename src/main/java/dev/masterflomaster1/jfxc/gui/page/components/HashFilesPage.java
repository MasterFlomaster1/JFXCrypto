package dev.masterflomaster1.jfxc.gui.page.components;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.JFXCrypto;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import dev.masterflomaster1.jfxc.crypto.UnkeyedCryptoHash;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Set;

public final class HashFilesPage extends SimplePage {

    public static final String NAME = "Hash Files";

    private final ComboBox<String> hashComboBox = new ComboBox<>();
    private final TextArea outputTextArea = UIElementFactory.createOuputTextArea("Result", 100);

    private final ToggleButton hexModeToggleBtn = new ToggleButton("Hex");
    private final ToggleButton b64ModeToggleBtn = new ToggleButton("Base64");

    private File file;

    public HashFilesPage() {
        super();
        addSection("Hash Files", mainSection());

        onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText("""
            Calculation of hash function values for files using 60+ different algorithms. For large files operation may take some time.
            
            [ul]
            [li]For [code]HARAKA-256[/code] - input [color="-color-danger-fg"]must be exactly 32 bytes[/color].[/li]
            [li]For [code]HARAKA-512[/code] - input [color="-color-danger-fg"]must be exactly 64 bytes[/color].[/li]
            [/ul]"""
        );

        var fileInputTextField = new TextField();
        fileInputTextField.setMinWidth(534);
        var fileInputLabel = new Label("", new FontIcon(BootstrapIcons.FILE_EARMARK));
        var fileInputBrowseButton = new Button("Browse");
        var fileInputGroup = new InputGroup(fileInputLabel, fileInputTextField, fileInputBrowseButton);

        fileInputTextField.setPromptText("Select file to hash");
        FileChooser fileChooser = new FileChooser();
        fileInputBrowseButton.setOnAction(event -> {
            file = fileChooser.showOpenDialog(JFXCrypto.getStage());

            if (file == null)
                return;

            fileInputTextField.setText(file.getAbsolutePath());
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

        Set<String> set = SecurityUtils.getDigests();
        hashComboBox.getItems().setAll(set);
        hashComboBox.getSelectionModel().select(0);

        var copyResultButton = UIElementFactory.createCopyButton(outputTextArea);

        var runButton = new Button("Run");
        runButton.setOnAction(event -> {
            if (file == null)
                return;

            var o = UnkeyedCryptoHash.asyncHash(hashComboBox.getValue(), file.getAbsolutePath());
            o
                    .thenAccept(hash -> {
                        outputTextArea.setText(output(hash));
                    })
                    .exceptionally(ex -> {
                        System.out.println(ex.getMessage());
                        return null;
                    });
        });

        var controlsHBox = new HBox(
                20, hashComboBox, runButton, outputModeHBox
        );

        return new VBox(
                20,
                description,
                fileInputGroup,
                controlsHBox,
                new Separator(Orientation.HORIZONTAL),
                outputTextArea,
                copyResultButton
        );
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
        hashComboBox.getSelectionModel().select(MemCache.readInteger("hash.files.algo", 0));
        outputTextArea.setText(MemCache.readString("hash.files.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeInteger("hash.files.algo", hashComboBox.getItems().indexOf(hashComboBox.getValue()));
        MemCache.writeString("hash.files.output", outputTextArea.getText());
    }
}

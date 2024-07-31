package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.JFXCrypto;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.HashFilesViewModel;
import javafx.beans.binding.Bindings;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;

public final class HashFilesPage extends AbstractByteFormattingView {

    public static final String NAME = "Hash Files";

    private final ComboBox<String> hashComboBox = new ComboBox<>();

    private final HashFilesViewModel viewModel = new HashFilesViewModel();

    public HashFilesPage() {
        super();
        addSection("Hash Files", mainSection());
        bindComponents();

        viewModel.onInit();
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
            File file = fileChooser.showOpenDialog(JFXCrypto.getStage());

            if (file == null)
                return;

            viewModel.setSelectedFile(file);
            fileInputTextField.setText(file.getAbsolutePath());
        });

        var runButton = new Button("Run");
        runButton.setOnAction(event -> viewModel.action());

        var controlsHBox = new HBox(
                20,
                hashComboBox,
                runButton
        );

        var footerHBox = createFormattingOutputArea();

        return new VBox(
                20,
                description,
                fileInputGroup,
                controlsHBox,
                new Separator(Orientation.HORIZONTAL),
                outputTextArea,
                footerHBox
        );
    }

    private void bindComponents() {
        outputTextArea.textProperty().bindBidirectional(viewModel.outputTextProperty());
        hashComboBox.valueProperty().bindBidirectional(viewModel.hashComboBoxPropertyProperty());
        Bindings.bindContent(hashComboBox.getItems(), viewModel.hashAlgorithmsList());
        hexModeToggleBtn.selectedProperty().bindBidirectional(viewModel.hexModeToggleButtonProperty());
        b64ModeToggleBtn.selectedProperty().bindBidirectional(viewModel.b64ModeToggleButtonProperty());

        hexModeToggleBtn.setSelected(true);
        toggleGroup.selectedToggleProperty().addListener(viewModel::onToggleChanged);
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

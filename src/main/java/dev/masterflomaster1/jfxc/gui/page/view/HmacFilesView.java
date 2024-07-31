package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.JFXCrypto;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.HmacFilesViewModel;
import javafx.animation.Timeline;
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
import javafx.stage.FileChooser;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;

public class HmacFilesView extends SimplePage {

    public static final String NAME = "HMAC Files";

    private final TextArea outputTextArea = UIElementFactory.createOuputTextArea("Hashed output");
    private final TextField keyTextField = new TextField();
    private final ComboBox<String> hmacComboBox = new ComboBox<>();

    private final ToggleButton hexModeToggleBtn = new ToggleButton("Hex");
    private final ToggleButton b64ModeToggleBtn = new ToggleButton("Base64");

    private ToggleGroup toggleGroup;
    private Timeline emptyKeyAnimation;

    private final HmacFilesViewModel viewModel = new HmacFilesViewModel();

    public HmacFilesView() {
        super();

        addSection(NAME, mainSection());
        bindComponents();
        viewModel.onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "HMAC (Hash-based Message Authentication Code) generator supporting over 40 algorithms for " +
                        "secure file authentication."
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

            viewModel.setSelectedFile(targetFile);
            targetFileInputTextField.setText(targetFile.getAbsolutePath());
        });
        var targetFileInputGroup = new InputGroup(
                targetFileInputLabel,
                targetFileInputTextField,
                targetFileInputBrowseButton
        );

        var runButton = new Button("Run");
        runButton.setOnAction(event -> viewModel.action());

        var keyGroup = new InputGroup(keyLabel, keyTextField);
        emptyKeyAnimation = Animations.wobble(keyGroup);
        var controlsHBox = new HBox(
                20,
                hmacComboBox,
                keyGroup,
                runButton
        );

        toggleGroup = new ToggleGroup();
        hexModeToggleBtn.setSelected(true);
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

        return new VBox(
                20,
                description,
                targetFileInputGroup,
                controlsHBox,
                outputTextArea,
                footerHBox
        );
    }

    private void bindComponents() {
        outputTextArea.textProperty().bindBidirectional(viewModel.outputTextProperty());
        keyTextField.textProperty().bindBidirectional(viewModel.keyTextProperty());
        counterLabel.textProperty().bindBidirectional(viewModel.counterTextProperty());

        Bindings.bindContent(hmacComboBox.getItems(), viewModel.getHmacAlgorithmsList());
        hmacComboBox.valueProperty().bindBidirectional(viewModel.hmacComboBoxProperty());

        hexModeToggleBtn.selectedProperty().bindBidirectional(viewModel.hexModeToggleButtonPropertyProperty());
        b64ModeToggleBtn.selectedProperty().bindBidirectional(viewModel.b64ModeToggleButtonPropertyProperty());

        hmacComboBox.getSelectionModel().selectFirst();
        hexModeToggleBtn.setSelected(true);
        toggleGroup.selectedToggleProperty().addListener(viewModel::onToggleChanged);
        viewModel.setEmptyKeyAnimation(emptyKeyAnimation);
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

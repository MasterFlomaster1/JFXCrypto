package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.HmacViewModel;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class HmacPage extends SimplePage {

    public static final String NAME = "HMAC";

    private final TextArea inputTextArea = UIElementFactory.createInputTextArea("Enter plain text to hash");
    private final TextArea outputTextArea = UIElementFactory.createOuputTextArea("Hashed output");
    private final TextField keyTextField = new TextField();
    private final ComboBox<String> hmacComboBox = new ComboBox<>();

    private final ToggleButton hexModeToggleBtn = new ToggleButton("Hex");
    private final ToggleButton b64ModeToggleBtn = new ToggleButton("Base64");

    private ToggleGroup toggleGroup;
    private Timeline emptyKeyAnimation;

    private final HmacViewModel viewModel = new HmacViewModel();

    public HmacPage() {
        super();

        addSection("HMAC", mainSection());
        bindComponents();
        viewModel.onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "HMAC (Hash-based Message Authentication Code) generator supporting over 40 algorithms for " +
                        "secure text authentication."
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
                inputTextArea,
                controlsHBox,
                outputTextArea,
                footerHBox
        );
    }

    private void bindComponents() {
        inputTextArea.textProperty().bindBidirectional(viewModel.inputTextProperty());
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

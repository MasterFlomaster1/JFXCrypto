package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.HmacViewModel;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class HmacTextPage extends AbstractByteFormattingView {

    public static final String NAME = "HMAC Text";

    private final TextArea inputTextArea = UIElementFactory.createInputTextArea("Enter plain text to hash");
    private final TextField keyTextField = new TextField();
    private final ComboBox<String> hmacComboBox = new ComboBox<>();

    private Timeline emptyKeyAnimation;

    private final HmacViewModel viewModel = new HmacViewModel();

    public HmacTextPage() {
        super();

        addSection(NAME, mainSection());
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

        var footerHBox = createFormattingOutputArea();

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

        hexModeToggleBtn.selectedProperty().bindBidirectional(viewModel.hexModeToggleButtonProperty());
        b64ModeToggleBtn.selectedProperty().bindBidirectional(viewModel.b64ModeToggleButtonProperty());

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

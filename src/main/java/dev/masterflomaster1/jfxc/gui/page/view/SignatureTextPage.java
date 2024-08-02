package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.SignatureTextViewModel;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

public class SignatureTextPage extends AbstractByteFormattingView {

    public static final String NAME = "Signature Text";

    private final TextArea inputTextArea = UIElementFactory.createInputTextArea("", 100);
    private final ComboBox<String> signaturesComboBox = new ComboBox<>();

    private ToggleGroup modeToggleGroup;
    private final ToggleButton signToggleButton = new ToggleButton("Sign");
    private final ToggleButton verifyToggleButton = new ToggleButton("Verify");

    private final Button resultLabel = new Button();

    private final SignatureTextViewModel viewModel = new SignatureTextViewModel();

    public SignatureTextPage() {
        super();

        addSection("Sign text data", mainSection());
        bindComponents();

        viewModel.onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Sign text data"
        );

        var signButton = new Button("Sign");
        var generateKeyPairButton = new Button("Generate");

        generateKeyPairButton.setOnAction(viewModel::onKeyPairGenerateAction);
        signButton.setOnAction(e -> viewModel.action());

        modeToggleGroup = new ToggleGroup();
        signToggleButton.setToggleGroup(modeToggleGroup);
        verifyToggleButton.setToggleGroup(modeToggleGroup);
        signToggleButton.getStyleClass().add(Styles.LEFT_PILL);
        verifyToggleButton.getStyleClass().add(Styles.RIGHT_PILL);

        var operationMode = new HBox(signToggleButton, verifyToggleButton);

        modeToggleGroup.selectedToggleProperty().addListener(e -> {
            resultLabel.setDisable(signToggleButton.isSelected());
        });

        resultLabel.textProperty().addListener((obs, oldValue, newValue) -> {
            if ("Valid".equals(newValue)) {
                resultLabel.setGraphic(new FontIcon(BootstrapIcons.CHECK_CIRCLE_FILL));
                resultLabel.getStyleClass().remove(Styles.DANGER);
                resultLabel.getStyleClass().add(Styles.SUCCESS);
            } else if ("Invalid".equals(newValue)) {
                resultLabel.setGraphic(new FontIcon(BootstrapIcons.EXCLAMATION_CIRCLE_FILL));
                resultLabel.getStyleClass().remove(Styles.SUCCESS);
                resultLabel.getStyleClass().add(Styles.DANGER);
            }
        });

        var signatureSettingsContainer = new FlowPane(
                20, 20,
                signaturesComboBox,
                operationMode
        );

        var controlsHBox2 = new HBox(
                20,
                signButton,
                generateKeyPairButton,
                resultLabel
        );

        var footerHBox = createFormattingOutputArea();

        return new VBox(
                20,
                description,
                inputTextArea,
                signatureSettingsContainer,
                controlsHBox2,
                outputTextArea,
                footerHBox
        );
    }

    private void bindComponents() {
        inputTextArea.textProperty().bindBidirectional(viewModel.inputTextProperty());
        outputTextArea.textProperty().bindBidirectional(viewModel.outputTextProperty());
        resultLabel.textProperty().bindBidirectional(viewModel.getResultTextProperty());
        counterLabel.textProperty().bind(viewModel.counterTextProperty());

        signaturesComboBox.valueProperty().bindBidirectional(viewModel.signatureComboBoxProperty());
        Bindings.bindContent(signaturesComboBox.getItems(), viewModel.getSignatureAlgorithmsList());

        hexModeToggleBtn.selectedProperty().bindBidirectional(viewModel.hexModeToggleButtonProperty());
        b64ModeToggleBtn.selectedProperty().bindBidirectional(viewModel.b64ModeToggleButtonProperty());
        toggleGroup.selectedToggleProperty().addListener(viewModel::onToggleChanged);
        hexModeToggleBtn.setSelected(true);

        signToggleButton.selectedProperty().bindBidirectional(viewModel.signToggleButtonProperty());
        verifyToggleButton.selectedProperty().bindBidirectional(viewModel.verifyToggleButtonProperty());
        modeToggleGroup.selectedToggleProperty().addListener(viewModel::onModeToggleChanged);
        signToggleButton.setSelected(true);
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

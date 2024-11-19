package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.SignatureTextViewModel;
import javafx.beans.binding.Bindings;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

public class SignatureTextPage extends SimplePage {

    public static final String NAME = "Signature Text";

    private final ComboBox<String> signaturesComboBox = new ComboBox<>();

    private ToggleGroup modeToggleGroup;
    private final ToggleButton signToggleButton = new ToggleButton("Sign");
    private final ToggleButton verifyToggleButton = new ToggleButton("Verify");

    private final Button resultLabel = new Button();

    private final KeyPairComponent keyPairComponent = new KeyPairComponent();
    private final InputOutputAreaComponent ioAreaComponent = new InputOutputAreaComponent();

    private final SignatureTextViewModel viewModel = new SignatureTextViewModel();

    public SignatureTextPage() {
        super();

        addSection("Sign text data", mainSection());
        viewModel.setKeyPairViewModelComponent(keyPairComponent.getViewModel());
        viewModel.setIoComponentViewModel(ioAreaComponent.getViewModel());
        bindComponents();

        viewModel.onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Sign text data"
        );

        var algoSelectionGroup = new InputGroup(new Label("Algorithm"), signaturesComboBox);

        var keyArea = createKeyGenArea();

        var signButton = new Button("Sign");
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
                onValid();
            } else if ("Invalid".equals(newValue)) {
                onInvalid();
            }
        });

        var signatureSettingsContainer = new FlowPane(
                20, 20,
                operationMode,
                signButton,
                resultLabel
        );

        var ioArea = ioAreaComponent.createSection();

        return new VBox(
                20,
                description,
                algoSelectionGroup,
                keyArea,
                new Separator(Orientation.HORIZONTAL),
                signatureSettingsContainer,
                ioArea
        );
    }

    private void onValid() {
        resultLabel.setGraphic(new FontIcon(BootstrapIcons.CHECK_CIRCLE_FILL));
        resultLabel.getStyleClass().remove(Styles.DANGER);
        resultLabel.getStyleClass().add(Styles.SUCCESS);
    }

    private void onInvalid() {
        resultLabel.setGraphic(new FontIcon(BootstrapIcons.EXCLAMATION_CIRCLE_FILL));
        resultLabel.getStyleClass().remove(Styles.SUCCESS);
        resultLabel.getStyleClass().add(Styles.DANGER);
    }

    private VBox createKeyGenArea() {
        var generateKeyButton = new Button("Generate");
        generateKeyButton.setOnAction(viewModel::onKeyPairGenerateAction);

        var grid = keyPairComponent.createSection();

        return new VBox(
                20,
                generateKeyButton,
                grid
        );
    }

    private void bindComponents() {
        resultLabel.textProperty().bindBidirectional(viewModel.getResultText());
        counterLabel.textProperty().bind(viewModel.getCounterText());

        signaturesComboBox.valueProperty().bindBidirectional(viewModel.getSignatureComboBoxProperty());
        Bindings.bindContent(signaturesComboBox.getItems(), viewModel.getSignatureAlgorithmsList());

        signToggleButton.selectedProperty().bindBidirectional(viewModel.getSignToggleButtonProperty());
        verifyToggleButton.selectedProperty().bindBidirectional(viewModel.getVerifyToggleButtonProperty());
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

package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

public class SignatureTextPage extends SimplePage {

    public static final String NAME = "Signature Text";

    private final ComboBox<String> signaturesComboBox = new ComboBox<>();

    private final Button signButton = new Button("Sign");
    private final Button verifyButton = new Button("Verify");
    private final Button resultLabel = new Button();

    private final KeyPairComponent keyPairComponent = new KeyPairComponent();
    private final InputOutputAreaComponent ioAreaComponent = new InputOutputAreaComponent();

    private final SignatureTextViewModel viewModel = new SignatureTextViewModel();

    public SignatureTextPage() {
        super();

        addSection("Sign text data", mainSection());
        viewModel.setKeyPairViewModelComponent(keyPairComponent.getViewModel());
        viewModel.setIoComponentViewModel(ioAreaComponent.getViewModel());
        hideResult();
        bindComponents();

        viewModel.onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Sign text data"
        );

        var algoSelectionGroup = new InputGroup(new Label("Algorithm"), signaturesComboBox);

        var keyArea = createKeyGenArea();

        HBox controlsBox = new HBox(10, signButton, verifyButton, resultLabel);

        signButton.setDisable(true);
        verifyButton.setDisable(true);
        signButton.setOnAction(e -> {
            viewModel.onSign();
            hideResult();
        });
        verifyButton.setOnAction(e -> {
            viewModel.onVerify();
            showResult();
        });

        ioAreaComponent.getInputArea().textProperty().addListener((observable, oldValue, newValue) -> {
            signButton.setDisable(newValue.isEmpty());
        });

        ioAreaComponent.getOutputArea().textProperty().addListener((observable, oldValue, newValue) -> {
            verifyButton.setDisable(newValue.isEmpty());
        });

        resultLabel.textProperty().addListener((obs, oldValue, newValue) -> {
            if ("Valid".equals(newValue)) {
                onValid();
            } else if ("Invalid".equals(newValue)) {
                onInvalid();
            }
        });

        var ioArea = ioAreaComponent.createSection();

        return new VBox(
                20,
                description,
                algoSelectionGroup,
                keyArea,
                new Separator(Orientation.HORIZONTAL),
                controlsBox,
                ioArea
        );
    }

    private void showResult() {
        resultLabel.setVisible(true);
        resultLabel.setManaged(true);

        var animation = Animations.fadeIn(resultLabel, Duration.millis(100));
        animation.playFromStart();
    }

    private void hideResult() {
        resultLabel.setVisible(false);
        resultLabel.setManaged(false);
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

        signaturesComboBox.valueProperty().bindBidirectional(viewModel.getSignatureComboBoxProperty());
        Bindings.bindContent(signaturesComboBox.getItems(), viewModel.getSignatureAlgorithmsList());
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

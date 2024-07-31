package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.Pbkdf2ViewModel;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

public final class Pbkdf2Page extends SimplePage {

    public static final String NAME = "PBKDF2";

    private final TextField passwordInputField = new TextField();
    private final TextField iterationsInputTextField = new TextField();
    private final TextField keyLengthInputTextField = new TextField();
    private final TextField saltInputField = new TextField();
    private final TextArea outputTextArea = UIElementFactory.createOuputTextArea("Result");

    private final ComboBox<String> pbkdfComboBox = new ComboBox<>();

    private final ToggleButton hexModeToggleBtn = new ToggleButton("Hex");
    private final ToggleButton b64ModeToggleBtn = new ToggleButton("Base64");

    private ToggleGroup toggleGroup;
    private Timeline emptyPasswordAnimation;
    private Timeline emptyIterationsAnimation;
    private Timeline emptyKeyLengthAnimation;
    private Timeline emptySaltAnimation;

    private final Pbkdf2ViewModel viewModel = new Pbkdf2ViewModel();

    public Pbkdf2Page() {
        super();

        addSection("PBKDF2", mainSection());
        bindComponents();
        viewModel.onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "PBKDF2 applies a pseudorandom function, such as hash-based message authentication code (HMAC)," +
                        " to the input password or passphrase along with a salt value and repeats the process many" +
                        " times to produce a derived key, which can then be used as a cryptographic key in subsequent" +
                        " operations. The added computational work makes password cracking much more difficult, and" +
                        " is known as key stretching."
        );

        var passwordInputGroup = new InputGroup(keyLabel, passwordInputField);

        var saltInputLabel = new Label("Salt");
        var saltInputButton = new Button("", new FontIcon(BootstrapIcons.SHUFFLE));
        var saltInputGroup = new InputGroup(saltInputLabel, saltInputField, saltInputButton);

        saltInputButton.setOnAction(viewModel::onSaltShuffleAction);

        var iterationsInputLabel = new Label("Iterations");
        var iterationsInputGroup = new InputGroup(iterationsInputLabel, iterationsInputTextField);

        var keyLengthInputLabel = new Label("Key bit-length");
        var keyLengthInputGroup = new InputGroup(keyLengthInputLabel, keyLengthInputTextField);

        var runButton = new Button("Run");
        runButton.setOnAction(event -> viewModel.action());

        outputTextArea.setEditable(false);
        outputTextArea.setWrapText(true);

        var container = new FlowPane(
                20, 20,
                pbkdfComboBox,
                passwordInputGroup,
                keyLengthInputGroup,
                iterationsInputGroup,
                saltInputGroup
        );

        var copyResultButton = UIElementFactory.createCopyButton(outputTextArea);

        toggleGroup = new ToggleGroup();
        hexModeToggleBtn.setToggleGroup(toggleGroup);
        b64ModeToggleBtn.setToggleGroup(toggleGroup);
        hexModeToggleBtn.getStyleClass().add(Styles.LEFT_PILL);
        b64ModeToggleBtn.getStyleClass().add(Styles.RIGHT_PILL);

        var outputModeHBox = new HBox(hexModeToggleBtn, b64ModeToggleBtn);

        var footerHBox = new HBox(
                20,
                copyResultButton,
                outputModeHBox,
                counterLabel
        );
        footerHBox.setAlignment(Pos.CENTER_LEFT);

        emptyPasswordAnimation = Animations.wobble(passwordInputGroup);
        emptyIterationsAnimation = Animations.wobble(iterationsInputGroup);
        emptyKeyLengthAnimation = Animations.wobble(keyLengthInputGroup);
        emptySaltAnimation = Animations.wobble(saltInputGroup);

        return new VBox(
                20,
                description,
                container,
                runButton,
                outputTextArea,
                footerHBox
        );
    }

    private void bindComponents() {
        passwordInputField.textProperty().bindBidirectional(viewModel.passwordTextProperty());
        iterationsInputTextField.textProperty().bindBidirectional(viewModel.iterationsTextProperty());
        keyLengthInputTextField.textProperty().bindBidirectional(viewModel.getKeyLengthTextProperty());
        saltInputField.textProperty().bindBidirectional(viewModel.saltTextProperty());
        outputTextArea.textProperty().bindBidirectional(viewModel.outputTextProperty());

        Bindings.bindContent(pbkdfComboBox.getItems(), viewModel.getPbkdf2AlgorithmsList());
        pbkdfComboBox.valueProperty().bindBidirectional(viewModel.pbkdf2ComboBoxProperty());
        hexModeToggleBtn.selectedProperty().bindBidirectional(viewModel.hexModeToggleButtonProperty());
        b64ModeToggleBtn.selectedProperty().bindBidirectional(viewModel.b64ModeToggleButtonProperty());

        pbkdfComboBox.getSelectionModel().selectFirst();
        toggleGroup.selectedToggleProperty().addListener(viewModel::onToggleChanged);
        hexModeToggleBtn.setSelected(true);

        viewModel.setEmptyPasswordAnimation(emptyPasswordAnimation);
        viewModel.setEmptyIterationsAnimation(emptyIterationsAnimation);
        viewModel.setEmptyKeyLengthAnimation(emptyKeyLengthAnimation);
        viewModel.setEmptySaltAnimation(emptySaltAnimation);
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

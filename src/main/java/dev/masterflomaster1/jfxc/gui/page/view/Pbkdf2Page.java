package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.Pbkdf2ViewModel;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

public final class Pbkdf2Page extends AbstractByteFormattingView {

    public static final String NAME = "PBKDF2";

    private final TextField passwordInputField = new TextField();
    private final TextField iterationsInputTextField = new TextField();
    private final TextField keyLengthInputTextField = new TextField();
    private final TextField saltInputField = new TextField();

    private final ComboBox<String> pbkdfComboBox = new ComboBox<>();

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

        var container = new FlowPane(
                20, 20,
                pbkdfComboBox,
                passwordInputGroup,
                keyLengthInputGroup,
                iterationsInputGroup,
                saltInputGroup
        );

        var footerHBox = createFormattingOutputArea();

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

package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.crypto.passwords.PasswordEvaluatorService;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

public final class PasswordMeterPage extends SimplePage {

    public static final String NAME = "Password Meter";

    private final TextField passwordTextField = new TextField();
    private final TextField scoreTextField = new TextField();
    private final TextArea reportTextArea = new TextArea();
    private final ProgressBar progressBar = new ProgressBar();

    private StackPane progressStack;

    public PasswordMeterPage() {
        super();

        addSection(NAME, mainSection());
        onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
            "Evaluates the strength of an entered password locally"
        );

        var passwordLabel = new Label("Password", new FontIcon(BootstrapIcons.KEY_FILL));
        var passwordGroup = new InputGroup(passwordLabel, passwordTextField);

        var scoreLabel = new Label("Score");
        scoreTextField.setEditable(false);
        var scoreGroup = new InputGroup(scoreLabel, scoreTextField);

        reportTextArea.setEditable(false);
        reportTextArea.setWrapText(true);

        progressStack = new StackPane(progressBar, new Label());
        progressStack.getStyleClass().add("example");
        progressBar.setProgress(0.0);

        passwordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue.equals(newValue)) return;

            action();
        });

        return new VBox(
                20,
                description,
                passwordGroup,
                scoreGroup,
                progressBar,
                reportTextArea
        );
    }

    private void action() {
        if (passwordTextField.getText().isEmpty())
            return;

        var value = passwordTextField.getText();
        var score = PasswordEvaluatorService.of().getZxcvbn().getStrengthScore(value);
        var report = PasswordEvaluatorService.of().getZxcvbn().getStrengthReport(value);

        if (score <= 25) {
            progressBar.setProgress(25);
            progressStack.pseudoClassStateChanged(Styles.STATE_DANGER, true);
        } else if (score <= 50) {
            progressBar.setProgress(50);
            progressStack.pseudoClassStateChanged(Styles.STATE_DANGER, true);
        } else if (score <= 75) {
            progressBar.setProgress(75);
            progressStack.pseudoClassStateChanged(Styles.STATE_WARNING, true);
        } else {
            progressBar.setProgress(100);
            progressStack.pseudoClassStateChanged(Styles.STATE_SUCCESS, true);
        }

        scoreTextField.setText(String.valueOf(score));
        reportTextArea.setText(report.getCombined());
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onInit() {
        super.onInit();
    }

    @Override
    public void onReset() {
        super.onReset();
    }
}

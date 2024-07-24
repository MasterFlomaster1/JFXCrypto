package dev.masterflomaster1.jfxc.gui.page.components;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.PasswordStrengthViewModel;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

public final class PasswordStrengthPage extends SimplePage {

    public static final String NAME = "Password Strength Evaluator";

    private final TextField passwordTextField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label lengthLabel = new Label("Length: ");

    private final ProgressBar zxcvbnProgressBar = new ProgressBar();
    private final Label zxcvbnScoreLabel = new Label();

    private final VBox feedbackBox = new VBox(10);

    private final PasswordStrengthViewModel viewModel = new PasswordStrengthViewModel();

    private StackPane progressStack;

    public PasswordStrengthPage() {
        super();

        addSection(NAME, mainSection());
        bindComponents();
        onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
            "Local calculation and analysis of password complexity"
        );

        var passwordLabel = new Label("Password", new FontIcon(BootstrapIcons.KEY_FILL));
        var clearButton = new Button("", new FontIcon(BootstrapIcons.X));
        var passwordGroup = new InputGroup(passwordLabel, passwordTextField, passwordField, clearButton);
        var passwordInputBox = new HBox(
                20,
                passwordGroup,
                lengthLabel
        );
        passwordInputBox.setAlignment(Pos.CENTER_LEFT);

        passwordTextField.setManaged(false);
        passwordTextField.setVisible(false);
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());

        var showPassword = new CheckBox("Show password");

        showPassword.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                passwordTextField.setVisible(true);
                passwordTextField.setManaged(true);
                passwordField.setVisible(false);
                passwordField.setManaged(false);
            } else {
                passwordTextField.setVisible(false);
                passwordTextField.setManaged(false);
                passwordField.setVisible(true);
                passwordField.setManaged(true);
            }
        });

        clearButton.setOnAction((event -> passwordTextField.clear()));

        zxcvbnProgressBar.getStyleClass().add(Styles.MEDIUM);
        zxcvbnProgressBar.setPrefWidth(300);
        zxcvbnProgressBar.setProgress(0.0);
        progressStack = new StackPane(zxcvbnProgressBar);
        progressStack.getStyleClass().add("example");

        zxcvbnProgressBar.progressProperty().addListener((obs, oldValue, newValue) -> {
            progressStack.pseudoClassStateChanged(Styles.STATE_DANGER, false);
            progressStack.pseudoClassStateChanged(Styles.STATE_WARNING, false);
            progressStack.pseudoClassStateChanged(Styles.STATE_SUCCESS, false);

            double val = newValue.doubleValue();

            if (val < 0.25) {
                progressStack.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            } else if (val < 0.50) {
                progressStack.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            } else if (val < 0.75) {
                progressStack.pseudoClassStateChanged(Styles.STATE_WARNING, true);
            } else if (val < 1.0) {
                progressStack.pseudoClassStateChanged(Styles.STATE_SUCCESS, true);
            } else {
                progressStack.pseudoClassStateChanged(Styles.STATE_SUCCESS, true);
            }
        });

        var dataClass = """
                .example:success .progress-bar {
                    -color-progress-bar-fill: -color-success-emphasis;
                }
                .example:danger .progress-bar {
                    -color-progress-bar-fill: -color-danger-emphasis;
                }
                .example:warning .progress-bar {
                    -color-progress-bar-fill: -color-warning-emphasis;
                }
                .example:success .label,
                .example:danger .label,
                .example:warning .label {
                    -fx-text-fill: -color-fg-emphasis;
                }""";

        var zxcvbnLabel = new Label("zxcvbn");

        var grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);

        zxcvbnScoreLabel.minWidth(36);
        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        col1.setPrefWidth(100);
        col2.setPrefWidth(40);
        col3.setPrefWidth(300);
        grid.getColumnConstraints().addAll(col1, col2, col3);

        grid.getStylesheets().add(Styles.toDataURI(dataClass));
        grid.addRow(0, zxcvbnLabel, zxcvbnScoreLabel, progressStack);

        var ratingLabel = new Label("Rating: ");
        ratingLabel.getStyleClass().add(Styles.TITLE_4);

        return new VBox(
                20,
                description,
                passwordInputBox,
                showPassword,
                new Separator(),
                ratingLabel,
                grid,
                feedbackBox
        );
    }

    private void bindComponents() {
        passwordTextField.textProperty().bindBidirectional(viewModel.passwordTextProperty());
        lengthLabel.textProperty().bindBidirectional(viewModel.lengthTextProperty());
        zxcvbnProgressBar.progressProperty().bindBidirectional(viewModel.zxcvbnProgressBarProperty());
        zxcvbnScoreLabel.textProperty().bindBidirectional(viewModel.zxcvbnScoreTextProperty());
        Bindings.bindContent(feedbackBox.getChildren(), viewModel.getFeedbackBoxList());
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

package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.PwnedPasswordsViewModel;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

public class PwnedPasswordsPage extends SimplePage {

    public static final String NAME = "Pwned Passwords";

    private final TextField passwordTextField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final VBox feedbackBox = new VBox(10);

    private final PwnedPasswordsViewModel viewModel = new PwnedPasswordsViewModel();

    public PwnedPasswordsPage() {
        super();

        addSection("Pwned Passwords", mainSection());
        bindComponents();

        viewModel.onInit();
    }

    public Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Pwned Passwords consists of hundreds of millions of real-world passwords that have been " +
                        "exposed in data breaches, making them unsuitable for ongoing use due to the heightened risk " +
                        "of account takeovers. This program checks passwords against this database using the Have I " +
                        "Been Pwned API and its k-anonymity method to ensure user privacy."
        );

        var passwordLabel = new Label("Password", new FontIcon(BootstrapIcons.KEY_FILL));
        var runButton = new Button("", new FontIcon(BootstrapIcons.FORWARD_FILL));
        var passwordGroup = new InputGroup(passwordLabel, passwordTextField, passwordField, runButton);
        var passwordInputBox = new HBox(
                20,
                passwordGroup
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

        passwordTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                viewModel.action();
        });
        runButton.setOnAction((event) -> viewModel.action());

        return new VBox(
                20,
                description,
                passwordInputBox,
                showPassword,
                feedbackBox
        );
    }

    private void bindComponents() {
        passwordTextField.textProperty().bindBidirectional(viewModel.getPasswordTextProperty());
        Bindings.bindContent(feedbackBox.getChildren(), viewModel.getFeedbackBoxList());
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

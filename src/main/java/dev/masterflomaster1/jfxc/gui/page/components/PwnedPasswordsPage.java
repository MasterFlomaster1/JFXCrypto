package dev.masterflomaster1.jfxc.gui.page.components;

import atlantafx.base.controls.Message;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.crypto.passwords.HaveIBeenPwnedApiClient;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class PwnedPasswordsPage extends SimplePage {

    public static final String NAME = "Pwned Passwords";

    private final TextField passwordTextField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final VBox feedbackBox = new VBox(10);

    public PwnedPasswordsPage() {
        super();

        addSection("Pwned Passwords", mainSection());

        onInit();
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
                action();
        });
        runButton.setOnAction((event) -> action());

        return new VBox(
                20,
                description,
                passwordInputBox,
                showPassword,
                feedbackBox
        );
    }

    private void action() {
        if (passwordTextField.getText().isEmpty())
            return;

        var value = passwordTextField.getText();

        Optional<Integer> apiResponse;

        try {
            apiResponse = HaveIBeenPwnedApiClient.passwordRange(value.getBytes(StandardCharsets.UTF_8));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        feedbackBox.getChildren().clear();

        if (apiResponse.isPresent()) {
            var title = "Compromised Password";
            var desc = ("Your password has been found in a data breach and is no longer safe to use. It has been " +
                    "seen %d times. We strongly recommend changing your password immediately.").formatted(apiResponse.get());
            var message = new Message(title, desc, new FontIcon(BootstrapIcons.EXCLAMATION_TRIANGLE_FILL));

            message.getStyleClass().add(Styles.DANGER);
            feedbackBox.getChildren().add(message);
        } else {
            var title = "Safe Password";
            var desc = "Good news! Your password was not found in any known data breaches and appears to be safe " +
                    "for use. However, always remember to use strong and unique passwords for each of your accounts.";
            var message = new Message(title, desc, new FontIcon(BootstrapIcons.CHECK_CIRCLE_FILL));

            message.getStyleClass().add(Styles.ACCENT);
            feedbackBox.getChildren().add(message);
        }
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
        super.onInit();
    }

}

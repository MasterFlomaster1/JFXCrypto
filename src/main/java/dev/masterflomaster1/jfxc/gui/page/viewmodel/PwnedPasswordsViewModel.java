package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import dev.masterflomaster1.jfxc.crypto.passwords.HaveIBeenPwnedApiClient;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class PwnedPasswordsViewModel extends AbstractViewModel {

    private final StringProperty passwordTextProperty = new SimpleStringProperty();
    private final ObservableList<Node> feedbackBoxList = FXCollections.observableList(FXCollections.observableArrayList());

    public StringProperty getPasswordTextProperty() {
        return passwordTextProperty;
    }

    public ObservableList<Node> getFeedbackBoxList() {
        return feedbackBoxList;
    }

    public void action() {
        if (passwordTextProperty.get().isEmpty())
            return;


        var value = passwordTextProperty.get();

        Optional<Integer> apiResponse;

        try {
            apiResponse = HaveIBeenPwnedApiClient.passwordRange(value.getBytes(StandardCharsets.UTF_8));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        feedbackBoxList.clear();

        if (apiResponse.isPresent()) {
            var title = "Compromised Password";
            var desc = ("Your password has been found in a data breach and is no longer safe to use. It has been " +
                    "seen %d times. We strongly recommend changing your password immediately.").formatted(apiResponse.get());
            var message = new Message(title, desc, new FontIcon(BootstrapIcons.EXCLAMATION_TRIANGLE_FILL));

            message.getStyleClass().add(Styles.DANGER);
            feedbackBoxList.add(message);
        } else {
            var title = "Safe Password";
            var desc = "Good news! Your password was not found in any known data breaches and appears to be safe " +
                    "for use. However, always remember to use strong and unique passwords for each of your accounts.";
            var message = new Message(title, desc, new FontIcon(BootstrapIcons.CHECK_CIRCLE_FILL));

            message.getStyleClass().add(Styles.ACCENT);
            feedbackBoxList.add(message);
        }
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onReset() {

    }
}

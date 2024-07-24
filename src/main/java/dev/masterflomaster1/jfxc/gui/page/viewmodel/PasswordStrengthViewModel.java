package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import atlantafx.base.controls.Message;
import atlantafx.base.theme.Styles;
import dev.masterflomaster1.jfxc.crypto.passwords.PasswordEvaluatorService;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

public class PasswordStrengthViewModel {

    private final StringProperty passwordTextProperty = new SimpleStringProperty();
    private final DoubleProperty zxcvbnProgressBarProperty = new SimpleDoubleProperty();
    private final StringProperty zxcvbnScoreTextProperty = new SimpleStringProperty();
    private final StringProperty lengthTextProperty = new SimpleStringProperty();

    private final ObservableList<Node> feedbackBoxList = FXCollections.observableList(FXCollections.observableArrayList());

    public PasswordStrengthViewModel() {
        passwordTextProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue == null || newValue == null) return;
            if (oldValue.equals(newValue)) return;

            lengthTextProperty.set("Length: " + newValue.length());

            action();
        });
    }

    public StringProperty passwordTextProperty() {
        return passwordTextProperty;
    }

    public StringProperty lengthTextProperty() {
        return lengthTextProperty;
    }

    public DoubleProperty zxcvbnProgressBarProperty() {
        return zxcvbnProgressBarProperty;
    }

    public StringProperty zxcvbnScoreTextProperty() {
        return zxcvbnScoreTextProperty;
    }

    public ObservableList<Node> getFeedbackBoxList() {
        return feedbackBoxList;
    }

    private void action() {
        if (passwordTextProperty.get().isEmpty())
            return;

        var value = passwordTextProperty.get();

        var score = PasswordEvaluatorService.of().getZxcvbn().getStrengthScore(value);
        var warning = PasswordEvaluatorService.of().getZxcvbn().getWarning(value);
        var suggestions = PasswordEvaluatorService.of().getZxcvbn().getSuggestions(value);

        if (score < 25) {
            zxcvbnProgressBarProperty.set(0.01);
        } else if (score < 50) {
            zxcvbnProgressBarProperty.set(0.25);
        } else if (score < 75) {
            zxcvbnProgressBarProperty.set(0.50);
        } else if (score < 100) {
            zxcvbnProgressBarProperty.set(0.75);
        } else {
            zxcvbnProgressBarProperty.set(1);
        }

        feedbackBoxList.clear();

        if (!warning.isEmpty()) {
            var warningMessage = new Message(warning, null, new FontIcon(BootstrapIcons.EXCLAMATION_TRIANGLE_FILL));
            warningMessage.getStyleClass().add(Styles.DANGER);
            feedbackBoxList.add(warningMessage);
        }

        if (!suggestions.isEmpty()) {
            for (var suggestion : suggestions) {
                var sugMessage = new Message(suggestion, null, new FontIcon(BootstrapIcons.INFO_CIRCLE_FILL));
                sugMessage.getStyleClass().add(Styles.ACCENT);
                feedbackBoxList.add(sugMessage);
            }
        }

        zxcvbnScoreTextProperty.set(score + "%");
    }

}

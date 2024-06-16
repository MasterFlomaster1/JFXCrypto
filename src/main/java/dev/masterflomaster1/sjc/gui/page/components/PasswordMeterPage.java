package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.crypto.passwords.PasswordStrengthCheckerService;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public final class PasswordMeterPage extends SimplePage {

    public static final String NAME = "Password Meter";

    private final TextField inputTextField = new TextField();
    private final TextField lengthTextField = new TextField();

    public PasswordMeterPage() {
        super();

        addPageHeader();
        addNode(titleNode());
        addNode(contentBox());
    }

    private Node titleNode() {

        var description = BBCodeParser.createFormattedText("""
            Evaluates the strength of an entered password locally
            """
        );

        return new VBox(description);
    }

    private Node contentBox() {
        inputTextField.setPromptText("Enter password");
        lengthTextField.setEditable(false);

        var bar = new ProgressBar(0);
        bar.setPrefWidth(300);
        bar.setMaxHeight(300);

        inputTextField.textProperty().addListener(event -> {
            String value = inputTextField.getText();

            int score = PasswordStrengthCheckerService.getInstance().getChecker().getStrengthScore(value);

            System.out.println(score);
            bar.setProgress(score);

            if (score <= 50) {
                bar.setProgress(1);
                bar.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            } else if (score <= 75)
                bar.pseudoClassStateChanged(Styles.STATE_WARNING, true);
            else
                bar.pseudoClassStateChanged(Styles.STATE_SUCCESS, true);

            lengthTextField.setText("Length: " + value.length());
        });

//        Styles

        return new FlowPane(
                HGAP_20, VGAP_10, inputTextField, lengthTextField, bar
        );
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

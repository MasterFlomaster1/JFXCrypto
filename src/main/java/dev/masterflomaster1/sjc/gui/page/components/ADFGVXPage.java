package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.crypto.impl.ADFGVXImpl;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import dev.masterflomaster1.sjc.utils.StringUtils;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

public final class ADFGVXPage extends SimplePage {

    public static final String NAME = "ADFGVX";

    private final TextArea inputTextArea = new TextArea();
    private final TextArea outputTextArea = new TextArea();
    private final TextField keyTextField = new TextField();
    private final Label counterLabel = new Label("", new FontIcon(Material2AL.LABEL));

    private final ToggleButton unblockedModeToggleBtn = new ToggleButton("Unblocked");
    private final ToggleButton blocksOf2ModeToggleBtn = new ToggleButton("Blocks of 2");
    private final ToggleButton blocksOf5ModeToggleBtn = new ToggleButton("Blocks of 5");

    public ADFGVXPage() {
        super();

        addSection("ADFGVX", section());
        onInit();
    }

    private Node section() {
        var description = BBCodeParser.createFormattedText("The ADFGX, later extended by ADFGVX, was a field " +
                "cipher used by the German Army during WWI.");

        inputTextArea.setPromptText("Enter text to encrypt / decrypt");
        inputTextArea.setWrapText(true);
        outputTextArea.setPromptText("Result");
        outputTextArea.setWrapText(true);

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> action(true));
        decryptButton.setOnAction(event -> action(false));

        var toggleGroup = new ToggleGroup();
        unblockedModeToggleBtn.getStyleClass().add(Styles.LEFT_PILL);
        blocksOf2ModeToggleBtn.getStyleClass().add(Styles.CENTER_PILL);
        blocksOf5ModeToggleBtn.getStyleClass().add(Styles.RIGHT_PILL);
        blocksOf5ModeToggleBtn.setSelected(true);

        toggleGroup.getToggles().addAll(unblockedModeToggleBtn, blocksOf2ModeToggleBtn, blocksOf5ModeToggleBtn);

        var boxOf3 = new HBox(unblockedModeToggleBtn, blocksOf2ModeToggleBtn, blocksOf5ModeToggleBtn);

        var controlsHBox = new HBox(
                20, encryptButton, decryptButton, boxOf3
        );

        counterLabel.getStyleClass().add(Styles.SUCCESS);

        return new VBox(
                20,
                description,
                inputTextArea,
                keyTextField,
                controlsHBox,
                outputTextArea,
                counterLabel
        );
    }

    private void action(boolean encrypt) {
        if (inputTextArea.getText().isEmpty() || keyTextField.getText().isEmpty())
            return;

        String value;

        if (encrypt) {
            value = ADFGVXImpl.encrypt(inputTextArea.getText(), keyTextField.getText());
            counterLabel.setText("Encoded %d chars".formatted(value.length()));
        } else {
            value = ADFGVXImpl.decrypt(inputTextArea.getText(), keyTextField.getText());
            counterLabel.setText("Decoded %d chars".formatted(value.length()));
        }

        if (blocksOf2ModeToggleBtn.isSelected())
            value = StringUtils.spaceAfterN(value, 2);
        else if (blocksOf5ModeToggleBtn.isSelected())
            value = StringUtils.spaceAfterN(value, 5);

        outputTextArea.setText(value);

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

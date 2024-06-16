package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.crypto.impl.ADFGVXImpl;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import dev.masterflomaster1.sjc.utils.StringUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public final class ADFGVXPage extends SimplePage {

    public static final String NAME = "ADFGVX";

    private final TextArea inputTextArea = new TextArea();
    private final TextField outputTextField = new TextField();
    private final TextField keyTextField = new TextField();

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

        Label label = new Label("Plaintext");

        var runButton = new Button("Run");
        runButton.setOnAction(event -> action());

        var toggleGroup = new ToggleGroup();
        unblockedModeToggleBtn.getStyleClass().add(Styles.LEFT_PILL);
        blocksOf2ModeToggleBtn.getStyleClass().add(Styles.CENTER_PILL);
        blocksOf5ModeToggleBtn.getStyleClass().add(Styles.RIGHT_PILL);
        blocksOf5ModeToggleBtn.setSelected(true);

        toggleGroup.getToggles().addAll(unblockedModeToggleBtn, blocksOf2ModeToggleBtn, blocksOf5ModeToggleBtn);

        var boxOf3 = new HBox(unblockedModeToggleBtn, blocksOf2ModeToggleBtn, blocksOf5ModeToggleBtn);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        GridPane.setColumnSpan(inputTextArea, 2);
        GridPane.setColumnSpan(outputTextField, 2);

        grid.addRow(0, description);
        grid.addRow(1, label);
        grid.addRow(2, inputTextArea);
        grid.addRow(3, outputTextField);
        grid.add(boxOf3, 1, 4);
        grid.addRow(5, keyTextField);
        grid.addRow(6, runButton);

        return grid;
    }

    private void action() {
        if (inputTextArea.getText().isEmpty() || keyTextField.getText().isEmpty())
            return;

        var val = ADFGVXImpl.encrypt(inputTextArea.getText(), keyTextField.getText());

        if (blocksOf2ModeToggleBtn.isSelected())
            val = StringUtils.spaceAfterN(val, 2);
        else if (blocksOf5ModeToggleBtn.isSelected())
            val = StringUtils.spaceAfterN(val, 5);

        outputTextField.setText(val);

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

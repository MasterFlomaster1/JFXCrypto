package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.HashTextViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class HashTextPage extends SimplePage {

    public static final String NAME = "Hash Text";

    private final Map<String, TextField> fields = new HashMap<>();

    private final TextField inputTextField = new TextField();
    private final ToggleButton hexModeToggleBtn = new ToggleButton("Hex");
    private final ToggleButton b64ModeToggleBtn = new ToggleButton("Base64");

    private ToggleGroup toggleGroup;

    private final HashTextViewModel viewModel = new HashTextViewModel();

    public HashTextPage() {
        super();

        addSection("Hash Text", mainSection());
        bindComponents();

        viewModel.onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText("""
            Calculate hashes for input text
            
            [ul]
            [li]For [code]HARAKA-256[/code] - input [color="-color-danger-fg"]must be exactly 32 bytes[/color].[/li]
            [li]For [code]HARAKA-512[/code] - input [color="-color-danger-fg"]must be exactly 64 bytes[/color].[/li]
            [/ul]"""
        );

        inputTextField.setPromptText("Enter text");

        toggleGroup = new ToggleGroup();
        hexModeToggleBtn.setSelected(true);
        hexModeToggleBtn.setToggleGroup(toggleGroup);
        b64ModeToggleBtn.setToggleGroup(toggleGroup);
        hexModeToggleBtn.getStyleClass().add(Styles.LEFT_PILL);
        b64ModeToggleBtn.getStyleClass().add(Styles.RIGHT_PILL);

        var outputModeHBox = new HBox(hexModeToggleBtn, b64ModeToggleBtn);

        hexModeToggleBtn.setOnAction(event -> viewModel.action());
        b64ModeToggleBtn.setOnAction(event -> viewModel.action());

        var controlsHBox = new HBox(
                20,
                inputTextField,
                outputModeHBox
        );

        return new VBox(
                20,
                description,
                controlsHBox,
                createNode()
        );
    }

    private Node createNode() {
        var grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);

        Set<String> set = SecurityUtils.getDigests();

        int index = 0;

        for (String item : set) {
            TextField tf = new TextField();
            tf.setPrefWidth(500);
            tf.setEditable(false);

            fields.put(item, tf);

            grid.addRow(index, new Label(item), tf, UIElementFactory.createCopyButton(tf));
            index++;
        }

        return grid;
    }

    private void bindComponents() {
        inputTextField.textProperty().bindBidirectional(viewModel.inputTextProperty());

        hexModeToggleBtn.selectedProperty().bindBidirectional(viewModel.hexModeToggleButtonPropertyProperty());
        b64ModeToggleBtn.selectedProperty().bindBidirectional(viewModel.b64ModeToggleButtonPropertyProperty());

        fields.forEach((algo, textField) -> {
            var stringProperty = new SimpleStringProperty();

            viewModel.getHashOutputMap().put(algo, stringProperty);
            textField.textProperty().bindBidirectional(stringProperty);
        });

        hexModeToggleBtn.setSelected(true);
        toggleGroup.selectedToggleProperty().addListener(viewModel::onToggleChanged);
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

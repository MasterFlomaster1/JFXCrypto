package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.HashTextViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class HashTextPage extends AbstractByteFormattingView {

    public static final String NAME = "Hash Text";

    private final Map<String, TextField> fields = new HashMap<>();

    private final TextField inputTextField = new TextField();

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

        var outputModeHBox = new HBox(hexModeToggleBtn, b64ModeToggleBtn);

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

        hexModeToggleBtn.selectedProperty().bindBidirectional(viewModel.hexModeToggleButtonProperty());
        b64ModeToggleBtn.selectedProperty().bindBidirectional(viewModel.b64ModeToggleButtonProperty());

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

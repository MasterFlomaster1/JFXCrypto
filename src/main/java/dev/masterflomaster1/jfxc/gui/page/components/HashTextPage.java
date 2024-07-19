package dev.masterflomaster1.jfxc.gui.page.components;

import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import dev.masterflomaster1.jfxc.crypto.UnkeyedCryptoHash;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;
import java.util.Set;

public final class HashTextPage extends SimplePage {

    public static final String NAME = "Hash Text";

    private final Map<String, TextField> fields = new HashMap<>();

    private final TextField inputTextField = new TextField();
    private final ToggleButton hexModeToggleBtn = new ToggleButton("Hex");
    private final ToggleButton b64ModeToggleBtn = new ToggleButton("Base64");

    public HashTextPage() {
        super();

        addSection("Hash Text", titleNode());
        addNode(workBox());
        addNode(createNode());

        onInit();
    }

    private Node titleNode() {

        var description = BBCodeParser.createFormattedText("""
            Calculate hashes for input text
            
            [ul]
            [li]For [code]HARAKA-256[/code] - input [color="-color-danger-fg"]must be exactly 32 bytes[/color].[/li]
            [li]For [code]HARAKA-512[/code] - input [color="-color-danger-fg"]must be exactly 64 bytes[/color].[/li]
            [/ul]"""
        );

        return new VBox(description);
    }

    private Node workBox() {
        var runButton = new Button("Run");
        runButton.setOnAction(event -> calculate());

        inputTextField.setPromptText("Enter text");
        inputTextField.setOnAction(event -> calculate());

        var toggleGroup = new ToggleGroup();
        hexModeToggleBtn.setSelected(true);
        hexModeToggleBtn.setToggleGroup(toggleGroup);
        b64ModeToggleBtn.setToggleGroup(toggleGroup);
        hexModeToggleBtn.getStyleClass().add(Styles.LEFT_PILL);
        b64ModeToggleBtn.getStyleClass().add(Styles.RIGHT_PILL);

        var outputModeHBox = new HBox(hexModeToggleBtn, b64ModeToggleBtn);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                oldValue.setSelected(true);
            }
        });

        hexModeToggleBtn.setOnAction(event -> calculate());
        b64ModeToggleBtn.setOnAction(event -> calculate());

        return new FlowPane(
                20,
                10,
                inputTextField,
                runButton,
                outputModeHBox
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

            grid.addRow(index, new Label(item), tf, createCopyBtn(tf));
            index++;
        }

        return grid;
    }

    private Button createCopyBtn(TextField textField) {
        var copyBtn = new Button(null, new FontIcon(Feather.COPY));
        copyBtn.setOnAction(event -> {
            var cc = new ClipboardContent();
            cc.putString(textField.getText());
            Clipboard.getSystemClipboard().setContent(cc);
        });

        return copyBtn;
    }

    private void calculate() {
        byte[] value = inputTextField.getText().getBytes(StandardCharsets.UTF_8);

        fields.forEach((k, v) -> {
            try {
                v.setText(output(UnkeyedCryptoHash.hash(k, value)));
            } catch (Exception e) {
                System.out.println(k + " " + e.getMessage());
            }
        });
    }

    private String output(byte[] value) {
        if (hexModeToggleBtn.isSelected())
            return HexFormat.of().formatHex(value);

        if (b64ModeToggleBtn.isSelected())
            return Base64.getEncoder().encodeToString(value);

        return "";
    }

    @Override
    public String getName() {
        return NAME;
    }

}

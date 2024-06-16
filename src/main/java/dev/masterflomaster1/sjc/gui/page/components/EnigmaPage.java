package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.crypto.enigma.Enigma;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import dev.masterflomaster1.sjc.utils.StringUtils;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public final class EnigmaPage extends SimplePage {

    public static final String NAME = "Enigma";

    private final TextField inputTextField = new TextField();
    private final TextField outputTextField = new TextField();

    private ComboBox<String> rotor1Type;
    private ComboBox<String> rotor2Type;
    private ComboBox<String> rotor3Type;

    private Spinner<Integer> rotor1Position;
    private Spinner<Integer> rotor2Position;
    private Spinner<Integer> rotor3Position;

    private Spinner<Integer> rotor1Ring;
    private Spinner<Integer> rotor2Ring;
    private Spinner<Integer> rotor3Ring;

    public EnigmaPage() {
        super();

        addSection("Enigma", create());
        addSection("Input", inputSection());

        onInit();
    }

    private Node create() {
        var description = BBCodeParser.createFormattedText("An enigma machine is a mechanical encryption " +
                "device that saw a lot of use before and during WW2. This code simulates a 3 rotor enigma, including " +
                "the 8 rotors commonly seen during the war.");

        ComboBox<String> reflectors = new ComboBox<>();
        reflectors.getItems().setAll("UKW B", "UKW C");

        var types = List.of("I", "II", "III", "IV", "V", "VI", "VII", "VIII");
        rotor1Type = new ComboBox<>();
        rotor2Type = new ComboBox<>();
        rotor3Type = new ComboBox<>();

        rotor1Type.getItems().setAll(types);
        rotor2Type.getItems().setAll(types);
        rotor3Type.getItems().setAll(types);

        rotor1Position = new Spinner<>(1, 26, 1);
        rotor2Position = new Spinner<>(1, 26, 1);
        rotor3Position = new Spinner<>(1, 26, 1);

        rotor1Position.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        rotor2Position.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        rotor3Position.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);

        rotor1Ring = new Spinner<>(1, 26, 1);
        rotor2Ring = new Spinner<>(1, 26, 1);
        rotor3Ring = new Spinner<>(1, 26, 1);

        rotor1Ring.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        rotor2Ring.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        rotor3Ring.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        grid.addRow(0,new Text("ROTOR 1"), rotor1Type, rotor1Position, rotor1Ring);
        grid.addRow(1, new Text("ROTOR 2"), rotor2Type, rotor2Position, rotor2Ring);
        grid.addRow(2, new Text("ROTOR 3"), rotor3Type, rotor3Position, rotor3Ring);

        return new VBox(
                10.0, description, reflectors, grid
        );
    }

    private Node inputSection() {
        var runButton = new Button("Run");
        runButton.setOnAction(event -> calculate());

        inputTextField.setPromptText("Enter text");
        inputTextField.setOnAction(event -> calculate());

        outputTextField.setEditable(false);

        return new FlowPane(inputTextField, runButton, outputTextField);
    }

    private void calculate() {

        Enigma enigma = new Enigma(
                new String[]{
                        rotor1Type.getValue(),
                        rotor2Type.getValue(),
                        rotor3Type.getValue()
                },
                "B",
                new int[]{
                        rotor1Position.getValue(),
                        rotor2Position.getValue(),
                        rotor3Position.getValue()
                },
                new int[]{
                        rotor1Ring.getValue(),
                        rotor2Ring.getValue(),
                        rotor3Ring.getValue()
                },
                ""
        );

        String val = new String(enigma.encrypt(inputTextField.getText()));

        outputTextField.setText(StringUtils.spaceAfterN(val, 5));

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

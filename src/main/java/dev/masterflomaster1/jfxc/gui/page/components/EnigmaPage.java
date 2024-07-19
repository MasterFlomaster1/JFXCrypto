package dev.masterflomaster1.jfxc.gui.page.components;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.enigma.Enigma;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.utils.StringUtils;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class EnigmaPage extends SimplePage {

    public static final String NAME = "Enigma";

    private final TextArea inputTextArea = UIElementFactory.createInputTextArea("Enter text to encrypt / decrypt");
    private final TextArea outputTextArea = UIElementFactory.createOuputTextArea("Result");
    private final TextField plugboardTextField = new TextField();

    private final ComboBox<String> reflectors = new ComboBox<>();

    private ComboBox<String> rotor1Type = new ComboBox<>();
    private ComboBox<String> rotor2Type = new ComboBox<>();
    private ComboBox<String> rotor3Type = new ComboBox<>();

    private Spinner<Integer> rotor1Position;
    private Spinner<Integer> rotor2Position;
    private Spinner<Integer> rotor3Position;

    private Spinner<Integer> rotor1Ring;
    private Spinner<Integer> rotor2Ring;
    private Spinner<Integer> rotor3Ring;

    private final ToggleButton unblockedModeToggleBtn = new ToggleButton("Unblocked");
    private final ToggleButton blocksOf5ModeToggleBtn = new ToggleButton("Blocks of 5");

    public EnigmaPage() {
        super();

        addSection("Enigma", mainSection());

        onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText("An enigma machine is a mechanical encryption " +
                "device that saw a lot of use before and during WW2. This code simulates a 3 rotor enigma, including " +
                "the 8 rotors commonly seen during the war.");

        reflectors.getItems().setAll("UKW B", "UKW C");
        reflectors.getSelectionModel().selectFirst();
        var reflectorLabel = new Label("Reflector");
        var reflectorGroup = new InputGroup(reflectorLabel, reflectors);

        var types = List.of("I", "II", "III", "IV", "V", "VI", "VII", "VIII");
        rotor1Type = new ComboBox<>();
        rotor2Type = new ComboBox<>();
        rotor3Type = new ComboBox<>();

        rotor1Type.getItems().setAll(types);
        rotor2Type.getItems().setAll(types);
        rotor3Type.getItems().setAll(types);

        rotor1Type.getSelectionModel().selectFirst();
        rotor2Type.getSelectionModel().selectFirst();
        rotor3Type.getSelectionModel().selectFirst();

        rotor1Type.valueProperty().addListener(this::onRotorTypeChange);
        rotor2Type.valueProperty().addListener(this::onRotorTypeChange);
        rotor3Type.valueProperty().addListener(this::onRotorTypeChange);

        rotor1Position = new Spinner<>(1, 26, 1);
        rotor2Position = new Spinner<>(1, 26, 1);
        rotor3Position = new Spinner<>(1, 26, 1);

        rotor1Position.valueProperty().addListener(this::onRotorPosChange);
        rotor2Position.valueProperty().addListener(this::onRotorPosChange);
        rotor3Position.valueProperty().addListener(this::onRotorPosChange);

        rotor1Position.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        rotor2Position.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        rotor3Position.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);

        rotor1Ring = new Spinner<>(1, 26, 1);
        rotor2Ring = new Spinner<>(1, 26, 1);
        rotor3Ring = new Spinner<>(1, 26, 1);

        rotor1Ring.valueProperty().addListener(this::onRotorPosChange);
        rotor2Ring.valueProperty().addListener(this::onRotorPosChange);
        rotor3Ring.valueProperty().addListener(this::onRotorPosChange);

        rotor1Ring.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        rotor2Ring.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        rotor3Ring.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        grid.addRow(0, new Text("ROTOR 1"), rotor1Type, rotor1Position, rotor1Ring);
        grid.addRow(1, new Text("ROTOR 2"), rotor2Type, rotor2Position, rotor2Ring);
        grid.addRow(2, new Text("ROTOR 3"), rotor3Type, rotor3Position, rotor3Ring);

        inputTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue.equals(newValue)) return;

            action();
        });

        var plugboardLabel = new Label("Plugboard");
        var plugboardGroup = new InputGroup(plugboardLabel, plugboardTextField);
        plugboardTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue.equals(newValue)) return;

            if (isValidPlugboard(newValue.trim())) {
                plugboardTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
                action();
            } else {
                plugboardTextField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            }
        });

        var toggleGroup = new ToggleGroup();
        unblockedModeToggleBtn.setToggleGroup(toggleGroup);
        blocksOf5ModeToggleBtn.setToggleGroup(toggleGroup);
        unblockedModeToggleBtn.getStyleClass().add(Styles.LEFT_PILL);
        blocksOf5ModeToggleBtn.getStyleClass().add(Styles.RIGHT_PILL);
        blocksOf5ModeToggleBtn.setSelected(true);
        var outputModeHBox = new HBox(unblockedModeToggleBtn, blocksOf5ModeToggleBtn);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                oldValue.setSelected(true);
                return;
            }

            if (oldValue == null)
                return;

            if (outputTextArea.getText().isEmpty())
                return;

            var val = outputTextArea.getText();

            if (unblockedModeToggleBtn.isSelected()) {
                outputTextArea.setText(StringUtils.removeSpaces(val));
            } else if (blocksOf5ModeToggleBtn.isSelected()) {
                outputTextArea.setText(StringUtils.spaceAfterN(val, 5));
            }
        });

        var copyResultButton = UIElementFactory.createCopyButton(outputTextArea);
        var footerHBox = new HBox(
                20,
                copyResultButton,
                outputModeHBox,
                counterLabel
        );
        footerHBox.setAlignment(Pos.CENTER_LEFT);

        return new VBox(
                20,
                description,
                reflectorGroup,
                grid,
                plugboardGroup,
                inputTextArea,
                outputTextArea,
                footerHBox
        );
    }

    private void onRotorTypeChange(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (oldValue.equals(newValue)) return;

        action();
    }

    private void onRotorPosChange(ObservableValue<? extends Integer> observable, int oldValue, int newValue) {
        if (oldValue == newValue) return;

        action();
    }

    private boolean isValidPlugboard(String input) {
        if (input.isEmpty()) return true;

        String[] pairsArray = input.split(" ");

        Set<String> pairs = new HashSet<>();
        Set<Character> characters = new HashSet<>();

        for (String pair : pairsArray) {
            if (pair.length() != 2) {
                return false;
            }

            char firstChar = pair.charAt(0);
            char secondChar = pair.charAt(1);

            if (!Character.isLetter(firstChar) || !Character.isLetter(secondChar)) {
                return false;
            }

            if (!characters.add(firstChar) || !characters.add(secondChar) || !pairs.add(pair)) {
                return false;
            }
        }

        return true;
    }

    private void action() {
        if (inputTextArea.getText().isEmpty())
            return;

        if (!plugboardTextField.getText().isEmpty() && !isValidPlugboard(plugboardTextField.getText().trim()))
            return;

        String ref = (reflectors.getValue().equals("UKW B")) ? "B" : "C";

        Enigma enigma = new Enigma(
                new String[]{
                        rotor1Type.getValue(),
                        rotor2Type.getValue(),
                        rotor3Type.getValue()
                },
                ref,
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
                plugboardTextField.getText().toUpperCase()
        );

        String input = StringUtils.removePunctuation(inputTextArea.getText());
        String val = new String(enigma.encrypt(input));

        if (unblockedModeToggleBtn.isSelected()) {
            outputTextArea.setText(val);
        } else {
            outputTextArea.setText(StringUtils.spaceAfterN(val, 5));
        }

        counterLabel.setText("Encoded %d chars".formatted(val.length()));

    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onInit() {
        reflectors.getSelectionModel().select(MemCache.readInteger("enigma.reflector", 0));
        rotor1Type.getSelectionModel().select(MemCache.readInteger("enigma.rotor1.type", 0));
        rotor2Type.getSelectionModel().select(MemCache.readInteger("enigma.rotor2.type", 0));
        rotor3Type.getSelectionModel().select(MemCache.readInteger("enigma.rotor3.type", 0));
        rotor1Position.getValueFactory().setValue(MemCache.readInteger("enigma.rotor1.pos", 0));
        rotor2Position.getValueFactory().setValue(MemCache.readInteger("enigma.rotor2.pos", 0));
        rotor3Position.getValueFactory().setValue(MemCache.readInteger("enigma.rotor3.pos", 0));
        rotor1Ring.getValueFactory().setValue(MemCache.readInteger("enigma.ring1", 0));
        rotor2Ring.getValueFactory().setValue(MemCache.readInteger("enigma.ring2", 0));
        rotor3Ring.getValueFactory().setValue(MemCache.readInteger("enigma.ring3", 0));
        inputTextArea.setText(MemCache.readString("enigma.input", ""));
        plugboardTextField.setText(MemCache.readString("enigma.plugboard", ""));
        outputTextArea.setText(MemCache.readString("enigma.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeInteger("enigma.reflector", reflectors.getItems().indexOf(reflectors.getValue()));
        MemCache.writeInteger("enigma.rotor1.type", rotor1Type.getItems().indexOf(rotor1Type.getValue()));
        MemCache.writeInteger("enigma.rotor2.type", rotor2Type.getItems().indexOf(rotor2Type.getValue()));
        MemCache.writeInteger("enigma.rotor3.type", rotor3Type.getItems().indexOf(rotor3Type.getValue()));
        MemCache.writeInteger("enigma.rotor1.pos", rotor1Position.getValue());
        MemCache.writeInteger("enigma.rotor2.pos", rotor2Position.getValue());
        MemCache.writeInteger("enigma.rotor3.pos", rotor3Position.getValue());
        MemCache.writeInteger("enigma.ring1", rotor1Ring.getValue());
        MemCache.writeInteger("enigma.ring2", rotor2Ring.getValue());
        MemCache.writeInteger("enigma.ring3", rotor3Ring.getValue());
        MemCache.writeString("enigma.input", inputTextArea.getText());
        MemCache.writeString("enigma.plugboard", plugboardTextField.getText());
        MemCache.writeString("enigma.output", outputTextArea.getText());
    }
}

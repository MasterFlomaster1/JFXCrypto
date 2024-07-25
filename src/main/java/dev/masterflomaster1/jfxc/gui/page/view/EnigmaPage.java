package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.EnigmaViewModel;
import javafx.beans.binding.Bindings;
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

import java.util.List;

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

    private ToggleGroup toggleGroup;

    private final EnigmaViewModel viewModel = new EnigmaViewModel();

    public EnigmaPage() {
        super();

        addSection("Enigma", mainSection());
        bindComponents();

        viewModel.onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText("An enigma machine is a mechanical encryption " +
                "device that saw a lot of use before and during WW2. This code simulates a 3 rotor enigma, including " +
                "the 8 rotors commonly seen during the war.");

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

        grid.addRow(0, new Text("ROTOR 1"), rotor1Type, rotor1Position, rotor1Ring);
        grid.addRow(1, new Text("ROTOR 2"), rotor2Type, rotor2Position, rotor2Ring);
        grid.addRow(2, new Text("ROTOR 3"), rotor3Type, rotor3Position, rotor3Ring);

        var plugboardLabel = new Label("Plugboard");
        var plugboardGroup = new InputGroup(plugboardLabel, plugboardTextField);
        plugboardTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue == null || newValue == null) return;
            if (oldValue.equals(newValue)) return;

            if (viewModel.isValidPlugboard(newValue.trim())) {
                plugboardTextField.pseudoClassStateChanged(Styles.STATE_DANGER, false);
                viewModel.action();
            } else {
                plugboardTextField.pseudoClassStateChanged(Styles.STATE_DANGER, true);
            }
        });

        toggleGroup = new ToggleGroup();
        unblockedModeToggleBtn.setToggleGroup(toggleGroup);
        blocksOf5ModeToggleBtn.setToggleGroup(toggleGroup);
        unblockedModeToggleBtn.getStyleClass().add(Styles.LEFT_PILL);
        blocksOf5ModeToggleBtn.getStyleClass().add(Styles.RIGHT_PILL);

        var outputModeHBox = new HBox(unblockedModeToggleBtn, blocksOf5ModeToggleBtn);
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

    private void bindComponents() {
        inputTextArea.textProperty().bindBidirectional(viewModel.inputTextProperty());
        outputTextArea.textProperty().bindBidirectional(viewModel.outputTextProperty());
        plugboardTextField.textProperty().bindBidirectional(viewModel.plugboardTextProperty());
        counterLabel.textProperty().bindBidirectional(viewModel.counterTextProperty());

        reflectors.valueProperty().bindBidirectional(viewModel.reflectorsProperty());
        Bindings.bindContent(reflectors.getItems(), viewModel.getReflectorsList());

        rotor1Type.valueProperty().bindBidirectional(viewModel.rotor1TypeProperty());
        rotor2Type.valueProperty().bindBidirectional(viewModel.rotor2TypeProperty());
        rotor3Type.valueProperty().bindBidirectional(viewModel.rotor3TypeProperty());

        rotor1Position.getValueFactory().valueProperty().bindBidirectional(viewModel.rotor1PositionProperty().asObject());
        rotor2Position.getValueFactory().valueProperty().bindBidirectional(viewModel.rotor2PositionProperty().asObject());
        rotor3Position.getValueFactory().valueProperty().bindBidirectional(viewModel.rotor3PositionProperty().asObject());

        rotor1Ring.getValueFactory().valueProperty().bindBidirectional(viewModel.rotor1RingProperty().asObject());
        rotor2Ring.getValueFactory().valueProperty().bindBidirectional(viewModel.rotor2RingProperty().asObject());
        rotor3Ring.getValueFactory().valueProperty().bindBidirectional(viewModel.rotor3RingProperty().asObject());

        unblockedModeToggleBtn.selectedProperty().bindBidirectional(viewModel.unblockedModeToggleButtonProperty());
        blocksOf5ModeToggleBtn.selectedProperty().bindBidirectional(viewModel.blocksOf5ModeToggleButtonProperty());

        blocksOf5ModeToggleBtn.setSelected(true);

        inputTextArea.textProperty().addListener(viewModel::onInputTextChange);
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

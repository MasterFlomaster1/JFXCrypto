package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.theme.Styles;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.InputOutputAreaComponentViewModel;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.Getter;

import java.nio.charset.StandardCharsets;

@Getter
public class InputOutputAreaComponent {

    private final TextArea inputArea = new TextArea();
    private final TextArea outputArea = new TextArea();
    private final Label inputLengthLabel = new Label("Length");
    private final Label outputLengthLabel = new Label("Length");
    private final ComboBox<String> inputDisplayMode = new ComboBox<>();
    private final ComboBox<String> outputDisplayMode = new ComboBox<>();

    private final InputOutputAreaComponentViewModel viewModel = new InputOutputAreaComponentViewModel();

    InputOutputAreaComponent() {
        inputArea.setWrapText(true);
        outputArea.setWrapText(true);

        bindComponents();
    }

    GridPane createSection() {
        GridPane grid = new GridPane(10, 10);

        Label inputLabel = new Label("Input");
        Label outputLabel = new Label("Output");

        inputLengthLabel.getStyleClass().add(Styles.ACCENT);
        outputLengthLabel.getStyleClass().add(Styles.ACCENT);
        inputLengthLabel.setVisible(false);
        outputLengthLabel.setVisible(false);

        HBox inputLabelBox = new HBox(10, inputLabel, inputLengthLabel);
        HBox outputLabelBox = new HBox(10, outputLabel, outputLengthLabel);

        Button inputCopyButton = UIElementFactory.createCopyButton(inputArea);
        Button outputCopyButton = UIElementFactory.createCopyButton(outputArea);
        Button inputFileButton = new Button("Import File");
        Button outputFileButton = new Button("Export File");

        inputDisplayMode.getItems().addAll("String", "Hex", "Base64");
        outputDisplayMode.getItems().addAll("String", "Hex", "Base64");
        outputDisplayMode.setOnAction(viewModel::onOutputDisplayModeChanged);
        inputDisplayMode.getSelectionModel().selectFirst();
        outputDisplayMode.getSelectionModel().selectFirst();

        HBox inputBox = new HBox(5, inputCopyButton, inputFileButton, inputDisplayMode);
        HBox outputBox = new HBox(5, outputCopyButton, outputFileButton, outputDisplayMode);

        inputArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                var value = newValue.getBytes(StandardCharsets.UTF_8);

                inputLengthLabel.setVisible(true);
                inputLengthLabel.setText("bit length: %d".formatted(value.length));
            } else {
                inputLengthLabel.setVisible(false);
            }
        });

        outputArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                var value = newValue.getBytes(StandardCharsets.UTF_8);

                outputLengthLabel.setVisible(true);
                outputLengthLabel.setText("bit length: %d".formatted(value.length));
            } else {
                outputLengthLabel.setVisible(false);
            }
        });

        grid.add(inputLabelBox, 0, 0);
        grid.add(outputLabelBox, 1, 0);
        grid.add(inputArea, 0, 1);
        grid.add(outputArea, 1, 1);
        grid.add(inputBox, 0, 2);
        grid.add(outputBox, 1, 2);

        return grid;
    }

    private void bindComponents() {
        inputArea.textProperty().bindBidirectional(viewModel.getInputProperty());
        outputArea.textProperty().bindBidirectional(viewModel.getOutputProperty());
        inputLengthLabel.textProperty().bindBidirectional(viewModel.getInputLengthProperty());
        outputLengthLabel.textProperty().bindBidirectional(viewModel.getOutputLengthProperty());
        inputDisplayMode.valueProperty().bindBidirectional(viewModel.getInputDisplayModeProperty());
        outputDisplayMode.valueProperty().bindBidirectional(viewModel.getOutputDisplayModeProperty());
    }

}

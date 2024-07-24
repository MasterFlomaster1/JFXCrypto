package dev.masterflomaster1.jfxc.gui.page.components;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.AffineViewModel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class AffinePage extends SimplePage {

    public static final String NAME = "Affine Cipher";

    private final TextArea inputTextArea = UIElementFactory.createInputTextArea("Enter text to encrypt / decrypt");
    private final TextArea outputTextArea = UIElementFactory.createOuputTextArea("Result");
    private final ComboBox<Integer> slopeComboBox = new ComboBox<>();
    private final ComboBox<Integer> interceptComboBox = new ComboBox<>();

    private final AffineViewModel viewModel = new AffineViewModel();

    public AffinePage() {
        super();
        addSection("Affine Cipher", section());
        bindComponents();

        onInit();
    }

    private Node section() {
        var description = BBCodeParser.createFormattedText(
                "The Affine cipher is a monoalphabetic substitution cipher, where each letter in the alphabet" +
                        " is mapped to another letter through a simple mathematical formula: [code](ax + b) mod 26[/code]." +
                        " The number 26 represents the length of the alphabet and will be different for different languages. "
        );

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> viewModel.action(true));
        decryptButton.setOnAction(event -> viewModel.action(false));

        slopeComboBox.getItems().setAll(1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25);
        var slopeLabel = new Label("Slope (A)");
        var slopeGroup = new InputGroup(slopeLabel, slopeComboBox);

        interceptComboBox.getItems().setAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25);
        var interceptLabel = new Label("Intercept (B)");
        var interceptGroup = new InputGroup(interceptLabel, interceptComboBox);

        var controlsHBox = new HBox(
                20,
                encryptButton,
                decryptButton,
                slopeGroup,
                interceptGroup
        );

        var copyResultButton = UIElementFactory.createCopyButton(outputTextArea);
        var footerHBox = new HBox(
                20,
                copyResultButton,
                counterLabel
        );
        footerHBox.setAlignment(Pos.CENTER_LEFT);

        return new VBox(
                20,
                description,
                inputTextArea,
                controlsHBox,
                outputTextArea,
                footerHBox
        );
    }

    private void bindComponents() {
        inputTextArea.textProperty().bindBidirectional(viewModel.inputTextProperty());
        outputTextArea.textProperty().bindBidirectional(viewModel.outputTextProperty());
        slopeComboBox.valueProperty().bindBidirectional(viewModel.slopeProperty());
        interceptComboBox.valueProperty().bindBidirectional(viewModel.interceptProperty());
        counterLabel.textProperty().bindBidirectional(viewModel.counterTextProperty());
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onInit() {
        inputTextArea.setText(MemCache.readString("affine.input", ""));
        outputTextArea.setText(MemCache.readString("affine.output", ""));
        slopeComboBox.getSelectionModel().select(MemCache.readInteger("affine.slope", 0));
        interceptComboBox.getSelectionModel().select(MemCache.readInteger("affine.intercept", 0));
    }

    @Override
    public void onReset() {
        MemCache.writeString("affine.input", inputTextArea.getText());
        MemCache.writeString("affine.output", outputTextArea.getText());
        MemCache.writeInteger("affine.slope", slopeComboBox.getItems().indexOf(slopeComboBox.getValue()));
        MemCache.writeInteger("affine.intercept", interceptComboBox.getItems().indexOf(interceptComboBox.getValue()));
    }
}

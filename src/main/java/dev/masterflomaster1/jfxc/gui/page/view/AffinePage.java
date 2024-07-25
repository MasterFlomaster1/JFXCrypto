package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.AffineViewModel;
import javafx.beans.binding.Bindings;
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

        viewModel.onInit();
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

        var slopeLabel = new Label("Slope (A)");
        var slopeGroup = new InputGroup(slopeLabel, slopeComboBox);

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
        counterLabel.textProperty().bindBidirectional(viewModel.counterTextProperty());

        slopeComboBox.valueProperty().bindBidirectional(viewModel.slopeProperty());
        Bindings.bindContent(slopeComboBox.getItems(), viewModel.getSlopeList());

        interceptComboBox.valueProperty().bindBidirectional(viewModel.interceptProperty());
        Bindings.bindContent(interceptComboBox.getItems(), viewModel.getInterceptList());
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

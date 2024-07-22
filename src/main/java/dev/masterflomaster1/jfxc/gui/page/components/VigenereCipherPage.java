package dev.masterflomaster1.jfxc.gui.page.components;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.viewmodel.VigenereCipherViewModel;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class VigenereCipherPage extends SimplePage {

    public static final String NAME = "Vigenère Cipher";

    private final TextArea inputTextArea = UIElementFactory.createInputTextArea("Enter text to encrypt / decrypt");
    private final TextArea outputTextArea = UIElementFactory.createOuputTextArea("Result");
    private final TextField keyTextField = new TextField();
    private Timeline emptyKeyAnimation;

    private final VigenereCipherViewModel viewModel = new VigenereCipherViewModel();

    public VigenereCipherPage() {
        super();
        addSection("Vigenère Cipher", mainSection());
        bindComponents();
        onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Method of encrypting alphabetic text by using a series of interwoven Caesar ciphers based on " +
                        "the letters of a keyword."
        );

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> viewModel.action(true));
        decryptButton.setOnAction(event -> viewModel.action(false));

        var keyGroup = new InputGroup(keyLabel, keyTextField);
        emptyKeyAnimation = Animations.wobble(keyGroup);

        var controlsHBox = new HBox(
                20,
                encryptButton,
                decryptButton,
                keyGroup
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
        keyTextField.textProperty().bindBidirectional(viewModel.keyTextProperty());
        counterLabel.textProperty().bindBidirectional(viewModel.counterTextProperty());

        viewModel.setEmptyKeyAnimation(emptyKeyAnimation);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onInit() {
        inputTextArea.setText(MemCache.readString("vigenere.input", ""));
        outputTextArea.setText(MemCache.readString("vigenere.output", ""));
        keyTextField.setText(MemCache.readString("vigenere.key", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("vigenere.input", inputTextArea.getText());
        MemCache.writeString("vigenere.output", outputTextArea.getText());
        MemCache.writeString("vigenere.key", keyTextField.getText());
    }
}

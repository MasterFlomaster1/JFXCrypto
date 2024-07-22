package dev.masterflomaster1.jfxc.gui.page.components;

import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.viewmodel.AtbashViewModel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class AtbashPage extends SimplePage {

    public static final String NAME = "Atbash Cipher";

    private final TextArea inputTextArea = UIElementFactory.createInputTextArea("Enter text to encrypt / decrypt");
    private final TextArea outputTextArea = UIElementFactory.createOuputTextArea("Result");

    private final AtbashViewModel viewModel = new AtbashViewModel();

    public AtbashPage() {
        super();

        addSection("Atbash Cipher", mainSection());
        bindComponents();
        onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Originally used to encode the hebrew alphabet, Atbash is formed by mapping an alphabet to its" +
                        " reverse, so that the first letter becomes the last letter. The Atbash cipher can be seen as" +
                        " a special case of the affine cipher."
        );

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> viewModel.action(true));
        decryptButton.setOnAction(event -> viewModel.action(false));

        var controlsHBox = new HBox(
                20,
                encryptButton,
                decryptButton
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
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onInit() {
        inputTextArea.setText(MemCache.readString("atbash.input", ""));
        outputTextArea.setText(MemCache.readString("atbash.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("atbash.input", inputTextArea.getText());
        MemCache.writeString("atbash.output", outputTextArea.getText());
    }
}

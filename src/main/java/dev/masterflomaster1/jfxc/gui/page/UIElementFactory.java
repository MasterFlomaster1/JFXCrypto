package dev.masterflomaster1.jfxc.gui.page;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public final class UIElementFactory {

    private UIElementFactory() { }

    public static TextArea createInputTextArea(final String promptText, final int maxHeight) {
        TextArea inputTextArea = createInputTextArea(promptText);
        inputTextArea.setMaxHeight(maxHeight);
        return inputTextArea;
    }

    public static TextArea createInputTextArea(final String promptText) {
        TextArea inputTextArea = new TextArea();
        inputTextArea.setPromptText(promptText);
        inputTextArea.setWrapText(true);
        return inputTextArea;
    }

    public static TextArea createOuputTextArea(final String promptText) {
        TextArea outputTextArea = new TextArea();
        outputTextArea.setPromptText(promptText);
        outputTextArea.setWrapText(true);
        outputTextArea.setEditable(false);
        return outputTextArea;
    }

    public static TextArea createOuputTextArea(final String promptText, final int maxHeight) {
        TextArea outputTextArea = createOuputTextArea(promptText);
        outputTextArea.setMaxHeight(100);
        return outputTextArea;
    }

    public static Button createCopyButton(final TextArea textArea) {
        Button copyResultButton = new Button("Copy");
        copyResultButton.setOnAction(event -> {
            var cc = new ClipboardContent();
            cc.putString(textArea.getText());
            Clipboard.getSystemClipboard().setContent(cc);
        });
        return copyResultButton;
    }

}

package dev.masterflomaster1.jfxc.gui.page;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.HexFormat;

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
        outputTextArea.setMaxHeight(maxHeight);
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

    public static Button createCopyButton(final TextField textField) {
        Button copyResultButton = new Button(null, new FontIcon(Feather.COPY));
        copyResultButton.setOnAction(event -> {
            var cc = new ClipboardContent();
            cc.putString(textField.getText());
            Clipboard.getSystemClipboard().setContent(cc);
        });
        return copyResultButton;
    }

    public static VBox createPasswordSettingsModal(ComboBox<Integer> keyLengthComboBox, TextField keyField, ModalPane modalPane) {
        var header = new Label("Generate password based key with PBKDF2");
        header.getStyleClass().add(Styles.TITLE_4);

        var passwordTextField = new TextField();
        var passwordLabel = new Label("Password");
        var passwordGroup  = new InputGroup(passwordLabel, passwordTextField);

        var saltTextField = new TextField();
        var saltLabel = new Label("Salt");
        var saltShuffleButton = new Button("", new FontIcon(BootstrapIcons.SHUFFLE));
        var saltGroup = new InputGroup(saltLabel, saltTextField, saltShuffleButton);

        saltShuffleButton.setOnAction((e) -> {
            saltTextField.setText(HexFormat.of().formatHex(SecurityUtils.generateSalt()));
        });

        var generateButton = new Button("Generate");

        generateButton.setOnAction(event -> {
            if (passwordTextField.getText().isEmpty())
                return;

            if (saltTextField.getText().isEmpty())
                return;

            var key = SecurityUtils.generatePasswordBasedKey(
                    passwordTextField.getText().toCharArray(),
                    keyLengthComboBox.getValue(),
                    HexFormat.of().parseHex(saltTextField.getText())
            );

            keyField.setText(HexFormat.of().formatHex(key));
            modalPane.hide();
        });

        return new VBox(
                20,
                header,
                saltGroup,
                passwordGroup,
                generateButton
        );
    }

}

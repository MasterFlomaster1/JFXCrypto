package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.theme.Styles;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

/**
 * Abstract base class for views that handle byte data formatting and parsing.
 */
abstract class AbstractByteFormattingView extends SimplePage {

    final TextArea outputTextArea = UIElementFactory.createOuputTextArea("Result", 100);
    final ToggleButton hexModeToggleBtn = new ToggleButton("Hex");
    final ToggleButton b64ModeToggleBtn = new ToggleButton("Base64");
    final ToggleGroup toggleGroup;

    AbstractByteFormattingView() {
        toggleGroup = new ToggleGroup();
        hexModeToggleBtn.setToggleGroup(toggleGroup);
        b64ModeToggleBtn.setToggleGroup(toggleGroup);
        hexModeToggleBtn.getStyleClass().add(Styles.LEFT_PILL);
        b64ModeToggleBtn.getStyleClass().add(Styles.RIGHT_PILL);

//        outputTextArea.setStyle("-fx-font-family: 'Monospaced'; -fx-font-weight: bold;");
    }

    Node createFormattingOutputArea() {
        var outputModeHBox = new HBox(hexModeToggleBtn, b64ModeToggleBtn);

        var copyButton = UIElementFactory.createCopyButton(outputTextArea);
        var footerHBox = new HBox(
                20,
                copyButton,
                outputModeHBox,
                counterLabel
        );
        footerHBox.setAlignment(Pos.CENTER_LEFT);

        return footerHBox;
    }

}

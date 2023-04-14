package GUI.Controllers;

import GUI.Main.GUI;
import Hash.CurrentHash;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TextHashPageController {

    public ImageView menuImage;

    @FXML
    private TextArea inputText;

    @FXML
    private TextArea outputText;

    public void initialize() {
        inputText.setPromptText("Input text");
        inputText.setFocusTraversable(false);
        outputText.setPromptText("Output text");
    }

    public void hashButtonAction() {
        outputText.setText(CurrentHash.hashText(inputText.getText()));
    }

    public void menuButtonPressed() {
        menuImage.setImage(new javafx.scene.image.Image("/menu2.png"));
        GUI.menuButtonPressed();
    }

    public void menuButtonRelease() {
        menuImage.setImage(new javafx.scene.image.Image("/menu1.png"));
    }

    public void mouseEntered() {
        menuImage.setImage(new Image("/menu2.png"));
    }

}

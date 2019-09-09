package GUI.Controllers;

import Cipher.CurrentCipher;
import GUI.Main.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TextEncryptionPageController {

    public ImageView menuImage;

    @FXML
    private TextArea inputText;

    @FXML
    private TextArea outputText;

    public void encrypt() {
        outputText.setText(CurrentCipher.encrypt(inputText.getText()));
    }

    public void decrypt() {
        outputText.setText(CurrentCipher.decrypt(inputText.getText()));
    }

    public void initialize() {
        inputText.setPromptText("Input text");
        inputText.setFocusTraversable(false);
        outputText.setPromptText("Output text");
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

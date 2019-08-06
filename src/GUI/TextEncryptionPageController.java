package GUI;

import Cipher.CurrentCipher;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class TextEncryptionPageController {
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



    public void setParentPage(PageSwitcher page) {

        inputText.setPromptText("Input text");
        inputText.setFocusTraversable(false);
        outputText.setPromptText("Output text");
    }


}

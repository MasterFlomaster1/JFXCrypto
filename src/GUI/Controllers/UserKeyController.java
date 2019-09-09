package GUI.Controllers;

import Cipher.CurrentCipher;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserKeyController {

    @FXML
    private Text cipherName;

    @FXML
    private TextField userKeyField;

    public void initialize() {
        cipherName.setText("Enter "+CurrentCipher.getCurrentCipherName()+" key: ");
    }

    public void enterKey() {
        String userKey = userKeyField.getText();
        CurrentCipher.setKey(userKey);
        Stage stage = (Stage) userKeyField.getScene().getWindow();
        stage.close();
    }

}

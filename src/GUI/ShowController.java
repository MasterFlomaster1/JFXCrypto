package GUI;

import Cipher.CurrentCipher;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ShowController {


    @FXML
    private Text cipherName;

    @FXML
    private TextField keyField;

    public void initialize() {
        cipherName.setText(CurrentCipher.getCurrentCipherName()+" KEY");
        keyField.setText(CurrentCipher.getKey());
        keyField.setStyle("-fx-background-color:  #0d0d0d;");
    }

}

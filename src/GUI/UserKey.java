package GUI;

import Cipher.Aes256;
import Cipher.CurrentCipher;
import Cipher.SimpleCipher;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

class UserKey {

    static void getKeyFromUser() {

        Label secondLabel = new Label();
        StackPane secondaryLayout = new StackPane();

        Scene secondScene = new Scene(secondaryLayout, 230, 100);
        Stage newWindow = new Stage();
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.setTitle("Enter key");

        Text text = new Text();
        switch (CurrentCipher.getCurrentCipher()) {
            case CurrentCipher.AES:
                text.setText("AES key");
                break;
            case CurrentCipher.AES256:
                text.setText("AES-256 key");
                break;
            case CurrentCipher.SimpleCipher:
                text.setText("SimpleCipher key");
                break;
        }
        secondaryLayout.getChildren().add(text);
        secondaryLayout.getChildren().add(secondLabel);

        TextField textField = new TextField();
        textField.setOnAction(event -> {
            String userKey = textField.getText();

            switch (CurrentCipher.getCurrentCipher()) {
                case CurrentCipher.AES:
                    break;
                case CurrentCipher.AES256:
                    if (userKey.length()!=16) {
                        AlertDialog.showError("AES-256 key length must be 16 symbols!");
                    } else {
                        Aes256.setKey(userKey);
                        newWindow.close();
                    }
                    break;
                case CurrentCipher.SimpleCipher:
                    SimpleCipher.updateCombination();
                    break;
            }
        });
        secondaryLayout.getChildren().add(textField);
        newWindow.setScene(secondScene);
        newWindow.show();

    }

}

package GUI;

import Cipher.Aes256;
import Cipher.CurrentCipher;
import Cipher.SimpleCipher;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

class UserKey {

    static void getKeyFromUser() {

        Label secondLabel = new Label();
        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);

        Scene secondScene = new Scene(secondaryLayout, 230, 100);
        Stage newWindow = new Stage();
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.setTitle("Enter key");
        TextField textField = new TextField();
        textField.setOnAction(event -> {
            String userKey = textField.getText();

            switch (CurrentCipher.getCurrentCipher()) {
                case CurrentCipher.AES:
                    break;
                case CurrentCipher.AES256:
                    if (userKey.length()!=16) {
                        AlertWindows.showError("AES-256 key length must be 16 symbols!");
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

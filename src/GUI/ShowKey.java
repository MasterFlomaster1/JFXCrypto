package GUI;

import Cipher.Aes256;
import Cipher.CurrentCipher;
import Cipher.SimpleCipher;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

class ShowKey {

    static void showKey() {
        Label secondLabel = new Label();
        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);

        Scene secondScene = new Scene(secondaryLayout, 230, 100);
        Stage newWindow = new Stage();

        String key = "0";
        switch (CurrentCipher.getCurrentCipher()) {
            case CurrentCipher.AES:
                break;
            case CurrentCipher.AES256:
                key = Aes256.getKey();
                newWindow.close();
                break;
            case CurrentCipher.SimpleCipher:
                key = SimpleCipher.getKey();
                newWindow.close();
                break;
        }
        newWindow.setTitle("Key");
        TextField textField = new TextField(key);
        secondaryLayout.getChildren().add(textField);
        newWindow.setScene(secondScene);
        newWindow.show();
    }

}

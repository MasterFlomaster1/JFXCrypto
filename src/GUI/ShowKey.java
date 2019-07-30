package GUI;

import Cipher.Aes256;
import Cipher.CurrentCipher;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

class ShowKey {

    static void showKey() {
        String key = "0";
        switch (CurrentCipher.getCurrentCipher()) {
            case CurrentCipher.AES:
                break;
            case CurrentCipher.AES256:
                key = Aes256.getKey();
                break;
            case CurrentCipher.SimpleCipher:
                break;
        }
        Label secondLabel = new Label("I'm a Label on new Window");
        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);

        Scene secondScene = new Scene(secondaryLayout, 230, 100);
        Stage newWindow = new Stage();
        newWindow.setTitle("Key");
        TextField textField = new TextField(key);
        secondaryLayout.getChildren().add(textField);
        newWindow.setScene(secondScene);
        newWindow.show();
    }

}

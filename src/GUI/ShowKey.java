package GUI;

import Cipher.CurrentCipher;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

class ShowKey {

    void showKey() {
        try {
            Stage newWindow = new Stage();
            Parent content = FXMLLoader.load(getClass().getResource("ShowKey.fxml"));
            Scene scene = new Scene(content);
            newWindow.setTitle("key");
            newWindow.setScene(scene);
            newWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

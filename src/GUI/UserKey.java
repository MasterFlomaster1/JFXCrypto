package GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

class UserKey {

    static void getKeyFromUser() {
        try {
            Parent content = FXMLLoader.load(UserKey.class.getResource("UserKey.fxml"));
            Scene scene = new Scene(content);
            Stage userKey = new Stage();
            userKey.getIcons().add(new Image("/SJC1.png"));
            userKey.initModality(Modality.APPLICATION_MODAL);
            userKey.setTitle("Enter key");
            userKey.setScene(scene);
            userKey.setResizable(false);
            userKey.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

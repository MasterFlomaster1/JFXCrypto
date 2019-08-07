package GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

class UserKey {

    static void getKeyFromUser() {
        try {
            Parent content = FXMLLoader.load(UserKey.class.getResource("UserKey.fxml"));
            Scene scene = new Scene(content);
            Stage newWindow = new Stage();
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.setTitle("Enter key");
            newWindow.setScene(scene);
            newWindow.setResizable(false);
            newWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

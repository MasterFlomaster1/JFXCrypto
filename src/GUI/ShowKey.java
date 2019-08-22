package GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

class ShowKey {

    void showKey() {
        try {
            Stage showKey = new Stage();
            Parent content = FXMLLoader.load(getClass().getResource("ShowKey.fxml"));
            Scene scene = new Scene(content);
            content.setId("showKey");
            showKey.setTitle("key");
            showKey.getIcons().add(new Image("/SJC1.png"));
            showKey.setResizable(false);
            showKey.setScene(scene);
            showKey.initModality(Modality.APPLICATION_MODAL);
            showKey.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

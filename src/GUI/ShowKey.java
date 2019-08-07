package GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

class ShowKey {

    void showKey() {
        try {
            Stage newWindow = new Stage();
            Parent content = FXMLLoader.load(getClass().getResource("ShowKey.fxml"));
            Scene scene = new Scene(content);
            newWindow.setTitle("key");
            newWindow.setResizable(false);
            newWindow.setScene(scene);
            newWindow.initModality(Modality.APPLICATION_MODAL);
            newWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

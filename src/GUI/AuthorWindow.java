package GUI;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

class AuthorWindow {

    static void showAuthorWindow() {
        Label secondLabel = new Label();
        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);
        Stage newWindow = MainGUI.primaryStage;
        Scene secondScene = new Scene(secondaryLayout, 600, 400);
        newWindow.setScene(secondScene);
        newWindow.show();
    }

}

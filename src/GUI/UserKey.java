package GUI;

import Cipher.CurrentCipher;
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
        text.setText(CurrentCipher.getCurrentCipherName()+" key");
        secondaryLayout.getChildren().add(text);
        secondaryLayout.getChildren().add(secondLabel);

        TextField textField = new TextField();
        textField.setOnAction(event -> {
            String userKey = textField.getText();
            CurrentCipher.setKey(userKey);
            newWindow.close();
        });
        secondaryLayout.getChildren().add(textField);
        newWindow.setScene(secondScene);
        newWindow.show();

    }

}

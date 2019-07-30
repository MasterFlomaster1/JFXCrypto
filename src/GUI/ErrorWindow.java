package GUI;

import javafx.scene.control.Alert;

public class ErrorWindow {


    public static void showError(String error, String solution) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(error);
        alert.setContentText(solution);
        alert.showAndWait();
    }

    public static void showError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(error);
        alert.showAndWait();
    }

}

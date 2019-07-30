package GUI;

import javafx.scene.control.Alert;

public class AlertWindows {

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

    public static void showWarning(String warn) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(warn);
        alert.showAndWait();
    }

}

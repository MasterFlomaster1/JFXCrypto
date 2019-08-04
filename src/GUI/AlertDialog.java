package GUI;

import javafx.scene.control.Alert;

public class AlertDialog {

    public static void showError(String error, String solution) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(error);
        alert.setContentText(solution);
        alert.showAndWait();
    }

    public static void showError(String error) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(error);
        alert.showAndWait();
    }

    public static void showWarning(String warn) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(warn);
        alert.showAndWait();
    }

    public static void showInfo(String info) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(info);
        alert.showAndWait();
    }

}

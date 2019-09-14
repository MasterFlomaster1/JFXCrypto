package GUI.Main;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Add icons
 */
public class AlertDialog {

    public static void showError(String error, String details) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(error);
        alert.setContentText(details);
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

    public static void showInfo(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(info);
        alert.showAndWait();
    }

    public static boolean showConfirmDialog(String warn) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning");
        alert.setHeaderText(warn);
        Optional<ButtonType> option = alert.showAndWait();
        return option.isPresent() && option.get() == ButtonType.OK;
    }

}

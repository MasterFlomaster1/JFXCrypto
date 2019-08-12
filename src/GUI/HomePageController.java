package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomePageController {

    @FXML
    private Button menu;

    public void textEncryptionPageAction() {
        GUI.updatePageContent(Pages.TEXT_ENCRYPTION_PAGE.getParent());
    }

    public void fileEncryptionPageAction() {
        GUI.updatePageContent(Pages.FILE_ENCRYPTION_PAGE.getParent());
    }

    public void menuButtonAction() {
        GUI.menuTranslation.setRate(1);
        GUI.menuTranslation.play();
    }

}

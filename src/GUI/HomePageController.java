package GUI;

import javafx.fxml.FXML;

import java.awt.*;

public class HomePageController {

    @FXML
    public Button menuButton;

    public void textEncryptionPageAction() {
        GUI.updatePageContent(Pages.TEXT_ENCRYPTION_PAGE.getParent());
    }

    public void fileEncryptionPageAction() {
        GUI.updatePageContent(Pages.FILE_ENCRYPTION_PAGE.getParent());
    }

    public void initialize() {

    }

    public void menuButtonAction() {
        GUI.menuTranslation.setRate(1);
        GUI.menuTranslation.play();
    }

}

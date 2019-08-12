package GUI;

import javafx.fxml.FXML;

public class SideMenuController {

    public void homeButtonAction() {
        GUI.updatePageContent(Pages.HOME_PAGE.getParent());
        GUI.hideMenu();
    }

    public void textEncryptionButtonAction() {
        GUI.updatePageContent(Pages.TEXT_ENCRYPTION_PAGE.getParent());
        GUI.hideMenu();
    }

    public void fileEncryptionButtonAction() {
        GUI.updatePageContent(Pages.FILE_ENCRYPTION_PAGE.getParent());
        GUI.hideMenu();
    }

    public void fileHashButtonAction() {

    }

    public void aboutButtonAction() {
        GUI.updatePageContent(Pages.ABOUT_PAGE.getParent());
        GUI.hideMenu();
    }

}

package GUI.Controllers;

import GUI.Main.GUI;
import GUI.Main.Pages;

public class SideMenuController {

    public void homeButtonAction() {
        GUI.updatePageContent(Pages.HOME_PAGE.getParent());
        GUI.hideMenu();
    }

    public void hashSumCheckerButtonAction() {
        GUI.updatePageContent(Pages.HASH_SUM_CHECKER_PAGE.getParent());
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
        GUI.updatePageContent(Pages.FILE_HASH_PAGE.getParent());
        GUI.hideMenu();
    }

    public void textHashButtonAction() {
        GUI.updatePageContent(Pages.TEXT_HASH_PAGE.getParent());
        GUI.hideMenu();
    }

    public void aboutButtonAction() {
        GUI.updatePageContent(Pages.ABOUT_PAGE.getParent());
        GUI.hideMenu();
    }

}

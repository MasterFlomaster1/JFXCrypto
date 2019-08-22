package GUI;

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
        GUI.updatePageContent(Pages.FILE_HASH_PAGE.getParent());
        GUI.hideMenu();
    }

    public void aboutButtonAction() {
        GUI.updatePageContent(Pages.ABOUT_PAGE.getParent());
        GUI.hideMenu();
    }

}

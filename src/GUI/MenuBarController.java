package GUI;

import Cipher.CurrentCipher;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

public class MenuBarController {

    @FXML
    private MenuBar menuBar;

    @FXML
    private RadioMenuItem AES128_radio;

    @FXML
    private RadioMenuItem AES256_radio;

    @FXML
    private RadioMenuItem SimpleCipher_radio;

    @FXML
    private RadioMenuItem MD5_radio;

    public void initialize() {
        radioMenuInit();
    }

    public void genKeyAction() {
        GenerateKey.generateKey();
    }

    public void showKeyAction() {
        ShowKey showKey = new ShowKey();
        showKey.showKey();
    }

    public void setKeyAction() {
        UserKey.getKeyFromUser();
    }

    public void homePageAction() {
        GUI.updatePageContent(Pages.HOME_PAGE.getParent());
    }

    public void textEncryptionPageAction() {
        GUI.updatePageContent(Pages.TEXT_ENCRYPTION_PAGE.getParent());
    }

    public void fileEncryptionPageAction() {
        GUI.updatePageContent(Pages.FILE_ENCRYPTION_PAGE.getParent());
    }

    public void aboutAction() {
        GUI.updatePageContent(Pages.ABOUT_PAGE.getParent());
    }

    public void settingsAction() {
        GUI.updatePageContent(Pages.SETTINGS_PAGE.getParent());
    }

    public void AES128_radioAction() {
        AES128_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.AES128);
    }

    public void AES256_radioAction() {
        AES256_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.AES256);
    }

    public void SimpleCipher_radioAction() {
        SimpleCipher_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.SimpleCipher);
    }

    public void MD5_radioAction() {
        MD5_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.MD5);
    }

    private void radioMenuInit() {
        ToggleGroup group = new ToggleGroup();
        AES128_radio.setToggleGroup(group);
        AES256_radio.setToggleGroup(group);
        SimpleCipher_radio.setToggleGroup(group);
        MD5_radio.setToggleGroup(group);
        AES256_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.AES256);
    }


}

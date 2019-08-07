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
    private RadioMenuItem DES_radio;

    @FXML
    private RadioMenuItem MD2_radio;

    @FXML
    private RadioMenuItem MD5_radio;

    @FXML
    private RadioMenuItem SHA512_radio;

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

    public void DES_radioAction() {
        DES_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.DES);
    }

    public void MD2_radioAction() {
        MD2_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.MD2);
    }

    public void MD5_radioAction() {
        MD5_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.MD5);
    }

    public void SHA512_radioAction() {
        SHA512_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.SHA512);
    }

    private void radioMenuInit() {
        ToggleGroup group = new ToggleGroup();
        AES128_radio.setToggleGroup(group);
        AES256_radio.setToggleGroup(group);
        SimpleCipher_radio.setToggleGroup(group);
        DES_radio.setToggleGroup(group);
        MD2_radio.setToggleGroup(group);
        MD5_radio.setToggleGroup(group);
        SHA512_radio.setToggleGroup(group);
        AES256_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.AES256);
    }


}

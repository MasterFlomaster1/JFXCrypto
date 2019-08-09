package GUI;

import Cipher.CurrentCipher;
import Hash.CurrentHash;
import javafx.fxml.FXML;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

public class MenuBarController {

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
    private RadioMenuItem SHA1_radio;

    @FXML
    private RadioMenuItem SHA224_radio;

    @FXML
    private RadioMenuItem SHA256_radio;

    @FXML
    private RadioMenuItem SHA384_radio;

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

    public void DES_radioAction() {
        DES_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.DES);
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

    public void MD2_radioAction() {
        MD2_radio.setSelected(true);
        CurrentHash.setCurrentHash(CurrentHash.MD2);
    }

    public void MD5_radioAction() {
        MD5_radio.setSelected(true);
        CurrentHash.setCurrentHash(CurrentHash.MD5);
    }

    public void SHA1_radioAction() {
        SHA1_radio.setSelected(true);
        CurrentHash.setCurrentHash(CurrentHash.SHA1);
    }

    public void SHA224_radioAction() {
        SHA224_radio.setSelected(true);
        CurrentHash.setCurrentHash(CurrentHash.SHA224);
    }

    public void SHA256_radioAction() {
        SHA256_radio.setSelected(true);
        CurrentHash.setCurrentHash(CurrentHash.SHA256);
    }

    public void SHA384_radioAction() {
        SHA384_radio.setSelected(true);
        CurrentHash.setCurrentHash(CurrentHash.SHA384);
    }

    public void SHA512_radioAction() {
        MD2_radio.setSelected(true);
        CurrentHash.setCurrentHash(CurrentHash.SHA512);
    }

    private void radioMenuInit() {
        ToggleGroup ciphers = new ToggleGroup();
        AES128_radio.setToggleGroup(ciphers);
        AES256_radio.setToggleGroup(ciphers);
        SimpleCipher_radio.setToggleGroup(ciphers);
        DES_radio.setToggleGroup(ciphers);
        AES256_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.AES256);
        ToggleGroup hash = new ToggleGroup();
        MD2_radio.setToggleGroup(hash);
        MD5_radio.setToggleGroup(hash);
        SHA1_radio.setToggleGroup(hash);
        SHA224_radio.setToggleGroup(hash);
        SHA256_radio.setToggleGroup(hash);
        SHA384_radio.setToggleGroup(hash);
        SHA512_radio.setToggleGroup(hash);
        MD5_radio.setSelected(true);
        CurrentHash.setCurrentHash(CurrentHash.MD5);
    }


}

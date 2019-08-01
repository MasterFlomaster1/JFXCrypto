package GUI;

import Cipher.CurrentCipher;
import javafx.fxml.FXML;
import javafx.scene.control.RadioMenuItem;

public class FileEncryptionPageController implements Switchable {

    @FXML
    private RadioMenuItem AES_radio;

    @FXML
    private RadioMenuItem AES256_radio;

    @FXML
    private RadioMenuItem SimpleCipher_radio;

    @FXML
    public void homePageAction() {
        pageSwitcher.setPage(Pages.HOME_PAGE.getName());
    }

    @FXML
    public void textEncryptionPageAction() {
        pageSwitcher.setPage(Pages.TEXT_ENCRYPTION_PAGE.getName());
    }

    @FXML
    public void fileEncryptionPageAction() { }

    @FXML
    public void genKeyAction() {
        GenerateKey.generateKey();
    }

    @FXML
    public void showKeyAction() {
        ShowKey.showKey();
    }

    @FXML
    public void setKeyAction() {
        UserKey.getKeyFromUser();
    }

    @FXML
    public void aboutAction() {
        pageSwitcher.setPage(Pages.ABOUT_PAGE.getName());
    }

    @FXML
    public void AES_radioAction() {
        AES_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.AES);
    }

    @FXML
    public void AES256_radioAction() {
        AES256_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.AES256);
    }

    @FXML
    public void SimpleCipher_radioAction() {
        SimpleCipher_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.SimpleCipher);
    }

    private PageSwitcher pageSwitcher;

    @Override
    public void setParentPage(PageSwitcher page) {
        pageSwitcher = page;
    }
}

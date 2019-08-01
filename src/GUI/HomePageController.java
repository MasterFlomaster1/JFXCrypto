package GUI;

import Cipher.Aes256;
import Cipher.CurrentCipher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

public class HomePageController implements Switchable {

    @FXML
    private MenuItem homePage;

    @FXML
    private MenuItem textEncryptionPage;

    @FXML
    private MenuItem fileEncryptionPage;

    @FXML
    private MenuItem generateKey;

    @FXML
    private MenuItem showKey;

    @FXML
    private MenuItem setKey;

    @FXML
    private RadioMenuItem AES_radio;

    @FXML
    private RadioMenuItem AES256_radio;

    @FXML
    private RadioMenuItem SimpleCipher_radio;

    @FXML
    private Button buttonTextEncryption;

    @FXML
    private Button buttonFileEncryption;

    @FXML
    private MenuItem about;

    private Aes256 aes256;

    public void initialize() {
        aes256 = new Aes256();
        radioMenuInit();
    }

    @FXML
    public void homePageAction() { }

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

    @FXML
    public void textEncryptionAction() {
        pageSwitcher.setPage(Pages.TEXT_ENCRYPTION_PAGE.getName());
    }

    @FXML
    public void fileEncryptionAction() {
        pageSwitcher.setPage(Pages.FILE_ENCRYPTION_PAGE.getName());
    }

    private void radioMenuInit() {
        ToggleGroup group = new ToggleGroup();
        AES_radio.setToggleGroup(group);
        AES256_radio.setToggleGroup(group);
        SimpleCipher_radio.setToggleGroup(group);
        AES256_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.AES256);
    }

    private PageSwitcher pageSwitcher;

    @Override
    public void setParentPage(PageSwitcher page) {
        pageSwitcher = page;
    }
}

package GUI;

import Cipher.CurrentCipher;
import javafx.fxml.FXML;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutPageController implements Switchable {

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
    public void fileEncryptionPageAction() {
        pageSwitcher.setPage(Pages.FILE_ENCRYPTION_PAGE.getName());
    }

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
    public void aboutAction() { }

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
    public void repoLinkAction() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/MasterFlomaster1/SimpleJavaCrypter"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            AlertDialog.showError("Error");
        }
    }

    @FXML
    public void profileLinkAction() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/MasterFlomaster1"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            AlertDialog.showError("Error");
        }
    }

    private PageSwitcher pageSwitcher;

    @Override
    public void setParentPage(PageSwitcher page) {
        pageSwitcher = page;
    }

    private void radioMenuInit() {
        ToggleGroup group = new ToggleGroup();
        AES_radio.setToggleGroup(group);
        AES256_radio.setToggleGroup(group);
        SimpleCipher_radio.setToggleGroup(group);
    }

}

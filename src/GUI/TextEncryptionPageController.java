package GUI;

import Cipher.CurrentCipher;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class TextEncryptionPageController extends BaseController implements Switchable {
    @FXML
    private TextArea inputText;

    @FXML
    private TextArea outputText;

    @Override
    public void homePageAction() {
        pageSwitcher.setPage(Pages.HOME_PAGE.getName());
    }

    @Override
    public void textEncryptionPageAction() {

    }

    @Override
    public void fileEncryptionPageAction() {
        pageSwitcher.setPage(Pages.FILE_ENCRYPTION_PAGE.getName());
    }

    @Override
    public void aboutAction() {
        pageSwitcher.setPage(Pages.ABOUT_PAGE.getName());
    }

    @Override
    public void settingsAction() {
        pageSwitcher.setPage(Pages.SETTINGS_PAGE.getName());
    }

    public void encrypt() {
        outputText.setText(CurrentCipher.encrypt(inputText.getText()));
    }

    public void decrypt() {
        outputText.setText(CurrentCipher.decrypt(inputText.getText()));
    }

    private PageSwitcher pageSwitcher;

    @Override
    public void setParentPage(PageSwitcher page) {
        pageSwitcher = page;
        inputText.setPromptText("Input text");
        inputText.setFocusTraversable(false);
        outputText.setPromptText("Output text");
    }


}

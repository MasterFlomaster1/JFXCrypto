package GUI;

import Cipher.CurrentCipher;
import javafx.fxml.FXML;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

public abstract class BaseController {

    @FXML
    private RadioMenuItem AES128_radio;

    @FXML
    private RadioMenuItem AES256_radio;

    @FXML
    private RadioMenuItem SimpleCipher_radio;

    public void initialize() {
        radioMenuInit();
    }

    public void genKeyAction() {
        GenerateKey.generateKey();
    }

    public void showKeyAction() {
        ShowKey.showKey();
    }

    public void setKeyAction() {
        UserKey.getKeyFromUser();
    }

    public abstract void homePageAction();

    public abstract void textEncryptionPageAction();

    public abstract void fileEncryptionPageAction();

    public abstract void aboutAction();

    public abstract void settingsAction();

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

    private void radioMenuInit() {
        ToggleGroup group = new ToggleGroup();
        AES128_radio.setToggleGroup(group);
        AES256_radio.setToggleGroup(group);
        SimpleCipher_radio.setToggleGroup(group);
        AES256_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.AES256);
    }

    void updateRadioMenuValue() {
        System.out.println("updated: " + CurrentCipher.getCurrentCipher());
        switch (CurrentCipher.getCurrentCipher()) {
            case CurrentCipher.AES128:
                AES128_radio.setSelected(true);
                break;
            case CurrentCipher.AES256:
                AES256_radio.setSelected(true);
                break;
            case CurrentCipher.SimpleCipher:
                SimpleCipher_radio.setSelected(true);
                break;
        }
    }

}

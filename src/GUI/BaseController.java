package GUI;

import Cipher.CurrentCipher;
import javafx.fxml.FXML;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

public abstract class BaseController {

    boolean updated = false;

    @FXML
    private RadioMenuItem AES_radio;

    @FXML
    private RadioMenuItem AES256_radio;

    @FXML
    private RadioMenuItem SimpleCipher_radio;

    public void initialize() {
        radioMenuInit();
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
    public abstract void homePageAction();

    @FXML
    public abstract void textEncryptionPageAction();

    @FXML
    public abstract void fileEncryptionPageAction();

    @FXML
    public abstract void aboutAction();

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

    private void radioMenuInit() {
        ToggleGroup group = new ToggleGroup();
        AES_radio.setToggleGroup(group);
        AES256_radio.setToggleGroup(group);
        SimpleCipher_radio.setToggleGroup(group);
        AES256_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.AES256);
    }

    void updateRadioMenuValue() {
        System.out.println("updated: " + CurrentCipher.getCurrentCipher());
        switch (CurrentCipher.getCurrentCipher()) {
            case CurrentCipher.AES:
                AES_radio.setSelected(true);
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

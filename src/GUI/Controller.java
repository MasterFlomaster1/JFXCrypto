package GUI;

import Cipher.Aes256;
import Cipher.CurrentCipher;
import Cipher.SimpleCipher;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Controller {

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
        AuthorWindow.showAuthorWindow();
    }

    @FXML
    public void AES_radioAction() {
        AES_radio.setSelected(true);
    }

    @FXML
    public void AES256_radioAction() {
        AES256_radio.setSelected(true);
    }

    @FXML
    public void SimpleCipher_radioAction() {
        SimpleCipher_radio.setSelected(true);
    }

    @FXML
    public void setButtonTextEncryptionAction() {
        System.out.println("Action perform");
    }

    @FXML
    public void setButtonFileEncryptionAction() {
        System.out.println("file encryption option");
    }

    private void radioMenuInit() {
        ToggleGroup group = new ToggleGroup();
        AES_radio.setToggleGroup(group);
        AES256_radio.setToggleGroup(group);
        SimpleCipher_radio.setToggleGroup(group);
        AES256_radio.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.AES256);
    }

    private void func() {
        try {
            generateKey.setGraphic(new ImageView(new Image(getClass().getResource("/resources/update.png").toString())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label textHere = new Label("Enter text here:");
        textHere.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        grid.add(textHere, 0, 0, 5, 1);

        TextField inputTextField = new TextField();
        grid.add(inputTextField, 0, 1, 10, 1);

        Button encrypt = new Button("Encrypt");
        HBox encBtn = new HBox(10);
        encBtn.getChildren().add(encrypt);
        grid.add(encBtn, 0, 2);

        Button decrypt = new Button("Decrypt");
        HBox decBtn = new HBox(10);
        decBtn.getChildren().add(decrypt);
        grid.add(decBtn, 8, 2);

        Label pw = new Label("Result:");
        pw.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        grid.add(pw, 0, 3);

        TextField outputTextField = new TextField();
        grid.add(outputTextField, 0, 4, 10, 1);

        encrypt.setOnAction(e -> {
            String text = inputTextField.getText();
            outputTextField.setText(aes256.encryptString(text));
        });
        decrypt.setOnAction(e -> {
            String text = inputTextField.getText();
            outputTextField.setText(aes256.decryptString(text));
        });

    }
}

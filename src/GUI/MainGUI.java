package GUI;

import Cipher.Aes256;
import Cipher.CurrentCipher;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SimpleJavaCrypter");

        Aes256 aes256 = new Aes256();

        BorderPane root = new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");

        MenuItem generateKey = new MenuItem("Generate key");
        generateKey.setOnAction(e -> GenerateKey.generateKey());
        try {
            generateKey.setGraphic(new ImageView(new Image(getClass().getResource("/resources/update.png").toString())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        file.getItems().add(generateKey);

        MenuItem showKey = new MenuItem("Show key");
        showKey.setOnAction(e -> ShowKey.showKey());
        file.getItems().add(showKey);
        CurrentCipher.setCurrentCipher(CurrentCipher.AES256);

        MenuItem enterKey = new MenuItem("Set key");
        enterKey.setOnAction(event -> UserKey.getKeyFromUser());
        file.getItems().add(enterKey);

        Menu ciphersMenu = new Menu();
        RadioMenuItem AES_cipher = new RadioMenuItem("AES");
        RadioMenuItem AES256_cipher = new RadioMenuItem("AES-256");
        RadioMenuItem SimpleCipher_cipher = new RadioMenuItem("SimpleCipher");
        AES256_cipher.setSelected(true);
        CurrentCipher.setCurrentCipher(CurrentCipher.AES256);
        AES_cipher.setOnAction(e -> {
            if (CurrentCipher.getCurrentCipher()!=CurrentCipher.AES) {
                CurrentCipher.setCurrentCipher(CurrentCipher.AES);
                AES256_cipher.setSelected(false);
                SimpleCipher_cipher.setSelected(false);
            } else {
                AES_cipher.setSelected(true);
            }
        });
        AES256_cipher.setOnAction(e -> {
            if (CurrentCipher.getCurrentCipher()!=CurrentCipher.AES256) {
                CurrentCipher.setCurrentCipher(CurrentCipher.AES256);
                AES_cipher.setSelected(false);
                SimpleCipher_cipher.setSelected(false);
            } else {
                AES256_cipher.setSelected(true);
            }
        });
        SimpleCipher_cipher.setOnAction(e -> {
            if (CurrentCipher.getCurrentCipher()!=CurrentCipher.SimpleCipher) {
                CurrentCipher.setCurrentCipher(CurrentCipher.SimpleCipher);
                AES_cipher.setSelected(false);
                AES256_cipher.setSelected(false);
            } else {
                SimpleCipher_cipher.setSelected(true);
            }
        });
        ciphersMenu.getItems().add(AES_cipher);
        ciphersMenu.getItems().add(AES256_cipher);
        ciphersMenu.getItems().add(SimpleCipher_cipher);

        menuBar.getMenus().add(file);
        menuBar.getMenus().add(ciphersMenu);
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        root.setTop(menuBar);
        root.setCenter(grid);

        Scene scene = new Scene(root, 300, 230);
        primaryStage.setScene(scene);

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


        primaryStage.show();
    }

}

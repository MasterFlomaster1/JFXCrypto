package GUI;

import Cipher.Aes256;
import Cipher.SimpleCipher;
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

public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SimpleJavaCrypter");

        Aes256 aes256 = new Aes256();

        BorderPane root = new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");

        MenuItem update_cipher = new MenuItem("Update cipher");
        update_cipher.setOnAction(e -> SimpleCipher.updateCombination());
        try {
            update_cipher.setGraphic(new ImageView(new Image(getClass().getResource("/resources/update.png").toString())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        file.getItems().add(update_cipher);

        Menu ciphers = new Menu("Ciphers");
        RadioMenuItem AES256 = new RadioMenuItem("Aes-256");
        AES256.setSelected(true);
        ciphers.getItems().add(AES256);
        file.getItems().add(ciphers);

        menuBar.getMenus().add(file);
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
            outputTextField.setText(new String(aes256.encrypt(text.getBytes())));
        });
        decrypt.setOnAction(e -> {
            String text = inputTextField.getText();
            outputTextField.setText(new String(aes256.decrypt(text.getBytes())));
        });


        primaryStage.show();
    }

}

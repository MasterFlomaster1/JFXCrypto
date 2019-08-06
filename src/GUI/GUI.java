package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends Application {

    private static BorderPane borderPane = new BorderPane();

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("SimpleJavaCrypter");
            initializePages();
            Parent menuBar = FXMLLoader.load(getClass().getResource("MenuBar.fxml"));
            Scene scene = new Scene(new VBox(), 600, 400);
            borderPane.setTop(menuBar);
            borderPane.setCenter(Pages.HOME_PAGE.getParent());
            scene.setRoot(borderPane);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializePages() {
        try {
            Pages.ABOUT_PAGE.setParent(FXMLLoader.load(getClass().getResource("AboutPage.fxml")));
            Pages.FILE_ENCRYPTION_PAGE.setParent(FXMLLoader.load(getClass().getResource("FileEncryptionPage.fxml")));
            Pages.HOME_PAGE.setParent(FXMLLoader.load(getClass().getResource("HomePage.fxml")));
            Pages.SETTINGS_PAGE.setParent(FXMLLoader.load(getClass().getResource("SettingsPage.fxml")));
            Pages.TEXT_ENCRYPTION_PAGE.setParent(FXMLLoader.load(getClass().getResource("TextEncryptionPage.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void updatePageContent(Node nextNode) {
        borderPane.setCenter(nextNode);
    }

    public static void main(String[] args) {
        launch(args);
    }

}

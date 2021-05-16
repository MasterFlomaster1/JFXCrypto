package GUI.Main;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class GUI extends Application {

    private static BorderPane borderPane = new BorderPane();
    private static Pane pane;
    private static VBox menu;
    private static TranslateTransition menuTranslation;

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("SimpleJavaCrypter");
            primaryStage.getIcons().add(new Image("/SJC1.png"));
            initializePages();

            Scene scene = new Scene(new Pane(), 590, 390);
            Parent menuBar = FXMLLoader.load(getClass().getResource("/GUI/fxml/MenuBar.fxml"));

            borderPane.setTop(menuBar);
            pane = new Pane(Pages.HOME_PAGE.getParent());
            pane.getStylesheets().add(getClass().getResource("/GUI/css/General.css").toExternalForm());
            borderPane.setCenter(pane);

            menu = FXMLLoader.load(getClass().getResource("/GUI/fxml/SideMenu.fxml"));

            menu.setTranslateX(-200);
            menuTranslation = new TranslateTransition(Duration.millis(250), menu);
            menuTranslation.setFromX(-400);
            menuTranslation.setToX(0);

            menu.setOnMouseExited(evt -> {
                //bug here
                menuTranslation.setRate(-1);
                menuTranslation.play();
            });

            pane.getChildren().add(menu);
            scene.setRoot(borderPane);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializePages() {
        try {
            Pages.ABOUT_PAGE.setParent(FXMLLoader.load(getClass().getResource("/GUI/fxml/AboutPage.fxml")));
            Pages.FILE_ENCRYPTION_PAGE.setParent(FXMLLoader.load(getClass().getResource("/GUI/fxml/FileEncryptionPage.fxml")));
            Pages.FILE_HASH_PAGE.setParent(FXMLLoader.load(getClass().getResource("/GUI/fxml/FileHashPage.fxml")));
            Pages.HASH_SUM_CHECKER_PAGE.setParent(FXMLLoader.load(getClass().getResource("/GUI/fxml/HashSumCheckerPage.fxml")));
            Pages.HOME_PAGE.setParent(FXMLLoader.load(getClass().getResource("/GUI/fxml/HomePage.fxml")));
            Pages.SETTINGS_PAGE.setParent(FXMLLoader.load(getClass().getResource("/GUI/fxml/SettingsPage.fxml")));
            Pages.TEXT_ENCRYPTION_PAGE.setParent(FXMLLoader.load(getClass().getResource("/GUI/fxml/TextEncryptionPage.fxml")));
            Pages.TEXT_HASH_PAGE.setParent(FXMLLoader.load(getClass().getResource("/GUI/fxml/TextHashPage.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updatePageContent(Node nextNode) {
        pane.getChildren().set(0, nextNode);
    }

    public static void hideMenu() {
        menuTranslation.setRate(-1);
        menuTranslation.play();
    }

    public static void menuButtonPressed() {
        menuTranslation.setRate(1);
        menuTranslation.play();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

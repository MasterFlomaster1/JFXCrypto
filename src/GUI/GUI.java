package GUI;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    static TranslateTransition menuTranslation;

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("SimpleJavaCrypter");
            initializePages();
            Parent menuBar = FXMLLoader.load(getClass().getResource("MenuBar.fxml"));
            Scene scene = new Scene(new Pane(), 600, 400);
            borderPane.setTop(menuBar);
            pane = new Pane(Pages.HOME_PAGE.getParent());
            borderPane.setCenter(pane);

            menu = FXMLLoader.load(getClass().getResource("SideMenu.fxml"));
            menu.setId("menu");
            menu.prefHeightProperty().bind(scene.heightProperty());
            menu.setPrefWidth(200);
            menu.getStylesheets().add(getClass().getResource("SideMenu.css").toExternalForm());
            menu.setTranslateX(-200);
            menuTranslation = new TranslateTransition(Duration.millis(250), menu);
            menuTranslation.setFromX(-200);
            menuTranslation.setToX(0);

            menu.setOnMouseExited(evt -> {
                menuTranslation.setRate(-1);
                menuTranslation.play();
            });

            pane.getChildren().add(menu);
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
        pane.getChildren().set(0, nextNode);
    }

    static void hideMenu() {
        menuTranslation.setRate(-1);
        menuTranslation.play();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

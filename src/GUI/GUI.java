package GUI;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        PageSwitcher pageSwitcher = new PageSwitcher();
        pageSwitcher.loadPage(Pages.ABOUT_PAGE.getName(), Pages.ABOUT_PAGE.getPath());
        pageSwitcher.loadPage(Pages.FILE_ENCRYPTION_PAGE.getName(), Pages.FILE_ENCRYPTION_PAGE.getPath());
        pageSwitcher.loadPage(Pages.HOME_PAGE.getName(), Pages.HOME_PAGE.getPath());
        pageSwitcher.loadPage(Pages.TEXT_ENCRYPTION_PAGE.getName(), Pages.TEXT_ENCRYPTION_PAGE.getPath());
        pageSwitcher.loadPage(Pages.SETTINGS_PAGE.getName(), Pages.SETTINGS_PAGE.getPath());
        pageSwitcher.setPage(Pages.HOME_PAGE.getName());

        Group root = new Group();
        root.getChildren().addAll(pageSwitcher);
        primaryStage.setTitle("SimpleJavaCrypter");
//        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

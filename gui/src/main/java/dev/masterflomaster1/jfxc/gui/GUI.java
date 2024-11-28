package dev.masterflomaster1.jfxc.gui;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        primaryStage.setTitle("SJC");
        primaryStage.show();
    }

}

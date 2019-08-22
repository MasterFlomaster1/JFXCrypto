package GUI;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutPageController {

    public ImageView menuImage;

    public void repoLinkAction() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/MasterFlomaster1/SimpleJavaCrypter"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            AlertDialog.showError("Error");
        }
    }

    public void profileLinkAction() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/MasterFlomaster1"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            AlertDialog.showError("Error");
        }
    }

    public void menuButtonPressed() {
        menuImage.setImage(new javafx.scene.image.Image("/menu2.png"));
        GUI.menuTranslation.setRate(1);
        GUI.menuTranslation.play();
    }

    public void menuButtonRelease() {
        menuImage.setImage(new javafx.scene.image.Image("/menu1.png"));
    }

    public void mouseEntered() {
        menuImage.setImage(new Image("/menu2.png"));
    }

}

package GUI.Controllers;

import GUI.Main.GUI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SettingsPageController {

    public ImageView menuImage;

    public void menuButtonPressed() {
        menuImage.setImage(new javafx.scene.image.Image("/menu2.png"));

    }

    public void menuButtonRelease() {
        menuImage.setImage(new javafx.scene.image.Image("/menu1.png"));
    }

    public void mouseEntered() {
        menuImage.setImage(new Image("/menu2.png"));
    }

}

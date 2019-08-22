package GUI;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HomePageController {

    public ImageView menuImage;

    public void textEncryptionPageAction() {
        GUI.updatePageContent(Pages.TEXT_ENCRYPTION_PAGE.getParent());
    }

    public void fileEncryptionPageAction() {
        GUI.updatePageContent(Pages.FILE_ENCRYPTION_PAGE.getParent());
    }

    public void menuButtonPressed() {
        menuImage.setImage(new Image("/menu2.png"));
        GUI.menuTranslation.setRate(1);
        GUI.menuTranslation.play();
    }

    public void menuButtonRelease() {
        menuImage.setImage(new Image("/menu1.png"));
    }

    public void mouseEntered() {
        menuImage.setImage(new Image("/menu2.png"));
    }

}

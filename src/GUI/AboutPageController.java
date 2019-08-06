package GUI;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutPageController {

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

}

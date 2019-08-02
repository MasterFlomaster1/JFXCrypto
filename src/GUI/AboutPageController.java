package GUI;

import javafx.fxml.FXML;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutPageController extends BaseController implements Switchable {

    @Override
    public void homePageAction() {
        pageSwitcher.setPage(Pages.HOME_PAGE.getName());
    }

    @Override
    public void textEncryptionPageAction() {
        pageSwitcher.setPage(Pages.TEXT_ENCRYPTION_PAGE.getName());
    }

    @Override
    public void fileEncryptionPageAction() {
        pageSwitcher.setPage(Pages.FILE_ENCRYPTION_PAGE.getName());
    }

    @Override
    public void aboutAction() {

    }

    @FXML
    public void repoLinkAction() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/MasterFlomaster1/SimpleJavaCrypter"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            AlertDialog.showError("Error");
        }
    }

    @FXML
    public void profileLinkAction() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/MasterFlomaster1"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            AlertDialog.showError("Error");
        }
    }

    private PageSwitcher pageSwitcher;

    @Override
    public void setParentPage(PageSwitcher page) {
        pageSwitcher = page;
    }

}

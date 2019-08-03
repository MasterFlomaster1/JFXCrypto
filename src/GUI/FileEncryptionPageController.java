package GUI;

import javafx.fxml.FXML;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;

import java.io.File;
import java.util.List;

public class FileEncryptionPageController extends BaseController implements Switchable {

    @FXML
    public Text fileName;

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

    }

    public void aboutAction() {
        pageSwitcher.setPage(Pages.ABOUT_PAGE.getName());
    }

    @Override
    public void settingsAction() {
        pageSwitcher.setPage(Pages.SETTINGS_PAGE.getName());
    }

    public void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    public void handleDragDropped(DragEvent event) {
        List<File> files = event.getDragboard().getFiles();
        File out = new File("path to file");
        File in = files.get(0);
    }

    private PageSwitcher pageSwitcher;

    @Override
    public void setParentPage(PageSwitcher page) {
        pageSwitcher = page;
    }
}

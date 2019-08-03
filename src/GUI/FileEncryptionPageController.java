package GUI;

import Cipher.CurrentCipher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class FileEncryptionPageController extends BaseController implements Switchable {

    @FXML
    public Text fileName;

    @FXML
    public TextField pathToFile;

    @FXML
    public Button browse;

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

    @Override
    public void aboutAction() {
        pageSwitcher.setPage(Pages.ABOUT_PAGE.getName());
    }

    @Override
    public void settingsAction() {
        pageSwitcher.setPage(Pages.SETTINGS_PAGE.getName());
    }

    private boolean filesReady = false;
    private File in;
    private File out;

    public void encryptButtonAction() {
        if (filesReady) {
            CurrentCipher.encryptFile(in, out);
        }
    }

    public void decryptButtonAction() {
        if (filesReady) {
            CurrentCipher.decryptFile(in, out);
        }
    }

    public void getFilePathFromTextField() {

    }

    public void browseButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file");
        File file = fileChooser.showOpenDialog(browse.getScene().getWindow());
        pathToFile.setText(file.getPath());
        fileChooser.setTitle("Select ");
        File fileRes = fileChooser.showOpenDialog(browse.getScene().getWindow());
//        handleFiles(file, fileRes);
    }

    public void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    public void handleDragDropped(DragEvent event) {
        List<File> files = event.getDragboard().getFiles();
        File in = files.get(0);
        File out = new File("path to file");
//        handleFiles(in, out);
    }

    private PageSwitcher pageSwitcher;

    @Override
    public void setParentPage(PageSwitcher page) {
        pageSwitcher = page;
        pathToFile.setPromptText("File path");
        pathToFile.setFocusTraversable(false);
    }
}

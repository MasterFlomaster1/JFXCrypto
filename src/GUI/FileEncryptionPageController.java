package GUI;

import Cipher.CurrentCipher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class FileEncryptionPageController extends BaseController implements Switchable {

    @FXML
    public Button browseInFile;

    @FXML
    public Button browseOutFile;

    @FXML
    public Text fileInPath;

    @FXML
    public Text fileOutPath;

    @FXML
    public Text dragNDropText;

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

    private boolean fileInReady = false;
    private boolean fileOutReady = false;
    private File in;
    private File out;

    public void encryptButtonAction() {
        if (fileInReady && fileOutReady) {
            CurrentCipher.encryptFile(in, out);
            fileInReady = false;
            fileOutReady = false;
        }
    }

    public void decryptButtonAction() {
        if (fileInReady && fileOutReady) {
            CurrentCipher.decryptFile(in, out);
            fileInReady = false;
            fileOutReady = false;
        }
    }

    public void browseInButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file");
        in = fileChooser.showOpenDialog(browseInFile.getScene().getWindow());
        hideDragDropText();
        fileInPath.setText(in.getPath());
        fileInReady = true;
    }

    public void browseOutButtonAction() {
        FileChooser save = new FileChooser();
        save.setTitle("Save file");
        out = save.showSaveDialog(browseOutFile.getScene().getWindow());
        fileOutPath.setText(out.getPath());
        fileOutReady = true;
    }

    public void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    public void handleDragDropped(DragEvent event) {
        List<File> files = event.getDragboard().getFiles();
        in = files.get(0);
        hideDragDropText();
        fileInPath.setText(in.getPath());
        fileInReady = true;
    }

    private void hideDragDropText() {
        dragNDropText.setVisible(false);
    }

    private void enableDragDropText() {
        dragNDropText.setDisable(true);
    }

    private PageSwitcher pageSwitcher;

    @Override
    public void setParentPage(PageSwitcher page) {
        pageSwitcher = page;
    }
}

package GUI.Controllers;

import GUI.Main.GUI;
import GUI.Main.AlertDialog;
import Utils.PathCutter;
import Cipher.CurrentCipher;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.List;

public class FileEncryptionPageController {

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

    private boolean fileInReady = false;
    private boolean fileOutReady = false;
    private File in;
    private File out;
    public ImageView menuImage;

    public void encryptButtonAction() {
        if ((fileInReady && fileOutReady) && in!=out) {
            CurrentCipher.encryptFile(in, out);
        } else if (fileInReady && out!=null) {
            if (AlertDialog.showConfirmDialog("Do you want to rewrite \"" + out.getName() + "\" ?")) {
                CurrentCipher.encryptFile(in, out);
                fileInReady = false;
                fileOutReady = false;
            }
        } else if (fileInReady) {
            AlertDialog.showWarning("Select result file path");
        } else if (fileOutReady) {
            AlertDialog.showWarning("Choose file you want to encrypt/decrypt");
        }
    }

    public void decryptButtonAction() {
        if ((fileInReady && fileOutReady) && in!=out) {
            CurrentCipher.decryptFile(in, out);
        } else if (fileInReady && out!=null) {
            if (AlertDialog.showConfirmDialog("Do you want to rewrite \"" + out.getName() + "\" ?")) {
                CurrentCipher.decryptFile(in, out);
                fileInReady = false;
                fileOutReady = false;
            } else if (fileInReady) {
                AlertDialog.showWarning("Select result file path");
            } else if (fileOutReady) {
                AlertDialog.showWarning("Choose file you want to encrypt/decrypt");
            }
        }
    }

    public void browseInButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file");
        in = fileChooser.showOpenDialog(browseInFile.getScene().getWindow());
        //Debug here
        if (in==null) {
            fileInReady = false;
            return;
        }
        hideDragDropText();
        fileInPath.setText(PathCutter.cutPath(in.getPath()));
        fileInReady = true;
    }

    public void browseOutButtonAction() {
        FileChooser save = new FileChooser();
        save.setTitle("Save file");
        out = save.showSaveDialog(browseOutFile.getScene().getWindow());
        //Debug here
        if (out==null) {
            fileOutReady = false;
            return;
        }
        fileOutPath.setText(PathCutter.cutPath(out.getPath()));
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
        fileInPath.setText(PathCutter.cutPath(in.getPath()));
        fileInReady = true;
    }

    private void hideDragDropText() {
        dragNDropText.setVisible(false);
    }

    private void enableDragDropText() {
        dragNDropText.setDisable(true);
    }

    public void menuButtonPressed() {
        menuImage.setImage(new Image("/menu2.png"));
        GUI.menuButtonPressed();
    }

    public void menuButtonRelease() {
        menuImage.setImage(new Image("/menu1.png"));
    }

    public void mouseEntered() {
        menuImage.setImage(new Image("/menu2.png"));
    }
}

package GUI;

import Hash.CurrentHash;
import Utils.PathCutter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class FileHashPageController {

    public ImageView menuImage;

    @FXML
    public Button browseInFile;

    @FXML
    public Text fileInPath;

    @FXML
    public Text dragNDropText;

    @FXML
    public TextArea hashSumText;

    private boolean fileInReady = false;
    private File in;

    public void browseInButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file");
        in = fileChooser.showOpenDialog(browseInFile.getScene().getWindow());
        //debug here
        if (in==null) {
            fileInReady = false;
            return;
        }
        hideDragDropText();
        fileInPath.setText(PathCutter.cutPath(in.getPath()));
        fileInReady = true;
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

    public void hashButtonAction() {
        if (fileInReady) {
            hashSumText.setText(CurrentHash.fileHashSum(in));
        }
    }

    private void hideDragDropText() {
        dragNDropText.setVisible(false);
    }

    private void enableDragDropText() {
        dragNDropText.setDisable(true);
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

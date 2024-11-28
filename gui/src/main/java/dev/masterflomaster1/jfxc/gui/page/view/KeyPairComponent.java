package dev.masterflomaster1.jfxc.gui.page.view;

import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.KeyPairViewModelComponent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import lombok.Getter;

@Getter
final class KeyPairComponent {

    private final TextArea pubKeyTextArea = new TextArea();
    private final TextArea prtKeyTextArea = new TextArea();
    private final Button pubKeyExportButton = new Button("Export");
    private final Button prvKeyExportButton = new Button("Export");
    private final ComboBox<String> pubKeyDisplayMode = new ComboBox<>();
    private final ComboBox<String> prvKeyDisplayMode = new ComboBox<>();

    private final KeyPairViewModelComponent viewModel = new KeyPairViewModelComponent();

    KeyPairComponent() {
        pubKeyTextArea.setWrapText(true);
        prtKeyTextArea.setWrapText(true);

        bindComponents();
    }

    GridPane createSection() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label label1 = new Label("Public Key");
        Label label2 = new Label("Private Key");
        label1.setAlignment(Pos.CENTER);
        label2.setAlignment(Pos.CENTER);

        Button pubKeyCopyButton = UIElementFactory.createCopyButton(pubKeyTextArea);
        Button prvKeyCopyButton = UIElementFactory.createCopyButton(prtKeyTextArea);
        Button pubKeyImportButton = new Button("Import");
        Button prvKeyImportButton = new Button("Import");

        pubKeyDisplayMode.getItems().addAll("PEM", "Hex", "Base64");
        prvKeyDisplayMode.getItems().addAll("PEM", "Hex", "Base64");
        pubKeyDisplayMode.getSelectionModel().selectFirst();
        prvKeyDisplayMode.getSelectionModel().selectFirst();
        pubKeyDisplayMode.setOnAction(viewModel::onPubKeyDisplayModeChanged);
        prvKeyDisplayMode.setOnAction(viewModel::onPrvKeyDisplayModeChanged);

        HBox pubKeyBox = new HBox(5, pubKeyCopyButton, pubKeyImportButton, pubKeyExportButton, pubKeyDisplayMode);
        HBox prvKeyBox = new HBox(5, prvKeyCopyButton, prvKeyImportButton, prvKeyExportButton, prvKeyDisplayMode);

        pubKeyBox.setAlignment(Pos.CENTER);
        prvKeyBox.setAlignment(Pos.CENTER);

        grid.add(label1, 0, 0);
        grid.add(label2, 1, 0);
        grid.add(pubKeyTextArea, 0, 1);
        grid.add(prtKeyTextArea, 1, 1);
        grid.add(pubKeyBox, 0, 2);
        grid.add(prvKeyBox, 1, 2);

        return grid;
    }

    private void bindComponents() {
        pubKeyTextArea.textProperty().bindBidirectional(viewModel.getPublicKeyProperty());
        prtKeyTextArea.textProperty().bindBidirectional(viewModel.getPrivateKeyProperty());

        pubKeyDisplayMode.valueProperty().bindBidirectional(viewModel.getPubKeyDisplayModeProperty());
        prvKeyDisplayMode.valueProperty().bindBidirectional(viewModel.getPrvKeyDisplayModeProperty());
    }

}

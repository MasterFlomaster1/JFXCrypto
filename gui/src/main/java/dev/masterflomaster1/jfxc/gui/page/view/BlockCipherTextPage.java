package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.controls.ModalPane;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.layout.ModalBox;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.BlockCipherTextViewModel;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

public final class BlockCipherTextPage extends SimplePage {

    public static final String NAME = "Block Cipher Text";

    private final TextField keyTextField = new TextField();
    private final TextField ivTextField = new TextField();
    private final ComboBox<String> blockCipherComboBox = new ComboBox<>();
    private final ComboBox<String> modesComboBox = new ComboBox<>();
    private final ComboBox<String> paddingsComboBox = new ComboBox<>();
    private final ComboBox<Integer> keyLengthComboBox = new ComboBox<>();

    private Timeline emptyIvAnimation;
    private Timeline emptyKeyAnimation;

    private InputGroup paddingGroup;
    private InputGroup ivGroup;
    ModalPane modalPane = new ModalPane();

    private final InputOutputAreaComponent ioAreaComponent = new InputOutputAreaComponent();

    private final BlockCipherTextViewModel viewModel = new BlockCipherTextViewModel();

    public BlockCipherTextPage() {
        super();

        addSection("Block Cipher Text Encryption", mainSection());
        viewModel.setIoComponentViewModel(ioAreaComponent.getViewModel());
        bindComponents();

        viewModel.onAlgorithmSelection(null);
        viewModel.onInit();
        viewModel.onAlgorithmSelection(null);

        onModeSelection(null);
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "Encrypt text using various block cipher algorithms with configurable key generation, " +
                        "encryption modes, padding, and IV settings."
        );

        var algoGroup = new InputGroup(new Label("Algorithm"), blockCipherComboBox);

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> viewModel.action(true));
        decryptButton.setOnAction(event -> viewModel.action(false));

        blockCipherComboBox.setOnAction(viewModel::onAlgorithmSelection);

        var keyLenLabel = new Label("Key Length");
        var keyLenGroup = new InputGroup(keyLenLabel, keyLengthComboBox);
        var keySettingsButton = new Button("", new FontIcon(BootstrapIcons.GEAR));

        var keyGroup = new InputGroup(keyLabel, keyTextField, keySettingsButton);

        getChildren().add(modalPane);

        var modal = UIElementFactory.createPasswordSettingsModal(keyLengthComboBox, keyTextField, modalPane);
        modal.setPadding(new Insets(20));

        var passwordSettingsModal = new ModalBox(modalPane);
        passwordSettingsModal.addContent(modal);
        passwordSettingsModal.setMaxSize(500, 250);

        keySettingsButton.setOnAction((e) -> modalPane.show(passwordSettingsModal));

        var paddingsLabel = new Label("Padding");
        paddingGroup = new InputGroup(paddingsLabel, paddingsComboBox);

        var modeLabel = new Label("Mode");
        var modeGroup = new InputGroup(modeLabel, modesComboBox);
        modesComboBox.setOnAction(this::onModeSelection);

        var ivLabel = new Label("IV");
        var ivShuffleButton = new Button("", new FontIcon(BootstrapIcons.SHUFFLE));
        ivGroup = new InputGroup(ivLabel, ivTextField, ivShuffleButton);
        Tooltip ivTooltip = new Tooltip("Initialization Vector (IV). Required for certain encryption modes" +
                " (e.g., CBC). Must match block size.");
        Tooltip.install(ivGroup, ivTooltip);

        ivShuffleButton.setOnAction(viewModel::onIvShuffleAction);

        var cipherSettingsContainer = new FlowPane(
                20, 20,
                algoGroup,
                keyLenGroup,
                modeGroup,
                paddingGroup,
                ivGroup
        );

        var keySettingsContainer = new FlowPane(
                20, 20,
                keyGroup
        );

        var controlsHBox2 = new HBox(
                20,
                encryptButton,
                decryptButton
        );

        var ioArea = ioAreaComponent.createSection();
        ioAreaComponent.getOutputArea().setEditable(false);

        emptyIvAnimation = Animations.wobble(ivGroup);
        emptyKeyAnimation = Animations.wobble(keyGroup);

        return new VBox(
                20,
                description,
                cipherSettingsContainer,
                keySettingsContainer,
                controlsHBox2,
                new Separator(Orientation.HORIZONTAL),
                ioArea
        );
    }

    private void bindComponents() {
        keyTextField.textProperty().bindBidirectional(viewModel.getKeyProperty());
        ivTextField.textProperty().bindBidirectional(viewModel.getIvProperty());
        counterLabel.textProperty().bind(viewModel.getCounterText());

        blockCipherComboBox.valueProperty().bindBidirectional(viewModel.getBlockCipherComboBoxProperty());
        Bindings.bindContent(blockCipherComboBox.getItems(), viewModel.getBlockCipherAlgorithmsList());

        modesComboBox.valueProperty().bindBidirectional(viewModel.getModesComboBoxProperty());
        Bindings.bindContent(modesComboBox.getItems(), viewModel.getModesList());

        paddingsComboBox.valueProperty().bindBidirectional(viewModel.getPaddingsComboBoxProperty());
        Bindings.bindContent(paddingsComboBox.getItems(), viewModel.getPaddingsList());

        keyLengthComboBox.valueProperty().bindBidirectional(viewModel.getKeyLengthComboBoxProperty());
        Bindings.bindContent(keyLengthComboBox.getItems(), viewModel.getKeyLengthList());

        viewModel.setEmptyIvAnimation(emptyIvAnimation);
        viewModel.setEmptyKeyAnimation(emptyKeyAnimation);

        blockCipherComboBox.getSelectionModel().selectFirst();
        modesComboBox.getSelectionModel().selectFirst();
        paddingsComboBox.getSelectionModel().selectFirst();
        keyLengthComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Disable IV input group if ECB cipher mode is selected. Also disable padding input if GCM is selected.
     */
    private void onModeSelection(ActionEvent e) {
        paddingGroup.setDisable(viewModel.isGcmModeSelected());
        ivGroup.setDisable(viewModel.isNonIvModeSelected());
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onReset() {
        viewModel.onReset();
    }
}

package dev.masterflomaster1.jfxc.gui.page.view;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.gui.page.SimplePage;
import dev.masterflomaster1.jfxc.gui.page.UIElementFactory;
import dev.masterflomaster1.jfxc.gui.page.viewmodel.AdfgvxViewModel;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public final class AdfgvxPage extends SimplePage {

    public static final String NAME = "ADFGVX Cipher";

    private final TextArea inputTextArea = UIElementFactory.createInputTextArea("Enter text to encrypt / decrypt");
    private final TextArea outputTextArea = UIElementFactory.createOuputTextArea("Result");
    private final TextField keyTextField = new TextField();

    private final ToggleButton unblockedModeToggleBtn = new ToggleButton("Unblocked");
    private final ToggleButton blocksOf2ModeToggleBtn = new ToggleButton("Blocks of 2");
    private final ToggleButton blocksOf5ModeToggleBtn = new ToggleButton("Blocks of 5");

    private ToggleGroup toggleGroup;
    private Timeline emptyKeyAnimation;

    private final AdfgvxViewModel viewModel = new AdfgvxViewModel();

    public AdfgvxPage() {
        super();

        addSection("ADFGVX", section());
        bindComponents();
        viewModel.onInit();
    }

    private Node section() {
        var description = BBCodeParser.createFormattedText("The ADFGX, later extended by ADFGVX, was a field " +
                "cipher used by the German Army during WWI.");

        var encryptButton = new Button("Encrypt");
        var decryptButton = new Button("Decrypt");
        encryptButton.setOnAction(event -> viewModel.action(true));
        decryptButton.setOnAction(event -> viewModel.action(false));

        toggleGroup = new ToggleGroup();
        unblockedModeToggleBtn.setToggleGroup(toggleGroup);
        blocksOf2ModeToggleBtn.setToggleGroup(toggleGroup);
        blocksOf5ModeToggleBtn.setToggleGroup(toggleGroup);
        unblockedModeToggleBtn.getStyleClass().add(Styles.LEFT_PILL);
        blocksOf2ModeToggleBtn.getStyleClass().add(Styles.CENTER_PILL);
        blocksOf5ModeToggleBtn.getStyleClass().add(Styles.RIGHT_PILL);

        var outputModeHBox = new HBox(unblockedModeToggleBtn, blocksOf2ModeToggleBtn, blocksOf5ModeToggleBtn);

        InputGroup keyInputGroup = new InputGroup(keyLabel, keyTextField);
        emptyKeyAnimation = Animations.wobble(keyInputGroup);

        var controlsHBox = new HBox(
                20,
                encryptButton,
                decryptButton,
                keyInputGroup
        );

        var copyResultButton = UIElementFactory.createCopyButton(outputTextArea);
        var footerHBox = new HBox(
                20,
                copyResultButton,
                outputModeHBox,
                counterLabel
        );
        footerHBox.setAlignment(Pos.CENTER_LEFT);

        return new VBox(
                20,
                description,
                inputTextArea,
                controlsHBox,
                outputTextArea,
                footerHBox
        );
    }

    private void bindComponents() {
        inputTextArea.textProperty().bindBidirectional(viewModel.inputTextProperty());
        outputTextArea.textProperty().bindBidirectional(viewModel.outputTextProperty());
        keyTextField.textProperty().bindBidirectional(viewModel.keyTextProperty());
        counterLabel.textProperty().bindBidirectional(viewModel.counterTextProperty());

        unblockedModeToggleBtn.selectedProperty().bindBidirectional(viewModel.unblockedModeToggleButtonProperty());
        blocksOf2ModeToggleBtn.selectedProperty().bindBidirectional(viewModel.blocksOf2ModeToggleButtonProperty());
        blocksOf5ModeToggleBtn.selectedProperty().bindBidirectional(viewModel.blocksOf5ModeToggleButtonProperty());

        blocksOf5ModeToggleBtn.setSelected(true);
        toggleGroup.selectedToggleProperty().addListener(viewModel::onToggleChanged);
        viewModel.setEmptyKeyAnimation(emptyKeyAnimation);
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

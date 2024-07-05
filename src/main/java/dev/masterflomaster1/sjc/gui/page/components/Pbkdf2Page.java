package dev.masterflomaster1.sjc.gui.page.components;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.sjc.MemCache;
import dev.masterflomaster1.sjc.crypto.PbeImpl;
import dev.masterflomaster1.sjc.crypto.SecurityUtils;
import dev.masterflomaster1.sjc.gui.page.SimplePage;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Base64;
import java.util.Set;

public final class Pbkdf2Page extends SimplePage {

    public static final String NAME = "PBKDF2";

    private final TextField passwordInputField = new TextField();
    private final TextField iterationsInputTextField = new TextField();
    private final TextField keyLengthInputTextField = new TextField();
    private final TextArea outputTextArea = new TextArea();

    private final ComboBox<String> pbkdfComboBox = new ComboBox<>();

    public Pbkdf2Page() {
        super();
        addSection("PBKDF2", mainSection());

        onInit();
    }

    private Node mainSection() {
        var description = BBCodeParser.createFormattedText(
                "PBKDF2 applies a pseudorandom function, such as hash-based message authentication code (HMAC)," +
                        " to the input password or passphrase along with a salt value and repeats the process many" +
                        " times to produce a derived key, which can then be used as a cryptographic key in subsequent" +
                        " operations. The added computational work makes password cracking much more difficult, and" +
                        " is known as key stretching."
        );

        var passwordInputGroup = new InputGroup(keyLabel, passwordInputField);

        var saltInputLabel = new Label("Salt");
        var saltInputField = new TextField();
        var saltInputButton = new Button("", new FontIcon(BootstrapIcons.SHUFFLE));
        var saltInputGroup = new InputGroup(saltInputLabel, saltInputField, saltInputButton);

        iterationsInputTextField.setText("10000");
        var iterationsInputLabel = new Label("Iterations");
        var iterationsInputGroup = new InputGroup(iterationsInputLabel, iterationsInputTextField);

        keyLengthInputTextField.setText("128");
        var keyLengthInputLabel = new Label("Key bit-length");
        var keyLengthInputGroup = new InputGroup(keyLengthInputLabel, keyLengthInputTextField);

        Set<String> set = SecurityUtils.getPbkdfs();
        pbkdfComboBox.getItems().setAll(set);
        pbkdfComboBox.getSelectionModel().select(0);

        var runButton = new Button("Run");
        runButton.setOnAction(event -> action());

        outputTextArea.setEditable(false);
        outputTextArea.setWrapText(true);

        return new VBox(
                20,
                description,
                passwordInputGroup,
                saltInputGroup,
                iterationsInputGroup,
                keyLengthInputGroup,
                pbkdfComboBox,
                runButton,
                outputTextArea
        );
    }

    private void action() {
        if (passwordInputField.getText().isEmpty())
            return;

        if (iterationsInputTextField.getText().isEmpty())
            return;

        if (keyLengthInputTextField.getText().isEmpty())
            return;

        var algo = pbkdfComboBox.getValue();
        var pass = passwordInputField.getText().toCharArray();
        var salt = Base64.getDecoder().decode("4WHuOVNv8nIwjrPhLpyPwA==");
        var iter = Integer.parseInt(iterationsInputTextField.getText());
        var lKey = Integer.parseInt(keyLengthInputTextField.getText());

        var future = PbeImpl.asyncHash(algo, pass, salt, iter, lKey);

        future
                .thenAccept(bytes -> {
                    outputTextArea.setText(Base64.getEncoder().encodeToString(bytes));
                })
                .exceptionally(ex -> {
                    System.err.println(ex.getMessage());
                    return null;
                });

    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void onInit() {
        pbkdfComboBox.getSelectionModel().select(MemCache.readInteger("pbkdf2.algo", 0));
    }

    @Override
    public void onReset() {
        MemCache.writeInteger("pbkdf2.algo", pbkdfComboBox.getItems().indexOf(pbkdfComboBox.getValue()));
    }

}

package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import dev.masterflomaster1.jfxc.crypto.SignatureImpl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.HexFormat;

public class SignatureTextViewModel extends AbstractByteFormattingViewModel {

    private final StringProperty inputText = new SimpleStringProperty();
    private final StringProperty resultText = new SimpleStringProperty();
    private final ObjectProperty<String> signatureComboBoxProperty = new SimpleObjectProperty<>();
    private final ObservableList<String> signatureAlgorithmsList = FXCollections.observableArrayList();
    private final BooleanProperty signToggleButtonProperty = new SimpleBooleanProperty();
    private final BooleanProperty verifyToggleButtonProperty = new SimpleBooleanProperty();

    private KeyPair keyPair;

    public SignatureTextViewModel() {
        signatureAlgorithmsList.setAll(SecurityUtils.getSignatures());
        signatureComboBoxProperty.set(signatureAlgorithmsList.get(0));
    }

    public StringProperty inputTextProperty() {
        return inputText;
    }

    public StringProperty getResultTextProperty() {
        return resultText;
    }

    public ObjectProperty<String> signatureComboBoxProperty() {
        return signatureComboBoxProperty;
    }

    public ObservableList<String> getSignatureAlgorithmsList() {
        return signatureAlgorithmsList;
    }

    public BooleanProperty signToggleButtonProperty() {
        return signToggleButtonProperty;
    }

    public BooleanProperty verifyToggleButtonProperty() {
        return verifyToggleButtonProperty;
    }

    public void onKeyPairGenerateAction(@SuppressWarnings("unused") ActionEvent actionEvent) {
        keyPair = SignatureImpl.generateKey(signatureComboBoxProperty.get());
    }

    public void onModeToggleChanged(@SuppressWarnings("unused") ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        if (newValue == null) {
            if (oldValue != null)
                oldValue.setSelected(true);
            return;
        }

        var selectedButton = (ToggleButton) newValue;

        // bypass unpredictable behavior of ToggleButtonProperty.get(). Temporary solution
        if (selectedButton.getText().equalsIgnoreCase("Sign")) {
            signToggleButtonProperty.set(true);
            verifyToggleButtonProperty.set(false);
        } else if (selectedButton.getText().equalsIgnoreCase("Verify")) {
            verifyToggleButtonProperty.set(true);
            signToggleButtonProperty.set(false);
        }
    }

    public void action() {
        if (inputText.get().isEmpty())
            return;

        if (keyPair == null)
            return;

        var algo = signatureComboBoxProperty.get();
        var value = inputText.get().getBytes(StandardCharsets.UTF_8);

        if (signToggleButtonProperty.get()) {
            var signed = SignatureImpl.sign(algo, keyPair.getPrivate(), value);
            outputText.set(formatOutput(signed));
        } else if (verifyToggleButtonProperty.get()) {
            if (outputText.get().isEmpty())
                return;

            var sign = HexFormat.of().parseHex(outputText.get());

            boolean res = SignatureImpl.verify(algo, keyPair.getPublic(), sign, value);

            if (res) {
                resultText.set("Valid");
            } else {
                resultText.set("Invalid");
            }
        }
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onReset() {

    }
}

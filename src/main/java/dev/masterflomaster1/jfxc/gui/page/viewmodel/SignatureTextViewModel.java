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
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;

public class SignatureTextViewModel extends AbstractViewModel {

    @Getter
    private final StringProperty resultText = new SimpleStringProperty();

    @Getter
    private final ObjectProperty<String> signatureComboBoxProperty = new SimpleObjectProperty<>();

    @Getter
    private final ObservableList<String> signatureAlgorithmsList = FXCollections.observableArrayList();

    @Getter
    private final BooleanProperty signToggleButtonProperty = new SimpleBooleanProperty();

    @Getter
    private final BooleanProperty verifyToggleButtonProperty = new SimpleBooleanProperty();

    @Setter private KeyPairViewModelComponent keyPairViewModelComponent;
    @Setter private InputOutputAreaComponentViewModel ioComponentViewModel;

    private KeyPair keyPair;

    public SignatureTextViewModel() {
        signatureAlgorithmsList.setAll(SecurityUtils.getSignatures());
        signatureComboBoxProperty.set(signatureAlgorithmsList.get(0));
    }

    public void onKeyPairGenerateAction(@SuppressWarnings("unused") ActionEvent actionEvent) {
        keyPair = SignatureImpl.generateKey(signatureComboBoxProperty.get());
        keyPairViewModelComponent.onKeyPairChanged(keyPair);
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
        if (ioComponentViewModel.getInputProperty().get().isEmpty())
            return;

        if (keyPair == null)
            return;

        var algo = signatureComboBoxProperty.get();
        var value = ioComponentViewModel.getInputProperty().get().getBytes(StandardCharsets.UTF_8);

        if (signToggleButtonProperty.get()) {
            var signed = SignatureImpl.sign(algo, keyPair.getPrivate(), value);
            ioComponentViewModel.setOutput(signed);
        } else if (verifyToggleButtonProperty.get()) {
            if (ioComponentViewModel.outputDisplayModeProperty.get().isEmpty())
                return;

            var sign = ioComponentViewModel.getOutput();

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

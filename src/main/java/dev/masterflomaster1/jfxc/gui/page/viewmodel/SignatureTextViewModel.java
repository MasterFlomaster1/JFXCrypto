package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import dev.masterflomaster1.jfxc.crypto.SignatureImpl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import lombok.Getter;
import lombok.Setter;

import java.security.KeyPair;

public class SignatureTextViewModel extends AbstractViewModel {

    @Getter private final ObjectProperty<String> signatureComboBoxProperty = new SimpleObjectProperty<>();
    @Getter private final ObservableList<String> signatureAlgorithmsList = FXCollections.observableArrayList();
    @Getter private final StringProperty resultText = new SimpleStringProperty();

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

    public void onSign() {
        if (ioComponentViewModel.getInputProperty().get().isEmpty())
            return;

        if (keyPair == null)
            return;

        var algo = signatureComboBoxProperty.get();
        var value = ioComponentViewModel.getInput();

        var signed = SignatureImpl.sign(algo, keyPair.getPrivate(), value);
        ioComponentViewModel.setOutput(signed);
    }

    public void onVerify() {
        if (ioComponentViewModel.getInputProperty().get().isEmpty())
            return;

        if (keyPair == null)
            return;

        var algo = signatureComboBoxProperty.get();
        var value = ioComponentViewModel.getInput();

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

    @Override
    public void onInit() {

    }

    @Override
    public void onReset() {

    }
}

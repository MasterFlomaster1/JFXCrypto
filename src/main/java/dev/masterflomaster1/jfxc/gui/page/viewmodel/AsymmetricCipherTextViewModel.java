package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.crypto.AsymmetricCipherImpl;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
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
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HexFormat;

public class AsymmetricCipherTextViewModel extends AbstractViewModel {

    private final StringProperty inputText = new SimpleStringProperty();
    private final StringProperty outputText = new SimpleStringProperty();
    private final StringProperty publicKeyText = new SimpleStringProperty();
    private final StringProperty privateKeyText = new SimpleStringProperty();
    private final ObjectProperty<String> asymmetricCipherComboBoxProperty = new SimpleObjectProperty<>();
    private final ObservableList<String> asymmetricCipherAlgorithmsList = FXCollections.observableArrayList();
    private final StringProperty counterText = new SimpleStringProperty();

    private final BooleanProperty hexModeToggleButtonProperty = new SimpleBooleanProperty();
    private final BooleanProperty b64ModeToggleButtonProperty = new SimpleBooleanProperty();

    public AsymmetricCipherTextViewModel() {
        asymmetricCipherAlgorithmsList.setAll(SecurityUtils.getAsymmetricCiphers());
    }

    public StringProperty inputTextProperty() {
        return inputText;
    }

    public StringProperty outputTextProperty() {
        return outputText;
    }

    public StringProperty publicKeyTextProperty() {
        return publicKeyText;
    }

    public StringProperty privateKeyTextProperty() {
        return privateKeyText;
    }

    public StringProperty counterTextProperty() {
        return counterText;
    }

    public ObjectProperty<String> asymmetricCipherComboBoxProperty() {
        return asymmetricCipherComboBoxProperty;
    }

    public ObservableList<String> getAsymmetricCipherAlgorithmsList() {
        return asymmetricCipherAlgorithmsList;
    }

    public BooleanProperty hexModeToggleButtonProperty() {
        return hexModeToggleButtonProperty;
    }

    public BooleanProperty b64ModeToggleButtonProperty() {
        return b64ModeToggleButtonProperty;
    }

    @SuppressWarnings("unused")
    public void onGenerateKeysAction(ActionEvent actionEvent) {
        var pair = AsymmetricCipherImpl.generateKeyPair(AsymmetricCipherImpl.getProperKeyGenAlgorithm(asymmetricCipherComboBoxProperty.get()));

        var pub = HexFormat.of().formatHex(pair.getPublic().getEncoded());
        var prt = HexFormat.of().formatHex(pair.getPrivate().getEncoded());

        publicKeyText.set(pub);
        privateKeyText.set(prt);
    }

    @SuppressWarnings("unused")
    public void onToggleChanged(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        if (newValue == null) {
            if (oldValue != null)
                oldValue.setSelected(true);
            return;
        }

        var selectedButton = (ToggleButton) newValue;

        // bypass unpredictable behavior of ToggleButtonProperty.get()
        if (selectedButton.getText().equalsIgnoreCase("Hex")) {
            hexModeToggleButtonProperty.set(true);
            b64ModeToggleButtonProperty.set(false);
        } else if (selectedButton.getText().equalsIgnoreCase("Base64")) {
            b64ModeToggleButtonProperty.set(true);
            hexModeToggleButtonProperty.set(false);
        }
    }

    public void action(boolean encrypt) {
        if (inputText.get().isEmpty())
            return;

        if (publicKeyText.get().isEmpty())
            return;

        if (privateKeyText.get().isEmpty())
            return;

        var algo = asymmetricCipherComboBoxProperty.get();

        var text = inputText.get().getBytes(StandardCharsets.UTF_8);
        PublicKey pubKey;
        PrivateKey prtKey;

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(AsymmetricCipherImpl.getProperKeyGenAlgorithm(algo), "BC");
            pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(HexFormat.of().parseHex(publicKeyText.get())));
            prtKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(HexFormat.of().parseHex(privateKeyText.get())));
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        byte[] value;

        if (encrypt) {
            value = AsymmetricCipherImpl.encrypt(algo, text, pubKey);
            counterText.set("Encoded %d bytes".formatted(value.length));
            outputText.set(formatOutput(value));
        } else {
            var input = HexFormat.of().parseHex(inputText.get());

            value = AsymmetricCipherImpl.decrypt(algo, input, prtKey);
            counterText.set("Decoded %d bytes".formatted(value.length));
            outputText.set(new String(value));
        }

    }

    private String formatOutput(byte[] value) {
        if (hexModeToggleButtonProperty.get()) {
            return HexFormat.of().formatHex(value);
        } else if (b64ModeToggleButtonProperty.get()) {
            return Base64.getEncoder().encodeToString(value);
        }

        return "";
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onReset() {

    }
}

package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.AsymmetricCipherImpl;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HexFormat;

public final class AsymmetricCipherTextViewModel extends AbstractByteFormattingViewModel {

    private final StringProperty inputText = new SimpleStringProperty();
    private final StringProperty outputText = new SimpleStringProperty();
    private final StringProperty publicKeyText = new SimpleStringProperty();
    private final StringProperty privateKeyText = new SimpleStringProperty();
    private final ObjectProperty<String> asymmetricCipherComboBoxProperty = new SimpleObjectProperty<>();
    private final ObservableList<String> asymmetricCipherAlgorithmsList = FXCollections.observableArrayList();
    private final ObjectProperty<String> keyOptionsComboBoxProperty = new SimpleObjectProperty<>();
    private final ObservableList<String> keyOptionsList = FXCollections.observableArrayList();

    public AsymmetricCipherTextViewModel() {
        asymmetricCipherAlgorithmsList.setAll(SecurityUtils.getAsymmetricCiphers());
        asymmetricCipherComboBoxProperty.addListener((observable, oldValue, newValue) -> {
            onAlgorithmSelection(null);
        });
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

    public ObjectProperty<String> asymmetricCipherComboBoxProperty() {
        return asymmetricCipherComboBoxProperty;
    }

    public ObservableList<String> getAsymmetricCipherAlgorithmsList() {
        return asymmetricCipherAlgorithmsList;
    }

    public ObjectProperty<String> keyOptionsComboBoxProperty() {
        return keyOptionsComboBoxProperty;
    }

    public ObservableList<String> getKeyOptionsList() {
        return keyOptionsList;
    }

    @SuppressWarnings("unused")
    public void onAlgorithmSelection(ActionEvent event) {
        var algo = asymmetricCipherComboBoxProperty.get();
        keyOptionsList.setAll(AsymmetricCipherImpl.getAvailableKeyOptions(algo));
        keyOptionsComboBoxProperty.set(keyOptionsList.get(0));
    }

    @SuppressWarnings("unused")
    public void onGenerateKeysAction(ActionEvent actionEvent) {
        var pair = AsymmetricCipherImpl.generateKeyPair(
                AsymmetricCipherImpl.getProperKeyGenAlgorithm(asymmetricCipherComboBoxProperty.get()),
                keyOptionsComboBoxProperty.get()
        );

        var pub = HexFormat.of().formatHex(pair.getPublic().getEncoded());
        var prt = HexFormat.of().formatHex(pair.getPrivate().getEncoded());

        publicKeyText.set(pub);
        privateKeyText.set(prt);
    }

    public void onPublicKeyImport(File file) {
        var algo = asymmetricCipherComboBoxProperty.get();
        var keyGenAlgo = AsymmetricCipherImpl.getProperKeyGenAlgorithm(algo);

        var key = SecurityUtils.getPemKeyPairPersistence().importPublicKey(file.toPath(), keyGenAlgo);
        publicKeyText.set(HexFormat.of().formatHex(key.getEncoded()));
    }

    public void onPublicKeyExport(File file) {
        var algo = asymmetricCipherComboBoxProperty.get();
        var keyGenAlgo = AsymmetricCipherImpl.getProperKeyGenAlgorithm(algo);
        var key = HexFormat.of().parseHex(publicKeyText.get());

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(keyGenAlgo);
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(key));

            SecurityUtils.getPemKeyPairPersistence().exportPublicKey(file.toPath(), publicKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public void onPrivateKeyImport(File file) {
        var algo = asymmetricCipherComboBoxProperty.get();
        var keyGenAlgo = AsymmetricCipherImpl.getProperKeyGenAlgorithm(algo);

        var key = SecurityUtils.getPemKeyPairPersistence().importPrivateKey(file.toPath(), keyGenAlgo);
        privateKeyText.set(HexFormat.of().formatHex(key.getEncoded()));
    }

    public void onPrivateKeyExport(File file) {
        var algo = asymmetricCipherComboBoxProperty.get();
        var keyGenAlgo = AsymmetricCipherImpl.getProperKeyGenAlgorithm(algo);
        var key = HexFormat.of().parseHex(privateKeyText.get());

        try {
            KeyFactory keyFactory = KeyFactory.getInstance(keyGenAlgo);
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(key));

            SecurityUtils.getPemKeyPairPersistence().exportPrivateKey(file.toPath(), privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
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

    @Override
    public void onInit() {
        inputText.set(MemCache.readString("asymmetric.input", ""));
        asymmetricCipherComboBoxProperty.set(asymmetricCipherAlgorithmsList.get(MemCache.readInteger("asymmetric.algo", 0)));
        keyOptionsComboBoxProperty.set(keyOptionsList.get(MemCache.readInteger("asymmetric.option", 0)));
        publicKeyText.set(MemCache.readString("asymmetric.public.key", ""));
        privateKeyText.set(MemCache.readString("asymmetric.private.key", ""));
        outputText.set(MemCache.readString("asymmetric.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeString("asymmetric.input", inputText.get());
        MemCache.writeInteger("asymmetric.algo", asymmetricCipherAlgorithmsList.indexOf(asymmetricCipherComboBoxProperty.get()));
        MemCache.readInteger("asymmetric.option", keyOptionsList.indexOf(keyOptionsComboBoxProperty.get()));
        MemCache.writeString("asymmetric.public.key", publicKeyText.get());
        MemCache.writeString("asymmetric.private.key", privateKeyText.get());
        MemCache.writeString("asymmetric.output", outputText.get());
    }
}

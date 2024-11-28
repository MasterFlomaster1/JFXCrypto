package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.core.PemImpl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import lombok.Getter;
import lombok.Setter;

import java.security.KeyPair;
import java.util.Base64;
import java.util.HexFormat;

public class KeyPairViewModelComponent {

    @Getter final StringProperty publicKeyProperty = new SimpleStringProperty();
    @Getter final StringProperty privateKeyProperty = new SimpleStringProperty();

    @Getter final ObjectProperty<String> pubKeyDisplayModeProperty = new SimpleObjectProperty<>();
    @Getter final ObjectProperty<String> prvKeyDisplayModeProperty = new SimpleObjectProperty<>();

    @Getter
    @Setter
    private KeyPair keyPairBuffer;

    public void onPubKeyDisplayModeChanged(ActionEvent event) {
        if (keyPairBuffer == null)
            return;

        displayPubKey();
    }

    public void onPrvKeyDisplayModeChanged(ActionEvent event) {
        if (keyPairBuffer == null)
            return;

        displayPrvKey();
    }

    public void onKeyPairChanged(KeyPair keyPair) {
        this.keyPairBuffer = keyPair;

        displayPubKey();
        displayPrvKey();
    }

    private void displayPubKey() {
        switch (pubKeyDisplayModeProperty.get()) {
            case "PEM" -> publicKeyProperty.set(PemImpl.formatKey(keyPairBuffer.getPublic()));
            case "Base64" -> publicKeyProperty.set(Base64.getEncoder().encodeToString(keyPairBuffer.getPublic().getEncoded()));
            case "Hex" -> publicKeyProperty.set(HexFormat.of().formatHex(keyPairBuffer.getPublic().getEncoded()));
        }
    }

    private void displayPrvKey() {
        switch (prvKeyDisplayModeProperty.get()) {
            case "PEM" -> privateKeyProperty.set(PemImpl.formatKey(keyPairBuffer.getPrivate()));
            case "Base64" -> privateKeyProperty.set(Base64.getEncoder().encodeToString(keyPairBuffer.getPrivate().getEncoded()));
            case "Hex" -> privateKeyProperty.set(HexFormat.of().formatHex(keyPairBuffer.getPrivate().getEncoded()));
        }
    }




}

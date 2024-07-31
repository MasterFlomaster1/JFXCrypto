package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import dev.masterflomaster1.jfxc.crypto.HashImpl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;

public final class HashFilesViewModel extends AbstractByteFormattingViewModel {

    private final StringProperty outputText = new SimpleStringProperty();
    private final ObjectProperty<String> hashComboBoxProperty = new SimpleObjectProperty<>();
    private final ObservableList<String> hashAlgorithmsList = FXCollections.observableArrayList();

    private File selectedFile;

    public HashFilesViewModel() {
        hashAlgorithmsList.setAll(SecurityUtils.getDigests());
    }

    public StringProperty outputTextProperty() {
        return outputText;
    }

    public ObjectProperty<String> hashComboBoxPropertyProperty() {
        return hashComboBoxProperty;
    }

    public ObservableList<String> hashAlgorithmsList() {
        return hashAlgorithmsList;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    public void action() {
        if (selectedFile == null)
            return;

        var completableFuture = HashImpl.asyncHash(hashComboBoxProperty.get(), selectedFile.getAbsolutePath());
        completableFuture
                .thenAccept(hash -> outputText.set(formatOutput(hash)))
                .exceptionally(ex -> {
                    System.out.println(ex.getMessage());
                    return null;
                });
    }

    @Override
    public void onInit() {
        hashComboBoxProperty.set(hashAlgorithmsList.get(MemCache.readInteger("hash.files.algo", 0)));
        outputText.set(MemCache.readString("hash.files.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeInteger("hash.files.algo", hashAlgorithmsList.indexOf(hashComboBoxProperty.get()));
        MemCache.writeString("hash.files.output", outputText.get());
    }
}

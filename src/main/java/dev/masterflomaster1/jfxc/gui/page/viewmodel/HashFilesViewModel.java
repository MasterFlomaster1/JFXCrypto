package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import dev.masterflomaster1.jfxc.crypto.HashImpl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

public final class HashFilesViewModel extends ByteFormattingViewModel {

    @Getter private final ObjectProperty<String> hashComboBoxProperty = new SimpleObjectProperty<>();
    @Getter private final ObservableList<String> hashAlgorithmsList = FXCollections.observableArrayList();

    @Setter private File selectedFile;

    public HashFilesViewModel() {
        hashAlgorithmsList.setAll(SecurityUtils.getDigests());
    }

    public void action() {
        if (selectedFile == null)
            return;

        var completableFuture = HashImpl.asyncHash(hashComboBoxProperty.get(), selectedFile.getAbsolutePath());
        completableFuture
                .thenAccept(hash -> outputProperty.set(formatOutput(hash)))
                .exceptionally(ex -> {
                    System.out.println(ex.getMessage());
                    return null;
                });
    }

    @Override
    public void onInit() {
        hashComboBoxProperty.set(hashAlgorithmsList.get(MemCache.readInteger("hash.files.algo", 0)));
        outputProperty.set(MemCache.readString("hash.files.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeInteger("hash.files.algo", hashAlgorithmsList.indexOf(hashComboBoxProperty.get()));
        MemCache.writeString("hash.files.output", outputProperty.get());
    }
}

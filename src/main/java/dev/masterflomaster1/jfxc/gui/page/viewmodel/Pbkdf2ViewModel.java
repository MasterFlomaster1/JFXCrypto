package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.PbeImpl;
import dev.masterflomaster1.jfxc.crypto.SecurityUtils;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.HexFormat;

public final class Pbkdf2ViewModel extends ByteFormattingViewModel {

    @Getter private final StringProperty passwordProperty = new SimpleStringProperty();
    @Getter private final StringProperty iterationsProperty = new SimpleStringProperty();
    @Getter private final StringProperty keyLengthProperty = new SimpleStringProperty();
    @Getter private final StringProperty saltTextProperty = new SimpleStringProperty();
    @Getter private final ObjectProperty<String> pbkdf2ComboBoxProperty = new SimpleObjectProperty<>();
    @Getter private final ObservableList<String> pbkdf2AlgorithmsList = FXCollections.observableArrayList();

    @Setter private Timeline emptyPasswordAnimation;
    @Setter private Timeline emptyIterationsAnimation;
    @Setter private Timeline emptyKeyLengthAnimation;
    @Setter private Timeline emptySaltAnimation;

    public Pbkdf2ViewModel() {
        pbkdf2AlgorithmsList.setAll(SecurityUtils.getPbkdfs());
    }

    public void onSaltShuffleAction(@SuppressWarnings("unused") ActionEvent e) {
        saltTextProperty.set(HexFormat.of().formatHex(SecurityUtils.generateSalt()));
    }

    public void action() {
        if (passwordProperty.get().isEmpty()) {
            emptyPasswordAnimation.playFromStart();
            return;
        }

        if (iterationsProperty.get().isEmpty()) {
            emptyIterationsAnimation.playFromStart();
            return;
        }

        if (keyLengthProperty.get().isEmpty()) {
            emptyKeyLengthAnimation.playFromStart();
            return;
        }

        if (saltTextProperty.get().isEmpty()) {
            emptySaltAnimation.playFromStart();
            return;
        }

        var algo = pbkdf2ComboBoxProperty.get();
        var pass = passwordProperty.get().toCharArray();
        var salt = HexFormat.of().parseHex(saltTextProperty.get());
        var iter = Integer.parseInt(iterationsProperty.get());
        var lKey = Integer.parseInt(keyLengthProperty.get());

        var completableFuture = PbeImpl.asyncHash(algo, pass, salt, iter, lKey);
        completableFuture
                .thenAccept(bytes -> {
                    outputProperty.set(formatOutput(bytes));
                })
                .exceptionally(ex -> {
                    System.err.println(ex.getMessage());
                    return null;
                });

    }

    @Override
    public void onInit() {
        pbkdf2ComboBoxProperty.set(pbkdf2AlgorithmsList.get(MemCache.readInteger("pbkdf2.algo", 0)));
        passwordProperty.set(MemCache.readString("pbkdf2.password", ""));
        keyLengthProperty.set(MemCache.readString("pbkdf2.key.len", "128"));
        iterationsProperty.set(MemCache.readString("pbkdf2.iterations", "10000"));
        saltTextProperty.set(MemCache.readString("pbkdf2.salt", ""));
        outputProperty.set(MemCache.readString("pbkdf2.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeInteger("pbkdf2.algo", pbkdf2AlgorithmsList.indexOf(pbkdf2ComboBoxProperty.get()));
        MemCache.writeString("pbkdf2.password", passwordProperty.get());
        MemCache.writeString("pbkdf2.key.len", keyLengthProperty.get());
        MemCache.writeString("pbkdf2.iterations", iterationsProperty.get());
        MemCache.writeString("pbkdf2.salt", saltTextProperty.get());
        MemCache.writeString("pbkdf2.output", outputProperty.get());
    }
}

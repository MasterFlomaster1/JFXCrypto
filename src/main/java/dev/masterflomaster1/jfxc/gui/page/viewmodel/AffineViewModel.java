package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.classic.AffineCipherImpl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AffineViewModel extends AbstractViewModel {

    private final StringProperty inputText = new SimpleStringProperty();
    private final StringProperty outputText = new SimpleStringProperty();
    private final ObjectProperty<Integer> slopeProperty = new SimpleObjectProperty<>();
    private final ObservableList<Integer> slopeList = FXCollections.observableArrayList();
    private final ObjectProperty<Integer> interceptProperty = new SimpleObjectProperty<>();
    private final ObservableList<Integer> interceptList = FXCollections.observableArrayList();
    private final StringProperty counterText = new SimpleStringProperty();

    public AffineViewModel() {
        slopeList.setAll(AffineCipherImpl.SLOPE);
        slopeProperty.set(slopeList.get(0));

        interceptList.setAll(AffineCipherImpl.INTERCEPT);
        interceptProperty.set(interceptList.get(0));
    }

    public StringProperty inputTextProperty() {
        return inputText;
    }

    public StringProperty outputTextProperty() {
        return outputText;
    }

    public ObjectProperty<Integer> slopeProperty() {
        return slopeProperty;
    }

    public ObservableList<Integer> getSlopeList() {
        return slopeList;
    }

    public ObjectProperty<Integer> interceptProperty() {
        return interceptProperty;
    }

    public ObservableList<Integer> getInterceptList() {
        return interceptList;
    }

    public StringProperty counterTextProperty() {
        return counterText;
    }

    public void action(boolean encrypt) {
        if (inputText.get().isEmpty())
            return;

        var a = slopeProperty.get();
        var b = interceptProperty.get();

        String value;

        if (encrypt) {
            value = AffineCipherImpl.encrypt(inputText.get(), a, b);
            counterText.set("Encoded %d chars".formatted(value.length()));
        } else {
            value = AffineCipherImpl.decrypt(inputText.get(), a, b);
            counterText.set("Decoded %d chars".formatted(value.length()));
        }

        outputText.set(value);
    }

    @Override
    public void onInit() {
        inputText.set(MemCache.readString("affine.input", ""));
        outputText.set(MemCache.readString("affine.output", ""));
        slopeProperty.set(slopeList.get(MemCache.readInteger("affine.slope", 0)));
        interceptProperty.set(interceptList.get(MemCache.readInteger("affine.intercept", 0)));
    }

    @Override
    public void onReset() {
        MemCache.writeString("affine.input", inputText.get());
        MemCache.writeString("affine.output", outputText.get());
        MemCache.writeInteger("affine.slope", slopeList.indexOf(slopeProperty.get()));
        MemCache.writeInteger("affine.intercept", interceptList.indexOf(interceptProperty.get()));
    }
}

package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.MemCache;
import dev.masterflomaster1.jfxc.crypto.enigma.Enigma;
import dev.masterflomaster1.jfxc.utils.StringUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public final class EnigmaViewModel extends AbstractViewModel {

    private final StringProperty inputProperty = new SimpleStringProperty();
    private final StringProperty outputProperty = new SimpleStringProperty();
    private final StringProperty plugboardProperty = new SimpleStringProperty();

    private final ObjectProperty<String> reflectorsProperty = new SimpleObjectProperty<>();
    private final ObservableList<String> reflectorsList = FXCollections.observableArrayList();

    private final ObservableList<String> typesList = FXCollections.observableArrayList();

    private final ObjectProperty<String> rotor1TypeProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<String> rotor2TypeProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<String> rotor3TypeProperty = new SimpleObjectProperty<>();

    private final IntegerProperty rotor1PositionProperty = new SimpleIntegerProperty();
    private final IntegerProperty rotor2PositionProperty = new SimpleIntegerProperty();
    private final IntegerProperty rotor3PositionProperty = new SimpleIntegerProperty();

    private final IntegerProperty rotor1RingProperty = new SimpleIntegerProperty();
    private final IntegerProperty rotor2RingProperty = new SimpleIntegerProperty();
    private final IntegerProperty rotor3RingProperty = new SimpleIntegerProperty();

    private final BooleanProperty unblockedModeToggleButtonProperty = new SimpleBooleanProperty();
    private final BooleanProperty blocksOf5ModeToggleButtonProperty = new SimpleBooleanProperty();

    public EnigmaViewModel() {
        // Recalculate when selecting another reflector
        reflectorsProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue == null)
                return;

            action();
        });

        reflectorsList.setAll("UKW B", "UKW C");
        reflectorsProperty.set(reflectorsList.get(0));

        typesList.setAll("I", "II", "III", "IV", "V", "VI", "VII", "VIII");

        rotor1TypeProperty.addListener(this::onRotorTypeChange);
        rotor2TypeProperty.addListener(this::onRotorTypeChange);
        rotor3TypeProperty.addListener(this::onRotorTypeChange);

        rotor1PositionProperty.addListener(this::onRotorPosChange);
        rotor2PositionProperty.addListener(this::onRotorPosChange);
        rotor3PositionProperty.addListener(this::onRotorPosChange);

        rotor1RingProperty.addListener(this::onRotorPosChange);
        rotor2RingProperty.addListener(this::onRotorPosChange);
        rotor3RingProperty.addListener(this::onRotorPosChange);
    }

    @SuppressWarnings("unused")
    public void onRotorTypeChange(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (oldValue == null || newValue == null) return;
        if (oldValue.equals(newValue)) return;

        action();
    }

    @SuppressWarnings("unused")
    public void onRotorPosChange(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        if (Objects.equals(oldValue, newValue)) return;

        action();
    }

    @SuppressWarnings("unused")
    public void onInputTextChange(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (oldValue == null || newValue == null) return;
        if (oldValue.equals(newValue)) return;

        action();
    }

    @SuppressWarnings("unused")
    public void onToggleChanged(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        if (newValue == null) {
            if (oldValue != null)
                oldValue.setSelected(true);
            return;
        }

        if (outputProperty.get().isEmpty())
            return;

        var val = outputProperty.get();
        var selectedButton = (ToggleButton) newValue;

        // bypass unpredictable behavior of ToggleButtonProperty.get()
        if (selectedButton.getText().equalsIgnoreCase("Blocks of 5")) {
            blocksOf5ModeToggleButtonProperty.set(true);
            unblockedModeToggleButtonProperty.set(false);
            outputProperty.set(StringUtils.spaceAfterN(val, 5));
        } else if (selectedButton.getText().equalsIgnoreCase("Unblocked")) {
            unblockedModeToggleButtonProperty.set(true);
            blocksOf5ModeToggleButtonProperty.set(false);
            outputProperty.set(StringUtils.removeSpaces(val));
        }
    }

    public void action() {
        if (inputProperty.get() == null) return;

        if (inputProperty.get().isEmpty())
            return;

        if (!plugboardProperty.get().isEmpty() && !isValidPlugboard(plugboardProperty.get().trim()))
            return;

        String ref = (reflectorsProperty.get().equals("UKW B")) ? "B" : "C";

        Enigma enigma = new Enigma(
                new String[]{
                        rotor1TypeProperty.get(),
                        rotor2TypeProperty.get(),
                        rotor3TypeProperty.get()
                },
                ref,
                new int[]{
                        rotor1PositionProperty.get(),
                        rotor2PositionProperty.get(),
                        rotor3PositionProperty.get()
                },
                new int[]{
                        rotor1RingProperty.get(),
                        rotor2RingProperty.get(),
                        rotor3RingProperty.get()
                },
                plugboardProperty.get().toUpperCase()
        );

        String input = StringUtils.removePunctuation(inputProperty.get());
        String val = new String(enigma.encrypt(input));

        if (unblockedModeToggleButtonProperty.get()) {
            outputProperty.set(val);
        } else {
            outputProperty.set(StringUtils.spaceAfterN(val, 5));
        }

        counterText.set("Encoded %d chars".formatted(val.length()));

    }

    public boolean isValidPlugboard(String input) {
        if (input.isEmpty()) return true;

        String[] pairsArray = input.split(" ");

        Set<String> pairs = new HashSet<>();
        Set<Character> characters = new HashSet<>();

        for (String pair : pairsArray) {
            if (pair.length() != 2) {
                return false;
            }

            char firstChar = pair.charAt(0);
            char secondChar = pair.charAt(1);

            if (!Character.isLetter(firstChar) || !Character.isLetter(secondChar)) {
                return false;
            }

            if (!characters.add(firstChar) || !characters.add(secondChar) || !pairs.add(pair)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onInit() {
        reflectorsProperty.set(reflectorsList.get(MemCache.readInteger("enigma.reflector", 0)));
        rotor1TypeProperty.set(typesList.get(MemCache.readInteger("enigma.rotor1.type", 0)));
        rotor2TypeProperty.set(typesList.get(MemCache.readInteger("enigma.rotor2.type", 0)));
        rotor3TypeProperty.set(typesList.get(MemCache.readInteger("enigma.rotor3.type", 0)));
        rotor1PositionProperty.set(MemCache.readInteger("enigma.rotor1.pos", 0));
        rotor2PositionProperty.set(MemCache.readInteger("enigma.rotor2.pos", 0));
        rotor3PositionProperty.set(MemCache.readInteger("enigma.rotor3.pos", 0));
        rotor1RingProperty.set(MemCache.readInteger("enigma.ring1", 0));
        rotor2RingProperty.set(MemCache.readInteger("enigma.ring2", 0));
        rotor3RingProperty.set(MemCache.readInteger("enigma.ring3", 0));
        inputProperty.set(MemCache.readString("enigma.input", ""));
        plugboardProperty.set(MemCache.readString("enigma.plugboard", ""));
        outputProperty.set(MemCache.readString("enigma.output", ""));
    }

    @Override
    public void onReset() {
        MemCache.writeInteger("enigma.reflector", reflectorsList.indexOf(reflectorsProperty.get()));
        MemCache.writeInteger("enigma.rotor1.type", typesList.indexOf(rotor1TypeProperty.get()));
        MemCache.writeInteger("enigma.rotor2.type", typesList.indexOf(rotor2TypeProperty.get()));
        MemCache.writeInteger("enigma.rotor3.type", typesList.indexOf(rotor3TypeProperty.get()));
        MemCache.writeInteger("enigma.rotor1.pos", rotor1PositionProperty.get());
        MemCache.writeInteger("enigma.rotor2.pos", rotor2PositionProperty.get());
        MemCache.writeInteger("enigma.rotor3.pos", rotor3PositionProperty.get());
        MemCache.writeInteger("enigma.ring1", rotor1RingProperty.get());
        MemCache.writeInteger("enigma.ring2", rotor2RingProperty.get());
        MemCache.writeInteger("enigma.ring3", rotor3RingProperty.get());
        MemCache.writeString("enigma.input", inputProperty.get());
        MemCache.writeString("enigma.plugboard", plugboardProperty.get());
        MemCache.writeString("enigma.output", outputProperty.get());
    }
}

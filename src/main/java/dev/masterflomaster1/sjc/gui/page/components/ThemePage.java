package dev.masterflomaster1.sjc.gui.page.components;

import dev.masterflomaster1.sjc.gui.page.SimplePage;
import dev.masterflomaster1.sjc.gui.theme.SamplerTheme;
import dev.masterflomaster1.sjc.gui.theme.ThemeManager;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.util.Objects;

public class ThemePage extends SimplePage {

    public static final String NAME = "Theme";

    private static final ThemeManager TM = ThemeManager.getInstance();
    private final ComboBox<SamplerTheme> themeSelector = createThemeSelector();

    public ThemePage() {
        super();

        addSection("Theme", mainSection());
    }

    private Node mainSection() {
        var accentSelector = new AccentColorSelector();

        // ~

        var grid = new GridPane();
        grid.setHgap(HGAP_20);
        grid.setVgap(VGAP_10);
        grid.addRow(0, new Label("Color theme"), themeSelector);
        grid.addRow(1, new Label("Accent color"), accentSelector);

        return grid;
    }

    private ComboBox<SamplerTheme> createThemeSelector() {
        var choiceBox = new ComboBox<SamplerTheme>();

        var themes = TM.getRepository().getAll();
        choiceBox.getItems().setAll(themes);

        // set initial value
        var currentTheme = Objects.requireNonNullElse(TM.getTheme(), TM.getDefaultTheme());
        themes.stream()
                .filter(t -> Objects.equals(currentTheme.getName(), t.getName()))
                .findFirst()
                .ifPresent(t -> choiceBox.getSelectionModel().select(t));

        // must be after setting the initial value
        choiceBox.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> {
            if (val != null && getScene() != null) {
                TM.setTheme(val);
            }
        });
        choiceBox.setPrefWidth(310);

        choiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(SamplerTheme theme) {
                return theme != null ? theme.getName() : "";
            }

            @Override
            public SamplerTheme fromString(String themeName) {
                return TM.getRepository().getAll().stream()
                        .filter(t -> Objects.equals(themeName, t.getName()))
                        .findFirst()
                        .orElse(null);
            }
        });

        return choiceBox;
    }

    @Override
    public String getName() {
        return NAME;
    }

}

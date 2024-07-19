package dev.masterflomaster1.jfxc.gui.theme;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.CupertinoLight;
import atlantafx.base.theme.Dracula;
import atlantafx.base.theme.NordDark;
import atlantafx.base.theme.NordLight;
import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.Theme;
import dev.masterflomaster1.jfxc.Resources;
import dev.masterflomaster1.jfxc.gui.event.DefaultEventBus;
import dev.masterflomaster1.jfxc.gui.event.EventBus;
import dev.masterflomaster1.jfxc.gui.event.ThemeEvent;
import dev.masterflomaster1.jfxc.gui.event.ThemeEvent.EventType;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Objects;
import java.util.Set;

import static dev.masterflomaster1.jfxc.Resources.getResource;

public final class ThemeManager {

    static final String DUMMY_STYLESHEET = getResource("assets/styles/empty.css").toString();
    static final String[] APP_STYLESHEETS = new String[] {
        Resources.resolve("assets/styles/index.css")
    };
    static final Set<Class<? extends Theme>> PROJECT_THEMES = Set.of(
        PrimerLight.class, PrimerDark.class,
        NordLight.class, NordDark.class,
        CupertinoLight.class, CupertinoDark.class,
        Dracula.class
    );

    private static final PseudoClass DARK = PseudoClass.getPseudoClass("dark");
    private static final PseudoClass USER_CUSTOM = PseudoClass.getPseudoClass("user-custom");
    private static final EventBus EVENT_BUS = DefaultEventBus.getInstance();

    public static final AccentColor DEFAULT_ACCENT_COLOR = null;

    private final ThemeRepository repository = new ThemeRepository();

    private Scene scene;

    private SamplerTheme currentTheme;
    private AccentColor accentColor = DEFAULT_ACCENT_COLOR;

    public ThemeRepository getRepository() {
        return repository;
    }

    public Scene getScene() {
        return scene;
    }

    // MUST BE SET ON STARTUP
    // (this is supposed to be a constructor arg, but since app don't use DI..., sorry)
    public void setScene(Scene scene) {
        this.scene = Objects.requireNonNull(scene);
    }

    public SamplerTheme getTheme() {
        return currentTheme;
    }

    public SamplerTheme getDefaultTheme() {
        return getRepository().getAll().get(1);
    }

    /**
     * See {@link SamplerTheme}.
     */
    public void setTheme(SamplerTheme theme) {
        Objects.requireNonNull(theme);

        if (currentTheme != null) {
            animateThemeChange(Duration.millis(500));
        }

        Application.setUserAgentStylesheet(Objects.requireNonNull(theme.getUserAgentStylesheet()));
        getScene().getStylesheets().setAll(theme.getAllStylesheets());
        getScene().getRoot().pseudoClassStateChanged(DARK, theme.isDarkMode());

        // remove user CSS customizations and reset accent on theme change
        resetAccentColor();
        resetCustomCSS();

        currentTheme = theme;
        EVENT_BUS.publish(new ThemeEvent(EventType.THEME_CHANGE));
    }

    public void setAccentColor(AccentColor color) {
        Objects.requireNonNull(color);

        animateThemeChange(Duration.millis(350));

        if (accentColor != null) {
            getScene().getRoot().pseudoClassStateChanged(accentColor.pseudoClass(), false);
        }

        getScene().getRoot().pseudoClassStateChanged(color.pseudoClass(), true);
        this.accentColor = color;

        EVENT_BUS.publish(new ThemeEvent(EventType.COLOR_CHANGE));
    }

    public void resetAccentColor() {
        animateThemeChange(Duration.millis(350));

        if (accentColor != null) {
            getScene().getRoot().pseudoClassStateChanged(accentColor.pseudoClass(), false);
            accentColor = null;
        }

        EVENT_BUS.publish(new ThemeEvent(EventType.COLOR_CHANGE));
    }

    private void animateThemeChange(Duration duration) {
        Image snapshot = scene.snapshot(null);
        Pane root = (Pane) scene.getRoot();

        ImageView imageView = new ImageView(snapshot);
        root.getChildren().add(imageView); // add snapshot on top

        var transition = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(imageView.opacityProperty(), 1, Interpolator.EASE_OUT)),
            new KeyFrame(duration, new KeyValue(imageView.opacityProperty(), 0, Interpolator.EASE_OUT))
        );
        transition.setOnFinished(e -> root.getChildren().remove(imageView));
        transition.play();
    }

    public void resetCustomCSS() {
        getScene().getRoot().pseudoClassStateChanged(USER_CUSTOM, false);
    }

    private ThemeManager() {
    }

    private final static class InstanceHolder {
        private static final ThemeManager INSTANCE = new ThemeManager();
    }

    public static ThemeManager getInstance() {
        return InstanceHolder.INSTANCE;
    }
}

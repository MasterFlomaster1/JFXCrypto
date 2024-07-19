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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;

public final class ThemeRepository {

    private static final Comparator<SamplerTheme> THEME_COMPARATOR = Comparator.comparing(SamplerTheme::getName);

    private final List<SamplerTheme> internalThemes = Arrays.asList(
        new SamplerTheme(new PrimerLight()),
        new SamplerTheme(new PrimerDark()),
        new SamplerTheme(new NordLight()),
        new SamplerTheme(new NordDark()),
        new SamplerTheme(new CupertinoLight()),
        new SamplerTheme(new CupertinoDark()),
        new SamplerTheme(new Dracula())
    );

    private final List<SamplerTheme> externalThemes = new ArrayList<>();
    private final Preferences themePreferences = Resources.getPreferences().node("theme");

    public ThemeRepository() {
        try {
            loadPreferences();
        } catch (BackingStoreException e) {
            System.out.println("[WARNING] Unable to load themes from the preferences.");
            e.printStackTrace();
        }
    }

    public List<SamplerTheme> getAll() {
        var list = new ArrayList<>(internalThemes);
        list.addAll(externalThemes);
        return list;
    }

    public boolean isFileValid(Path path) {
        Objects.requireNonNull(path);
        return !Files.isDirectory(path, NOFOLLOW_LINKS)
            && Files.isRegularFile(path, NOFOLLOW_LINKS)
            && Files.isReadable(path)
            && path.getFileName().toString().endsWith(".css");
    }

    private void loadPreferences() throws BackingStoreException {
        for (String themeName : themePreferences.keys()) {
            var uaStylesheet = themePreferences.get(themeName, "");
            var uaStylesheetPath = Paths.get(uaStylesheet);

            // cleanup broken links, e.g. if theme was added for testing
            // but then CSS file was removed from the filesystem
            if (!isFileValid(uaStylesheetPath)) {
                System.err.println(
                    "[WARNING] CSS file invalid or missing: \"" + uaStylesheetPath + "\". Removing silently.");
                themePreferences.remove(themeName);
                continue;
            }

            externalThemes.add(new SamplerTheme(
                Theme.of(themeName, uaStylesheet, uaStylesheetPath.getFileName().toString().contains("dark"))
            ));
            externalThemes.sort(THEME_COMPARATOR);
        }
    }

}

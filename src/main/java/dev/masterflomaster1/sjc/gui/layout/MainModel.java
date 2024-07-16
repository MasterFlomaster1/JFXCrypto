package dev.masterflomaster1.sjc.gui.layout;

import dev.masterflomaster1.sjc.gui.event.DefaultEventBus;
import dev.masterflomaster1.sjc.gui.event.NavEvent;
import dev.masterflomaster1.sjc.gui.page.Page;
import dev.masterflomaster1.sjc.gui.page.components.ADFGVXPage;
import dev.masterflomaster1.sjc.gui.page.components.AffinePage;
import dev.masterflomaster1.sjc.gui.page.components.AllHashPage;
import dev.masterflomaster1.sjc.gui.page.components.AtbashPage;
import dev.masterflomaster1.sjc.gui.page.components.BlockCipherFilesPage;
import dev.masterflomaster1.sjc.gui.page.components.BlockCipherPage;
import dev.masterflomaster1.sjc.gui.page.components.CaesarPage;
import dev.masterflomaster1.sjc.gui.page.components.EnigmaPage;
import dev.masterflomaster1.sjc.gui.page.components.HashFilesPage;
import dev.masterflomaster1.sjc.gui.page.components.HmacPage;
import dev.masterflomaster1.sjc.gui.page.components.PasswordStrengthPage;
import dev.masterflomaster1.sjc.gui.page.components.Pbkdf2Page;
import dev.masterflomaster1.sjc.gui.page.components.PlayfairCipherPage;
import dev.masterflomaster1.sjc.gui.page.components.PwnedPasswordsPage;
import dev.masterflomaster1.sjc.gui.page.components.StreamCipherTextPage;
import dev.masterflomaster1.sjc.gui.page.components.ThemePage;
import dev.masterflomaster1.sjc.gui.page.components.VigenereCipherPage;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainModel {

    public static final Class<? extends Page> DEFAULT_PAGE = AllHashPage.class;

    private static final Map<Class<? extends Page>, NavTree.Item> NAV_TREE = createNavItems();

    NavTree.Item getTreeItemForPage(Class<? extends Page> pageClass) {
        return NAV_TREE.getOrDefault(pageClass, NAV_TREE.get(DEFAULT_PAGE));
    }

    List<NavTree.Item> findPages(String filter) {
        return NAV_TREE.values().stream()
            .filter(item -> item.getValue() != null && item.getValue().matches(filter))
            .toList();
    }

    public MainModel() {
        DefaultEventBus.getInstance().subscribe(NavEvent.class, e -> navigate(e.getPage()));
    }

    private final ReadOnlyObjectWrapper<Class<? extends Page>> selectedPage = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Class<? extends Page>> selectedPageProperty() {
        return selectedPage.getReadOnlyProperty();
    }

    private final ReadOnlyObjectWrapper<NavTree.Item> navTree = new ReadOnlyObjectWrapper<>(createTree());

    public ReadOnlyObjectProperty<NavTree.Item> navTreeProperty() {
        return navTree.getReadOnlyProperty();
    }

    private NavTree.Item createTree() {
        var generalGroup = NavTree.Item.group("General", new FontIcon(BootstrapIcons.FILE_EARMARK_LOCK2));
        generalGroup.getChildren().setAll(
                NAV_TREE.get(ThemePage.class)
        );

        var classicalGroup = NavTree.Item.group("Classical Cryptography", new FontIcon(BootstrapIcons.FILE_EARMARK_LOCK2));
        classicalGroup.getChildren().setAll(
                NAV_TREE.get(ADFGVXPage.class),
                NAV_TREE.get(AtbashPage.class),
                NAV_TREE.get(AffinePage.class),
                NAV_TREE.get(CaesarPage.class),
                NAV_TREE.get(EnigmaPage.class),
                NAV_TREE.get(PlayfairCipherPage.class),
                NAV_TREE.get(VigenereCipherPage.class)
        );
        classicalGroup.setExpanded(true);

        var symmetricGroup = NavTree.Item.group("Symmetric Encryption", new FontIcon(BootstrapIcons.FILE_EARMARK_LOCK2));
        symmetricGroup.getChildren().setAll(
                NAV_TREE.get(BlockCipherPage.class),
                NAV_TREE.get(BlockCipherFilesPage.class),
                NAV_TREE.get(StreamCipherTextPage.class)
        );

        var hashGroup = NavTree.Item.group("Unkeyed Hash Functions", new FontIcon(BootstrapIcons.FILE_EARMARK_LOCK2));
        hashGroup.getChildren().setAll(
                NAV_TREE.get(AllHashPage.class),
                NAV_TREE.get(HashFilesPage.class)
        );

        var macGroup = NavTree.Item.group("Message Authentication Code", new FontIcon(BootstrapIcons.FILE_EARMARK_LOCK2));
        macGroup.getChildren().setAll(NAV_TREE.get(HmacPage.class));

        var passwordGroup = NavTree.Item.group("Passwords", new FontIcon(BootstrapIcons.FILE_EARMARK_LOCK2));
        passwordGroup.getChildren().setAll(
                NAV_TREE.get(Pbkdf2Page.class),
                NAV_TREE.get(PasswordStrengthPage.class),
                NAV_TREE.get(PwnedPasswordsPage.class)
        );

        var root = NavTree.Item.root();
        root.getChildren().setAll(
                generalGroup,
                classicalGroup,
                symmetricGroup,
                hashGroup,
                macGroup,
                passwordGroup
        );

        return root;
    }

    public static Map<Class<? extends Page>, NavTree.Item> createNavItems() {
        var map = new HashMap<Class<? extends Page>, NavTree.Item>();

        map.put(ThemePage.class, NavTree.Item.page(ThemePage.NAME, ThemePage.class));
        map.put(ADFGVXPage.class, NavTree.Item.page(ADFGVXPage.NAME, ADFGVXPage.class));
        map.put(PlayfairCipherPage.class, NavTree.Item.page(PlayfairCipherPage.NAME, PlayfairCipherPage.class));
        map.put(AtbashPage.class, NavTree.Item.page(AtbashPage.NAME, AtbashPage.class));
        map.put(EnigmaPage.class, NavTree.Item.page(EnigmaPage.NAME, EnigmaPage.class));
        map.put(AffinePage.class, NavTree.Item.page(AffinePage.NAME, AffinePage.class));
        map.put(CaesarPage.class, NavTree.Item.page(CaesarPage.NAME, CaesarPage.class));
        map.put(VigenereCipherPage.class, NavTree.Item.page(VigenereCipherPage.NAME, VigenereCipherPage.class));
        map.put(BlockCipherPage.class, NavTree.Item.page(BlockCipherPage.NAME, BlockCipherPage.class));
        map.put(BlockCipherFilesPage.class, NavTree.Item.page(BlockCipherFilesPage.NAME, BlockCipherFilesPage.class));
        map.put(StreamCipherTextPage.class, NavTree.Item.page(StreamCipherTextPage.NAME, StreamCipherTextPage.class));
        map.put(AllHashPage.class, NavTree.Item.page(AllHashPage.NAME, AllHashPage.class));
        map.put(HashFilesPage.class, NavTree.Item.page(HashFilesPage.NAME, HashFilesPage.class));
        map.put(HmacPage.class, NavTree.Item.page(HmacPage.NAME, HmacPage.class));
        map.put(PasswordStrengthPage.class, NavTree.Item.page(PasswordStrengthPage.NAME, PasswordStrengthPage.class));
        map.put(Pbkdf2Page.class, NavTree.Item.page(Pbkdf2Page.NAME, Pbkdf2Page.class));
        map.put(PwnedPasswordsPage.class, NavTree.Item.page(PwnedPasswordsPage.NAME, PwnedPasswordsPage.class));

        return map;
    }

    public void navigate(Class<? extends Page> page) {
        selectedPage.set(Objects.requireNonNull(page));
    }

}

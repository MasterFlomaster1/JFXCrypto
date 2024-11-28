package dev.masterflomaster1.jfxc.gui.page;

import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import atlantafx.base.util.BBCodeParser;
import dev.masterflomaster1.jfxc.gui.event.BrowseEvent;
import dev.masterflomaster1.jfxc.gui.event.DefaultEventBus;
import dev.masterflomaster1.jfxc.gui.layout.ApplicationWindow;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import java.net.URI;
import java.util.Objects;

public interface Page {

    int MAX_WIDTH = ApplicationWindow.MIN_WIDTH - ApplicationWindow.SIDEBAR_WIDTH;

    String getName();

    Parent getView();

    void reset();

    default Node createFormattedText(String text, boolean handleUrl) {
        var node = BBCodeParser.createFormattedText(text);

        if (handleUrl) {
            node.addEventFilter(ActionEvent.ACTION, e -> {
                if (e.getTarget() instanceof Hyperlink link && link.getUserData() instanceof String url) {
                    if (url.startsWith("https://") || url.startsWith("http://")) {
                        DefaultEventBus.getInstance().publish(new BrowseEvent(URI.create(url)));
                    }
                }
                e.consume();
            });
        }

        return node;
    }

    class PageHeader extends HBox {

        public PageHeader(Page page) {
            super();

            Objects.requireNonNull(page, "page");

            var titleLbl = new Label(page.getName());
            titleLbl.getStyleClass().add(Styles.TITLE_2);

            var menuBtn = new MenuButton(null, new FontIcon(Material2AL.EXPAND_MORE));
            menuBtn.getStyleClass().addAll(Styles.FLAT, Styles.BUTTON_ICON, Tweaks.NO_ARROW);

            getStyleClass().add("header");
            setSpacing(20);
            getChildren().setAll(titleLbl, menuBtn);
        }
    }
}

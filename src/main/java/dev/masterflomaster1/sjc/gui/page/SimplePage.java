package dev.masterflomaster1.sjc.gui.page;

import atlantafx.base.theme.Styles;
import dev.masterflomaster1.sjc.MemCachePage;
import dev.masterflomaster1.sjc.gui.util.NodeUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER;

public abstract class SimplePage extends StackPane implements Page, MemCachePage {

    protected final ScrollPane scrollPane = new ScrollPane();
    protected final VBox userContent = new VBox();
    protected final StackPane userContentArea = new StackPane(userContent);
    protected boolean isRendered = false;

    protected final Label keyLabel = new Label("Key", new FontIcon(BootstrapIcons.KEY_FILL));
    protected final Label counterLabel = new Label("", new FontIcon(BootstrapIcons.FORWARD_FILL));

    protected SimplePage() {
        super();

        userContent.getStyleClass().add("user-content");
        getStyleClass().add("outline-page");

        createPageLayout();
    }

    protected void createPageLayout() {
        userContent.setMinWidth(Page.MAX_WIDTH - 100);
        userContent.setMaxWidth(Page.MAX_WIDTH - 100);

        scrollPane.setContent(userContentArea);
        NodeUtils.setScrollConstraints(scrollPane, AS_NEEDED, true, NEVER, true);
        scrollPane.setMaxHeight(20_000);

        var pageBody = new StackPane();
        pageBody.getChildren().setAll(scrollPane);
        pageBody.getStyleClass().add("body");

        setMinWidth(Page.MAX_WIDTH);
        getChildren().setAll(pageBody);
    }

    protected void addPageHeader() {
        var pageHeader = new PageHeader(this);
        userContent.getChildren().add(pageHeader);
    }

    protected void addNode(Node node) {
        userContent.getChildren().add(node);
    }

    protected void addFormattedText(String text) {
        addFormattedText(text, false);
    }

    protected void addFormattedText(String text, boolean handleUrl) {
        userContent.getChildren().add(createFormattedText(text, handleUrl));
    }

    protected void addSection(String title, Node content) {
        var titleIcon = new FontIcon(Feather.HASH);
        titleIcon.getStyleClass().add("icon-subtle");

        var titleLabel = new Label(title);
        titleLabel.getStyleClass().add(Styles.TITLE_3);
        titleLabel.setGraphic(titleIcon);
        titleLabel.setGraphicTextGap(10);
        titleLabel.setPadding(new Insets(20, 0, 0, 0));

        userContent.getChildren().addAll(titleLabel, content);
    }

    @Override
    public Pane getView() {
        return this;
    }

    @Override
    public void reset() {
        onReset();
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        if (isRendered) {
            return;
        }

        isRendered = true;
        onRendered();
    }

    // Some properties can only be obtained after node placed
    // to the scene graph and here is the place do this.
    protected void onRendered() {
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onReset() {

    }
}

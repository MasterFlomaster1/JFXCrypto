package dev.masterflomaster1.jfxc.gui.layout;

import dev.masterflomaster1.jfxc.gui.page.Page;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Objects;

import static javafx.scene.layout.Priority.ALWAYS;

class MainLayer extends BorderPane {

    static final int PAGE_TRANSITION_DURATION = 250; // ms

    private final MainModel model = new MainModel();
    private final Sidebar sidebar = new Sidebar(model);
    private final StackPane subLayerPane = new StackPane();

    MainLayer() {
        super();

        createView();
        initListeners();

        model.navigate(MainModel.DEFAULT_PAGE);

        // keyboard navigation won't work without focus
        Platform.runLater(sidebar::begForFocus);
    }

    private void createView() {
        sidebar.setMinWidth(ApplicationWindow.SIDEBAR_WIDTH);
        sidebar.setMaxWidth(ApplicationWindow.SIDEBAR_WIDTH);

        HBox.setHgrow(subLayerPane, ALWAYS);

        setId("main");
        setLeft(sidebar);
        setCenter(subLayerPane);
    }

    private void initListeners() {
        model.selectedPageProperty().addListener((obs, old, val) -> {
            if (val != null) {
                loadPage(val);
            }
        });
    }

    private void loadPage(Class<? extends Page> pageClass) {
        try {
            final Page prevPage = (Page) subLayerPane.getChildren().stream()
                .filter(c -> c instanceof Page)
                .findFirst()
                .orElse(null);
            final Page nextPage = pageClass.getDeclaredConstructor().newInstance();

            // startup, no prev page, no animation
            if (getScene() == null) {
                subLayerPane.getChildren().add(nextPage.getView());
                return;
            }

            Objects.requireNonNull(prevPage);

            // reset previous page, e.g. to free resources
            prevPage.reset();

            // animate switching between pages
            subLayerPane.getChildren().add(nextPage.getView());
            subLayerPane.getChildren().remove(prevPage.getView());
            var transition = new FadeTransition(Duration.millis(PAGE_TRANSITION_DURATION), nextPage.getView());
            transition.setFromValue(0.0);
            transition.setToValue(1.0);
            transition.setOnFinished(t -> {
                if (nextPage instanceof Pane nextPane) {
                    nextPane.toFront();
                }
            });
            transition.play();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

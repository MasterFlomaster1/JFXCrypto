package GUI;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.HashMap;

class PageSwitcher extends StackPane {

    private HashMap<String, Node> pages = new HashMap<>();

    PageSwitcher() {
        super();
    }

    private void addPage(String name, Node page) {
        pages.put(name, page);
    }

    void loadPage(String name, String resource) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadPage = fxmlLoader.load();
            Switchable switchable = fxmlLoader.getController();
            switchable.setParentPage(this);
            addPage(name, loadPage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void unloadPage(String name) {
        pages.remove(name);
    }

    void setPage(String name) {
        Node page = pages.get(name);
        if (page != null) {
            final DoubleProperty opacity = opacityProperty();

            if (!getChildren().isEmpty()) {
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(500), t -> {
                            getChildren().remove(0);
                            getChildren().add(0, page);
                            Timeline fadeIn = new Timeline(
                                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                    new KeyFrame(new Duration(500), new KeyValue(opacity, 1.0)));
                            fadeIn.play();
                        }, new KeyValue(opacity, 0.0)));
                fade.play();

            } else {
                setOpacity(0.0);
                getChildren().add(page);
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(500), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
        } else {
            System.out.println("ERROR: page missing!");
        }
    }

}

package GUI;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.HashMap;

class PageSwitcher extends StackPane {

    private HashMap<String, Node> pages = new HashMap<>();

    void setPage(String name) {
        Node page = pages.get(name);
        if (page != null) {
            final DoubleProperty opacity = opacityProperty();

            boolean doAnimations = true;
            if (!getChildren().isEmpty()) {
                if (doAnimations) {
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
                    getChildren().remove(0);
                    getChildren().add(0, page);
                }
            } else {
                if (doAnimations) {
                    setOpacity(0.0);
                    getChildren().add(page);
                    Timeline fadeIn = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                            new KeyFrame(new Duration(500), new KeyValue(opacity, 1.0)));
                    fadeIn.play();
                } else {
                    getChildren().add(page);
                }
            }
        } else {
            System.out.println("ERROR: page missing!");
        }
    }
}

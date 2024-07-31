package dev.masterflomaster1.jfxc.gui.page.viewmodel;

import dev.masterflomaster1.jfxc.gui.page.UIInputPersistence;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

abstract class AbstractViewModel implements UIInputPersistence {

    final StringProperty counterText = new SimpleStringProperty();

    public StringProperty counterTextProperty() {
        return counterText;
    }

}

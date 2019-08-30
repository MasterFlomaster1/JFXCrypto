package GUI;

import javafx.scene.Parent;

enum Pages {

    ABOUT_PAGE (null),
    FILE_ENCRYPTION_PAGE (null),
    FILE_HASH_PAGE (null),
    HASH_SUM_CHECKER_PAGE (null),
    HOME_PAGE (null),
    TEXT_ENCRYPTION_PAGE (null),
    TEXT_HASH_PAGE (null),
    SETTINGS_PAGE(null);

    private Parent parent;

    Pages(Parent parent) {
        this.parent = parent;
    }

    Parent getParent() {
        return parent;
    }

    void setParent(Parent parent) {
        this.parent = parent;
    }

}

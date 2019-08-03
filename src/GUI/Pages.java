package GUI;

public enum Pages {

    ABOUT_PAGE ("AboutPage", "AboutPage.fxml"),
    FILE_ENCRYPTION_PAGE ("FileEncryptionPage", "FileEncryptionPage.fxml"),
    HOME_PAGE ("HomePage", "HomePage.fxml"),
    TEXT_ENCRYPTION_PAGE ("TextEncryptionPage", "TextEncryptionPage.fxml"),
    SETTINGS_PAGE("SettingsPage", "SettingsPage.fxml");

    private String name;
    private String path;

    Pages(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

}

package GUI;

public class SettingsPageController extends BaseController implements Switchable {

    @Override
    public void homePageAction() {
        pageSwitcher.setPage(Pages.HOME_PAGE.getName());
    }

    @Override
    public void textEncryptionPageAction() {
        pageSwitcher.setPage(Pages.TEXT_ENCRYPTION_PAGE.getName());
    }

    @Override
    public void fileEncryptionPageAction() {
        pageSwitcher.setPage(Pages.FILE_ENCRYPTION_PAGE.getName());
    }

    @Override
    public void aboutAction() {
        pageSwitcher.setPage(Pages.ABOUT_PAGE.getName());
    }

    @Override
    public void settingsAction() {

    }

    private PageSwitcher pageSwitcher;

    @Override
    public void setParentPage(PageSwitcher page) {
        pageSwitcher = page;
    }
}

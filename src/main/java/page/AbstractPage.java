package page;

import utils.PendingRequests;

import static com.codeborne.selenide.Selenide.open;

public abstract class AbstractPage {

    private final String BASE_URL = "https://www.autohero.com/";
    private String pageURL;

    protected AbstractPage(final String pageURL) {
        this.pageURL = pageURL;
    }

    public void openPage() {
        open(BASE_URL + pageURL);
    }

    public void waitForPage() {
        PendingRequests.waitForPageToBeLoaded();
    }
}

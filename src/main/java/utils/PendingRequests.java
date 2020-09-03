package utils;

import lombok.extern.slf4j.Slf4j;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.sleep;

@Slf4j
public class PendingRequests {

    private final static int POOLING_TIME = 250;

    public static void waitForPageToBeLoaded() {
        executeScript();
        long pendingRequests = getPendingRequests();
        if (pendingRequests == 0L) {
            do {
                sleep(POOLING_TIME);
                pendingRequests = getPendingRequests();
            } while (pendingRequests > 0L);
        }
    }

    private static long getPendingRequests() {
        String script = "return window.openHTTPs";
        return (long) executeJavaScript(script);
    }

    private static void executeScript() {
        String script = "(function() { " +
                "var oldOpen = XMLHttpRequest.prototype.open;" +
                "window.openHTTPs = 0;" +
                "XMLHttpRequest.prototype.open = function(method, url, async, user, pass) {" +
                "window.openHTTPs++;" +
                "this.addEventListener('readystatechange', function() {" +
                "if(this.readyState == 4) {" +
                "window.openHTTPs--;" +
                "}" +
                "}, false);" +
                "oldOpen.call(this, method, url, async, user, pass);" +
                "}" +
                "})();";
        executeJavaScript(script);
    }
}

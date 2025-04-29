package tn.eduskool.utils;

import javafx.application.Platform;
import javafx.scene.web.WebView;

public class WebViewPool {
    private static WebView instance;

    public static synchronized WebView getInstance() {
        if (instance == null) {
            if (Platform.isFxApplicationThread()) {
                instance = new WebView();
            } else {
                Platform.runLater(() -> instance = new WebView());
                while (instance == null) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
            }
        }
        return instance;
    }

    public static synchronized void releaseInstance() {
        if (instance != null) {
            instance.getEngine().load(null);
            instance = null;
        }
    }
}

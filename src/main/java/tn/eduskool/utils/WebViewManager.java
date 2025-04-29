package tn.eduskool.utils;

import javafx.scene.web.WebView;
import java.util.concurrent.atomic.AtomicReference;

public class WebViewManager {
    private static final AtomicReference<WebView> webViewInstance = new AtomicReference<>();

    public static WebView getWebView() {
        WebView webView = webViewInstance.get();
        if (webView == null) {
            webView = new WebView();
            if (webViewInstance.compareAndSet(null, webView)) {
                return webView;
            }
            return webViewInstance.get();
        }
        return webView;
    }

    public static void cleanup() {
        WebView webView = webViewInstance.get();
        if (webView != null) {
            webView.getEngine().load(null);
            webViewInstance.set(null);
        }
    }
}

package tn.eduskool.utils;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import java.util.function.Consumer;

public class MapDialog {
    public static void show(Consumer<String> onAddressSelected) {
        Stage dialog = new Stage();
        dialog.setTitle("Sélectionner une localisation");

        WebView webView = new WebView();
        webView.setPrefSize(800, 600);
        WebEngine webEngine = webView.getEngine();

        // Debug handlers
        webEngine.setOnError(e -> System.out.println("Erreur JS: " + e.getMessage()));
        webEngine.setOnAlert(e -> System.out.println("Alert JS: " + e.getData()));

        webEngine.getLoadWorker().stateProperty().addListener((obs, old, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("Page chargée avec succès");

                // Injecter le callback JavaScript
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaCallback", new Object() {
                    public void updateAddress(String address) {
                        System.out.println("Adresse reçue: " + address);
                        Platform.runLater(() -> {
                            onAddressSelected.accept(address);
                            dialog.close();
                        });
                    }
                });

                // Vérifier que la carte est chargée
                webEngine.executeScript(
                        "console.log('État de la carte: ' + (typeof mapboxgl !== 'undefined' ? 'OK' : 'Non chargé'));");
            }
        });

        String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="utf-8">
                    <title>Sélectionner une localisation</title>
                    <script src='https://api.mapbox.com/mapbox-gl-js/v2.14.1/mapbox-gl.js'></script>
                    <link href='https://api.mapbox.com/mapbox-gl-js/v2.14.1/mapbox-gl.css' rel='stylesheet' />
                    <style>
                        html, body { margin: 0; padding: 0; height: 100%%; }
                        #map { width: 100%%; height: 100%%; }
                    </style>
                </head>
                <body>
                    <div id="map"></div>
                    <script>
                        mapboxgl.accessToken = '%s';
                        const map = new mapboxgl.Map({
                            container: 'map',
                            style: 'mapbox://styles/mapbox/streets-v12',
                            center: [10.1815, 36.8065],
                            zoom: 13
                        });

                        let marker;
                        map.on('click', async (e) => {
                            const {lng, lat} = e.lngLat;
                            if (marker) marker.remove();
                            marker = new mapboxgl.Marker()
                                .setLngLat([lng, lat])
                                .addTo(map);

                            try {
                                const response = await fetch(
                                    `https://api.mapbox.com/geocoding/v5/mapbox.places/${lng},${lat}.json?access_token=${mapboxgl.accessToken}`
                                );
                                const data = await response.json();
                                if (data.features && data.features.length > 0) {
                                    const address = data.features[0].place_name;
                                    if (window.javaCallback) {
                                        window.javaCallback.updateAddress(address);
                                    }
                                }
                            } catch(error) {
                                console.error('Erreur:', error);
                            }
                        });
                    </script>
                </body>
                </html>
                """
                .formatted(MapboxConfig.getApiKey());

        webEngine.loadContent(htmlContent);

        Scene scene = new Scene(webView);
        dialog.setScene(scene);
        dialog.show();
    }
}

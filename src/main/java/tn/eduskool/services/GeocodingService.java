package tn.eduskool.services;

import org.json.JSONArray;
import org.json.JSONObject;
import tn.eduskool.utils.MapboxConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GeocodingService {
    private final HttpClient httpClient;

    public GeocodingService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public String reverseGeocode(double latitude, double longitude) throws IOException, InterruptedException {
        String url = String.format("%s%f,%f.json?access_token=%s&language=fr",
                MapboxConfig.getBaseUrl(),
                longitude,
                latitude,
                MapboxConfig.getApiKey());

        System.out.println("Requête envoyée à l'API Mapbox : " + url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Réponse de l'API Mapbox : " + response.body());

        if (response.statusCode() == 200) {
            JSONObject json = new JSONObject(response.body());
            JSONArray features = json.getJSONArray("features");

            if (features.length() > 0) {
                String placeName = features.getJSONObject(0).getString("place_name");
                System.out.println("Adresse trouvée : " + placeName);
                return placeName;
            } else {
                System.out.println("Aucune adresse trouvée pour les coordonnées fournies.");
            }
        } else {
            System.err.println("Erreur lors de l'appel à l'API Mapbox : " + response.statusCode());
        }

        return null;
    }
}

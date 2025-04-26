package tn.eduskool.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
//import org.codehaus.jettison.json.JSONArray;
//import org.codehaus.jettison.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.JSONObject;
import org.json.JSONArray;

public class HelpController {

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField userInput;

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent"; // L'URL
                                                                                                                                      // de
                                                                                                                                      // l'API
                                                                                                                                      // réelle

    public class DisableSSLVerification {

        public static void disableSSLVerification() {
            try {
                TrustManager[] trustAllCertificates = new TrustManager[] {
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }

                            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            }

                            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            }
                        }
                };

                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCertificates, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void sendMessage(ActionEvent event) {
        // Désactiver la vérification SSL avant d'effectuer la requête HTTP
        DisableSSLVerification.disableSSLVerification();

        String input = userInput.getText();
        chatArea.appendText("Vous: " + input + "\n");

        String response = getChatbotResponse(input);
        chatArea.appendText("Bot: " + response + "\n");

        userInput.clear();
    }

    private String getChatbotResponse(String input) {
        try {
            String apiKey = "AIzaSyBKCXsC9wz65khEFaeXglCS-0zw2IBOHpE";
            URL url = new URL(API_URL + "?key=" + apiKey);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Corps de la requête JSON (ajustez la structure selon les spécifications de
            // l'API)
            String jsonInputString = "{\n" +
                    "  \"contents\": [{\n" +
                    "    \"parts\": [{\"text\": \"" + input + "\"}]\n" +
                    "  }]\n" +
                    "}";

            // Envoie la requête POST
            try (OutputStream os = connection.getOutputStream()) {
                byte[] inputBytes = jsonInputString.getBytes("utf-8");
                os.write(inputBytes, 0, inputBytes.length);
            }

            // Vérifie la réponse
            int statusCode = connection.getResponseCode();
            System.out.println("Response Code: " + statusCode);

            // Si la réponse est OK (200), on lit le résultat
            StringBuilder response = new StringBuilder();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray candidates = jsonResponse.getJSONArray("candidates");
                String botMessage = candidates.getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text");

                return botMessage;
            } else {
                return "Erreur: " + statusCode;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur dans la récupération de la réponse.";
        }
    }

}

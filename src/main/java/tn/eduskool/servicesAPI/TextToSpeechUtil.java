package tn.eduskool.servicesAPI;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TextToSpeechUtil {

    /**
     * Convertit un texte en audio via l'API Voice RSS et enregistre le résultat en
     * MP3.
     * 
     * @param contenu   Le texte à convertir.
     * @param commentId L'ID du commentaire (pour nommer le fichier).
     */
    public static void generateAudio(String contenu, int commentId) {
        String apiKey = "b73c93be238a48e09983ae50537f86ae";
        String lang = "fr-fr";
        try {
            String text = URLEncoder.encode(contenu, "UTF-8");
            // Ajoutez un paramètre 'fmt' si l'API supporte d'autres formats, par exemple:
            // fmt=ogg
            String urlStr = "https://api.voicerss.org/?key=" + apiKey + "&hl=" + lang + "&src=" + text;
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Vérifie le code de réponse HTTP
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(
                        "Erreur lors de l'appel à l'API Voice RSS : code " + connection.getResponseCode());
            }

            InputStream in = connection.getInputStream();
            // Changez l'extension pour utiliser .ogg au lieu de .mp3
            File outputFile = new File("uploads/comment_" + commentId + ".ogg");
            outputFile.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(outputFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            out.close();
            in.close();
            System.out.println("Audio généré et sauvegardé dans " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

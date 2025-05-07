package tn.eduskool.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.chart.XYChart.Data;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class StatistiqueDevoirsController implements Initializable {

    @FXML
    private BarChart<String, Number> barChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chargerStatistiques();
    }

    private void chargerStatistiques() {
        String url = "jdbc:mysql://localhost:3306/eduskool"; // Lien vers ta base de données
        String user = "root"; // Utilisateur
        String password = ""; // Mot de passe

        String query = "SELECT u.nom, u.prenom, COUNT(d.id) AS nombre_devoirs " +
                "FROM devoirs d " +
                "JOIN utilisateur u ON d.idEnseignant = u.id_utilisateur " +
                "GROUP BY d.idEnseignant";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Étape 1 : Stocker temporairement les résultats
            java.util.List<XYChart.Data<String, Number>> dataList = new java.util.ArrayList<>();

            while (rs.next()) {
                String enseignant = rs.getString("nom") + " " + rs.getString("prenom");
                int nombreDevoirs = rs.getInt("nombre_devoirs");

                XYChart.Data<String, Number> data = new XYChart.Data<>(enseignant, nombreDevoirs);
                dataList.add(data);
            }

            // Étape 2 : Trier la liste par nombre de devoirs décroissant
            dataList.sort((d1, d2) -> Integer.compare(d2.getYValue().intValue(), d1.getYValue().intValue()));

            // Étape 3 : Créer la série et ajouter les données triées
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Nombre de devoirs par enseignant");

            for (XYChart.Data<String, Number> data : dataList) {
                series.getData().add(data);

                // Gérer la couleur et l'affichage du texte au clic
                data.nodeProperty().addListener((observable, oldNode, newNode) -> {
                    if (newNode != null) {
                        newNode.setStyle("-fx-bar-fill: " + getRandomColor());

                        newNode.setOnMouseClicked(event -> {
                            Text text = new Text(String.valueOf(data.getYValue()));
                            text.setTranslateY(-10);  // Positionner le label au-dessus de la barre

                            text.setTranslateX(newNode.getLayoutX() + (newNode.getBoundsInLocal().getWidth() / 2) - (text.getBoundsInLocal().getWidth() / 2));

                            barChart.getChildrenUnmodifiable().add(text);
                        });
                    }
                });
            }

            barChart.getData().clear(); // Vider avant d'ajouter
            barChart.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private String getRandomColor() {
        String[] colors = {"#FF5733", "#33FF57", "#3357FF", "#FF33A1", "#FF8C33", "#33FFF6"};
        int index = (int) (Math.random() * colors.length);
        return colors[index];
    }
}

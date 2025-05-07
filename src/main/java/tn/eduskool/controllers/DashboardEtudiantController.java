package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import tn.eduskool.entities.Utilisateur;

import java.io.IOException;
import java.net.URL;  // Ajout de l'import pour URL
import java.util.ResourceBundle;  // Ajout de l'import pour ResourceBundle

public class DashboardEtudiantController implements BaseController, javafx.fxml.Initializable {

    private Utilisateur utilisateur;

    @FXML
    private Label titleLabel;

    @FXML
    private StackPane mainContent;

    @FXML
    private Button soumissionDevoirButton; // Ajout du bouton pour la soumission de devoir

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Ajouter un événement au clic sur le bouton SoumissionsDevoir
        soumissionDevoirButton.setOnAction(event -> afficherSoumissionDevoirsView());
    }

    @Override
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        if (titleLabel != null && utilisateur != null) {
            titleLabel.setText("Bonjour ID: " + utilisateur.getIdUtilisateur());
        }
    }

    @Override
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    private void afficherSoumissionDevoirsView() {
        try {
            // Charger le fichier FXML de la vue SoumissionDevoirs
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SoumissionDevoirsView.fxml"));
            Parent root = loader.load();

            // Passer l'utilisateur actuel au contrôleur si nécessaire
            SoumissionDevoirsController controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setUtilisateur(this.utilisateur);
            }

            // Configurer le layout pour remplir tout l'espace disponible
            StackPane.setMargin(root, new Insets(0));

            // Assurer que le nœud remplit l'espace parent
            if (root instanceof Region) {
                Region region = (Region) root;
                region.setPrefWidth(Region.USE_COMPUTED_SIZE);
                region.setPrefHeight(Region.USE_COMPUTED_SIZE);
                region.setMaxWidth(Double.MAX_VALUE);
                region.setMaxHeight(Double.MAX_VALUE);
            }

            // Remplacer le contenu de la StackPane avec la nouvelle vue
            mainContent.getChildren().setAll(root);

        } catch (IOException e) {
            e.printStackTrace();
            // Gérer les erreurs de chargement de la vue
        }
    }
}

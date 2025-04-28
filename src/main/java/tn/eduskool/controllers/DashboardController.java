package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import tn.eduskool.entities.Utilisateur;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable ,BaseController {
    private Utilisateur utilisateur;

    @FXML
    private Label titleLabel;


    @FXML
    private StackPane mainContent;

    @FXML
    private Button devoirsBtn;  // Référence au bouton Devoirs

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation vide pour le moment
        // Nous ajouterons le code nécessaire après la phase de test
    }

    /**
     * Gère le clic sur le bouton Devoirs
     * Charge la vue ListeDevoirs.fxml dans la zone principale
     */
    @FXML
    private void handleDevoirsClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeDevoirs.fxml"));
            Parent view = loader.load();
            
            // Appliquer les styles
            view.getStyleClass().add("content-view");
            
            // Configuration du contrôleur
            ListeDevoirsController controller = loader.getController();
            if (controller != null) {
                controller.setUtilisateur(this.utilisateur);
                controller.chargerDevoirs();
            }

            // Mise à jour de l'affichage
            mainContent.getChildren().clear();
            mainContent.getChildren().add(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de la vue des devoirs");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
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
}
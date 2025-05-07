package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import tn.eduskool.entities.Utilisateur;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, BaseController {
    private Utilisateur utilisateur;

    @FXML
    private AnchorPane contentPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation si nécessaire (par exemple, charger une vue par défaut)
    }

    @FXML
    private void showHome() {
        System.out.println("Bouton Home cliqué");
        // Logique ou redirection vers la page d'accueil
    }

    @FXML
    private void showThemes() {
        System.out.println("Bouton Themes cliqué");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CalendrierEtudiant.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Fichier FXML /CalendrierEtudiant.fxml introuvable");
            }
            VBox coursView = loader.load();
            contentPane.getChildren().setAll(coursView);
            AnchorPane.setTopAnchor(coursView, 0.0);
            AnchorPane.setBottomAnchor(coursView, 0.0);
            AnchorPane.setLeftAnchor(coursView, 0.0);
            AnchorPane.setRightAnchor(coursView, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de CalendrierEtudiant.fxml : " + e.getMessage());
        }
    }

    @FXML
    private void showCours() {
        System.out.println("Bouton Cours cliqué");
        try {
            // Remplacement de FrontCours.fxml par Cours.fxml pour correspondre aux fichiers précédents
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontCours.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Fichier FXML /FrontCours.fxml introuvable");
            }
            VBox coursView = loader.load();
            contentPane.getChildren().setAll(coursView);
            AnchorPane.setTopAnchor(coursView, 0.0);
            AnchorPane.setBottomAnchor(coursView, 0.0);
            AnchorPane.setLeftAnchor(coursView, 0.0);
            AnchorPane.setRightAnchor(coursView, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de FrontCours.fxml : " + e.getMessage());
        }
    }

    @FXML
    private void showActivite() {
        System.out.println("Bouton Activités cliqué");
        // TODO: Implémenter la logique pour afficher les activités
        showAlert("Information", "Section Activités en cours de développement.");
    }

    @FXML
    private void showDevoirs() {
        System.out.println("Bouton Devoirs cliqué");
        // TODO: Implémenter la logique pour afficher les devoirs
        showAlert("Information", "Section Devoirs en cours de développement.");
    }

    @FXML
    private void showAbonnement() {
        System.out.println("Bouton Abonnement cliqué");
        // TODO: Implémenter la logique pour afficher la section abonnement
        showAlert("Information", "Section Abonnement en cours de développement.");
    }

    @FXML
    private void showAide() {
        System.out.println("Bouton Aide cliqué");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/help.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Fichier FXML /help.fxml introuvable");
            }
            VBox helpView = loader.load();
            contentPane.getChildren().setAll(helpView);
            AnchorPane.setTopAnchor(helpView, 0.0);
            AnchorPane.setBottomAnchor(helpView, 0.0);
            AnchorPane.setLeftAnchor(helpView, 0.0);
            AnchorPane.setRightAnchor(helpView, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de help.fxml : " + e.getMessage());
        }
    }

    @FXML
    private void handleButtonHover(MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-background-color: #FF69B4; -fx-text-fill: white; -fx-font-size: 18; -fx-padding: 10 20; -fx-background-radius: 12; -fx-font-family: 'Comic Sans MS';");
    }

    @FXML
    private void handleButtonExit(MouseEvent event) {
        Button btn = (Button) event.getSource();
        btn.setStyle("-fx-background-color: #FFC0CB; -fx-text-fill: white; -fx-font-size: 18; -fx-padding: 10 20; -fx-background-radius: 12; -fx-font-family: 'Comic Sans MS';");
    }

    @Override
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
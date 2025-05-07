package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import tn.eduskool.entities.Utilisateur;

import java.io.IOException;

public class  FrontEnseignantController implements BaseController {
    private Utilisateur utilisateur;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private void showHome() {
        System.out.println("Bouton Home cliqué");

        // Logique ou redirection vers la page d'accueil
    }

    @FXML
    private void showThemes() {
        System.out.println("Bouton Themes cliqué");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CalendrierCours.fxml"));
            VBox coursView = loader.load(); // Changé de AnchorPane à VBox
            contentPane.getChildren().setAll(coursView);
            AnchorPane.setTopAnchor(coursView, 0.0);
            AnchorPane.setBottomAnchor(coursView, 0.0);
            AnchorPane.setLeftAnchor(coursView, 0.0);
            AnchorPane.setRightAnchor(coursView, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de FrontCours.fxml");
        }
    }
    // Logique ou redirection vers les thèmes


    @FXML
    private void showCours() {
        System.out.println("Bouton Cours cliqué");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCoursEnseignant.fxml"));
            VBox coursView = loader.load(); // Changé de AnchorPane à VBox
            contentPane.getChildren().setAll(coursView);
            AnchorPane.setTopAnchor(coursView, 0.0);
            AnchorPane.setBottomAnchor(coursView, 0.0);
            AnchorPane.setLeftAnchor(coursView, 0.0);
            AnchorPane.setRightAnchor(coursView, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de FrontCours.fxml");
        }
    }

    @FXML
    private void showActivite() {
        System.out.println("Bouton Activities cliqué");
        // Logique ou redirection vers les activités
    }

    @FXML
    private void showDevoirs() {
        System.out.println("Bouton Devoirs ou Abonnement cliqué");
        // Logique ou redirection vers les devoirs
    }

    @FXML
    private void showAbonnement() {
        System.out.println("Bouton Psychologique cliqué");
        // Logique ou redirection vers la section Psychologique
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

    }

    @Override
    public Utilisateur getUtilisateur() {
        return null;
    }
}
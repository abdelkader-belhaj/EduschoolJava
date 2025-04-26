// MainController.java
package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import tn.eduskool.entities.Utilisateur;

public class FrontController implements Initializable, BaseController {

    @FXML
    private StackPane contentArea;

    private Utilisateur utilisateur;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        navigateToHome();
    }

    @FXML
    private void navigateToHome() {
        loadPage("/home_view.fxml");
    }

    @FXML
    private void navigateToAbout() {
        loadPage("/about_view.fxml");
    }

    @FXML
    private void navigateToActivities() {
        loadPage("/ActivityGridVIew.fxml");
    }

    private void loadPage(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent page = loader.load();

            // S'assurer que la page chargée hérite des styles de la scène parente
            Scene currentScene = contentArea.getScene();
            if (currentScene != null && !page.getStylesheets().equals(currentScene.getStylesheets())) {
                page.getStylesheets().addAll(currentScene.getStylesheets());
            }

            contentArea.getChildren().clear();
            contentArea.getChildren().add(page);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading page: " + fxml);
        }
    }

    @Override
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
}
// MainController.java
package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import javafx.scene.control.Alert;
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

    @Override
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        if (utilisateur != null) {
            System.out.println("Type utilisateur dans FrontController: " + utilisateur.getType_Utilisateur());
        }
    }

    @Override
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    @FXML
    private void navigateToDevoirs() {
        String fxmlFile;
        System.out.println("Navigate to Devoirs - User Type: " + (utilisateur != null ? utilisateur.getType_Utilisateur() : "null")); // Debug log

        if (utilisateur == null || utilisateur.getType_Utilisateur() == null || utilisateur.getType_Utilisateur().isEmpty()) {
            showError("Erreur", "Type d'utilisateur invalide");
            return;
        }

        switch (utilisateur.getType_Utilisateur().toLowerCase()) {
            case "etudiant":
                fxmlFile = "/ListeDevoirsEtudiant.fxml";
                break;
            case "enseiagnt":
                fxmlFile = "/ListeDevoirs.fxml";
                break;
            default:
                showError("Erreur", "Type d'utilisateur non reconnu: " + utilisateur.getType_Utilisateur());
                return;
        }
        
        loadPage(fxmlFile);
    }

    @FXML
    void handleLogout(ActionEvent event) {
        try {
            // Load the login view
            Parent root = FXMLLoader.load(getClass().getResource("/login_view.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("EduSkool - Connexion");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur", "Impossible de charger l'écran de connexion.");
        }
    }

    private void loadPage(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent page = loader.load();
            
            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setUtilisateur(utilisateur);
                System.out.println("Utilisateur transmis au controller: " + fxml); // Debug log
            }
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(page);

        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur", "Impossible de charger la page: " + fxml);
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
package tn.eduskool.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.eduskool.entities.Utilisateur;
import tn.eduskool.entities.Utilisateur.TypeUtilisateur;
import tn.eduskool.services.*;
import tn.eduskool.tools.DatabaseConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class LoginController implements Initializable, BaseController {
    private Utilisateur utilisateur;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    private LoginService loginService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialiser le service de login avec la connexion à la base de données
        Connection connection = DatabaseConnection.connect();
        loginService = new LoginService(connection);

        // Configurer les options du ComboBox
        roleComboBox.setItems(FXCollections.observableArrayList(
                "Étudiant", "Enseignant", "Administrateur"));

        // Sélectionner "Étudiant" par défaut
        roleComboBox.getSelectionModel().selectFirst();

        // Effacer le message d'erreur quand l'utilisateur commence à taper
        emailField.textProperty().addListener((observable, oldValue, newValue) -> errorLabel.setText(""));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> errorLabel.setText(""));
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String selectedRole = roleComboBox.getValue();

        if (email.isEmpty() || password.isEmpty() || selectedRole == null) {
            errorLabel.setText("Veuillez remplir tous les champs!");
            return;
        }

        // Convertir le rôle sélectionné au format TypeUtilisateur
        TypeUtilisateur role;
        switch (selectedRole) {
            case "Administrateur":
                role = TypeUtilisateur.ADMIN;
                break;
            case "Enseignant":
                role = TypeUtilisateur.ENSEIGNANT;
                break;
            case "Étudiant":
                role = TypeUtilisateur.ETUDIANT;
                break;
            default:
                errorLabel.setText("Rôle invalide");
                return;
        }

        try {
            // Appeler le service de login pour vérifier les identifiants
            Utilisateur utilisateur = loginService.login(email, password, role);

            if (utilisateur != null) {
                // Authentification réussie, ouvrir l'écran correspondant au rôle
                ouvrirEcranSelonRole(utilisateur);
            } else {
                // Authentification échouée
                errorLabel.setText("Email ou mot de passe incorrect, ou vous n'avez pas le rôle sélectionné!");
            }
        } catch (Exception e) {
            errorLabel.setText("Erreur lors de la connexion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void ouvrirEcranSelonRole(Utilisateur utilisateur) {
        try {
            String fxmlFile;
            String title;

            // Déterminer quel écran ouvrir selon le rôle
            switch (utilisateur.getTypeUtilisateur()) {
                case ADMIN:
                    fxmlFile = "/dashbord_view.fxml";
                    title = "Dashboard Admin";
                    break;
                case ENSEIGNANT:
                case ETUDIANT:
                    fxmlFile = "/front_view.fxml";
                    title = "EduSkool - Accueil";
                    break;
                default:
                    errorLabel.setText("Type d'utilisateur non pris en charge");
                    return;
            }

            // Charger le fichier FXML approprié
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Maintenir les styles CSS
            scene.getStylesheets().addAll(
                    getClass().getResource("/activities.css").toExternalForm(),
                    getClass().getResource("/front.css").toExternalForm(),
                    getClass().getResource("/dashboard.css").toExternalForm(),
                    getClass().getResource("/style.css").toExternalForm());

            // Récupérer le contrôleur et définir l'utilisateur
            BaseController controller = loader.getController();
            controller.setUtilisateur(utilisateur);

            // Configurer et afficher la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("EduSkool - " + utilisateur.getRole());
            stage.setMaximized(true);
            stage.show();

            // Fermer la fenêtre de connexion
            ((Stage) loginButton.getScene().getWindow()).close();

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la vue principale: " + e.getMessage());
            e.printStackTrace();
            showError("Erreur lors du chargement de l'application.");
        }
    }

    @FXML
    void handleRegister(ActionEvent event) {
        try {
            // Charger l'écran d'inscription
            Parent root = FXMLLoader.load(getClass().getResource("/inscription_vienw.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("EduSkool - Inscription");
            stage.show();
        } catch (IOException e) {
            errorLabel.setText("Erreur lors du chargement de l'écran d'inscription: " + e.getMessage());
            e.printStackTrace();
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

    private void showError(String message) {
        errorLabel.setText(message);
    }
}
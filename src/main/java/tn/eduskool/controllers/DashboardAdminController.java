package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import tn.eduskool.entities.Utilisateur;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardAdminController implements Initializable, BaseController {

    private Utilisateur utilisateur;

    @FXML
    private Label messageLabel;

    @FXML
    private StackPane mainContent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation si nécessaire
    }

    @Override
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        if (messageLabel != null && utilisateur != null) {
            messageLabel.setText("Bienvenue, Admin ID: " + utilisateur.getIdUtilisateur());
        }
    }

    @Override
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
    @FXML
    private void handleStatsClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StatistiqueDevoirs.fxml"));
            Parent view = loader.load();

            // Si le contrôleur a besoin de l'utilisateur, on peut faire comme ceci :
            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setUtilisateur(this.utilisateur);
            }

            mainContent.getChildren().clear();
            mainContent.getChildren().add(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    @FXML
    private void handleSoumissionsClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeSoumissions.fxml"));
            Parent view = loader.load();

            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setUtilisateur(this.utilisateur);
            }

            mainContent.getChildren().clear();
            mainContent.getChildren().add(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

        } catch (IOException e) {
            e.printStackTrace();

        }

    }
    @FXML
    private void handleDevoirsClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeDevoirs.fxml"));
            Parent view = loader.load();

            // Passe l'utilisateur connecté au contrôleur si nécessaire
            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setUtilisateur(this.utilisateur);
            }

            mainContent.getChildren().clear();
            mainContent.getChildren().add(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}

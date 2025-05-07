package tn.eduskool.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import tn.eduskool.entities.Utilisateur;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable, BaseController {

    @FXML
    private VBox activitiesView;
    @FXML
    private Button btnActivities;
    @FXML
    private Button btnComments;
    @FXML
    private Button btnCourses;
    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnLogout;
    @FXML
    private Button btnReports;
    @FXML
    private Button btnSettings;
    @FXML
    private Button btnUsers;
    // Nous supprimons la référence à btnCreate qui n'est pas dans le FXML
    @FXML
    private Button minimizeButton;
    @FXML
    private Button maximizeButton;
    @FXML
    private Button closeButton;
    @FXML
    private VBox commentsView;
    @FXML
    private StackPane contentArea;
    @FXML
    private VBox coursesView;
    @FXML
    private VBox dashboardView;
    @FXML
    private Label lblPageTitle;
    @FXML
    private VBox reportsView;
    @FXML
    private VBox settingsView;
    @FXML
    private VBox usersView;

    // Variables pour le déplacement de la fenêtre
    private double xOffset = 0;
    private double yOffset = 0;

    // User instance
    private Utilisateur utilisateur;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialisation du contrôleur Dashboard...");

        // Vérifier que tous les composants sont correctement chargés
        checkComponents();

        // Configuration des contrôles de fenêtre
        setupWindowControls();

        // Configuration des boutons de navigation
        setupNavigationButtons();

        // Afficher le tableau de bord par défaut
        showDashboardView();
    }

    /**
     * Configure les boutons de la barre de titre
     */
    private void setupWindowControls() {
        if (minimizeButton != null) {
            minimizeButton.setOnAction(event -> {
                System.out.println("Minimizing window");
                Stage stage = (Stage) minimizeButton.getScene().getWindow();
                stage.setIconified(true);
            });
        } else {
            System.out.println("Warning: minimizeButton is null");
        }

        if (maximizeButton != null) {
            maximizeButton.setOnAction(event -> {
                System.out.println("Maximizing/restoring window");
                Stage stage = (Stage) maximizeButton.getScene().getWindow();
                stage.setMaximized(!stage.isMaximized());
            });
        } else {
            System.out.println("Warning: maximizeButton is null");
        }

        if (closeButton != null) {
            closeButton.setOnAction(event -> {
                System.out.println("Closing window");
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.close();
            });
        } else {
            System.out.println("Warning: closeButton is null");
        }
    }

    /**
     * Configure les boutons de navigation
     */
    private void setupNavigationButtons() {
        // Vérification des boutons avant d'assigner les actions
        if (btnDashboard != null) {
            btnDashboard.setOnAction(this::showDashboard);
        } else {
            System.out.println("Warning: btnDashboard is null");
        }

        if (btnUsers != null) {
            btnUsers.setOnAction(this::showUsers);
        } else {
            System.out.println("Warning: btnUsers is null");
        }

        if (btnActivities != null) {
            btnActivities.setOnAction(this::showActivities);
        } else {
            System.out.println("Warning: btnActivities is null");
        }

        if (btnComments != null) {
            btnComments.setOnAction(this::showComments);
        } else {
            System.out.println("Warning: btnComments is null");
        }

        if (btnCourses != null) {
            btnCourses.setOnAction(this::showCourses);
        } else {
            System.out.println("Warning: btnCourses is null");
        }

        if (btnReports != null) {
            btnReports.setOnAction(this::showReports);
        } else {
            System.out.println("Warning: btnReports is null");
        }

        if (btnSettings != null) {
            btnSettings.setOnAction(this::showSettings);
        } else {
            System.out.println("Warning: btnSettings is null");
        }

        if (btnLogout != null) {
            btnLogout.setOnAction(this::handleLogout);
        } else {
            System.out.println("Warning: btnLogout is null");
        }

        // Nous ne référençons plus btnCreate ici
    }

    /**
     * Vérifie que tous les composants sont correctement initialisés
     */
    private void checkComponents() {
        System.out.println("Vérification des composants:");
        System.out.println("dashboardView: " + (dashboardView != null ? "OK" : "NULL"));
        System.out.println("usersView: " + (usersView != null ? "OK" : "NULL"));
        System.out.println("activitiesView: " + (activitiesView != null ? "OK" : "NULL"));
        System.out.println("commentsView: " + (commentsView != null ? "OK" : "NULL"));
        System.out.println("coursesView: " + (coursesView != null ? "OK" : "NULL"));
        System.out.println("reportsView: " + (reportsView != null ? "OK" : "NULL"));
        System.out.println("settingsView: " + (settingsView != null ? "OK" : "NULL"));
        System.out.println("contentArea: " + (contentArea != null ? "OK" : "NULL"));
        System.out.println("lblPageTitle: " + (lblPageTitle != null ? "OK" : "NULL"));
    }

    /**
     * Cache toutes les vues
     */
    private void hideAllViews() {
        if (dashboardView != null)
            dashboardView.setVisible(false);
        if (usersView != null)
            usersView.setVisible(false);
        if (activitiesView != null)
            activitiesView.setVisible(false);
        if (commentsView != null)
            commentsView.setVisible(false);
        if (coursesView != null)
            coursesView.setVisible(false);
        if (reportsView != null)
            reportsView.setVisible(false);
        if (settingsView != null)
            settingsView.setVisible(false);
    }

    /**
     * Affiche la vue du tableau de bord
     */
    private void showDashboardView() {
        hideAllViews();
        if (dashboardView != null) {
            dashboardView.setVisible(true);
            if (lblPageTitle != null)
                lblPageTitle.setText("Dashboard");
        }
    }

    /**
     * Configure la scène pour permettre le déplacement de la fenêtre
     */
    public void setupDraggableStage(HBox titleBar) {
        if (titleBar == null)
            return;

        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    @FXML
    void handleLogout(ActionEvent event) {
        System.out.println("Logout button clicked");
        // Implémentation de la déconnexion
    }

    @FXML
    void showActivities(ActionEvent event) {
        System.out.println("Showing Activities view");
        hideAllViews();
        if (activitiesView != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ActivityGridVIew.fxml"));
                Parent gridView = loader.load();
                activitiesView.getChildren().setAll(gridView);
                activitiesView.setVisible(true);
                if (lblPageTitle != null)
                    lblPageTitle.setText("Our Activities");
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error loading activities grid view: " + e.getMessage());
            }
        }
    }

    @FXML
    void showComments(ActionEvent event) {
        System.out.println("Showing Comments view");
        hideAllViews();
        if (commentsView != null) {
            commentsView.setVisible(true);
            if (lblPageTitle != null)
                lblPageTitle.setText("Comments Management");
        }
    }

    @FXML
    void showCourses(ActionEvent event) {
        System.out.println("Showing Courses view");
        hideAllViews();
        if (coursesView != null) {
            coursesView.setVisible(true);
            if (lblPageTitle != null)
                lblPageTitle.setText("Courses Management");
        }
    }

    @FXML
    void showDashboard(ActionEvent event) {
        System.out.println("Showing Dashboard view");
        showDashboardView();
    }

    @FXML
    void showDevoirs(ActionEvent event) {
        System.out.println("Showing Devoirs view");
        hideAllViews();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeDevoirs.fxml"));
            Parent devoirsView = loader.load();

            ListeDevoirsController controller = loader.getController();
            controller.setUtilisateur(this.utilisateur);

            if (contentArea != null) {
                contentArea.getChildren().setAll(devoirsView);
                if (lblPageTitle != null) {
                    lblPageTitle.setText("Liste des devoirs");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading devoirs view: " + e.getMessage());
        }
    }

    @FXML
    void showReports(ActionEvent event) {
        System.out.println("Showing Reports view");
        hideAllViews();
        if (reportsView != null) {
            reportsView.setVisible(true);
            if (lblPageTitle != null)
                lblPageTitle.setText("Reports");
        }
    }

    @FXML
    void showSettings(ActionEvent event) {
        System.out.println("Showing Settings view");
        hideAllViews();
        if (settingsView != null) {
            settingsView.setVisible(true);
            if (lblPageTitle != null)
                lblPageTitle.setText("Settings");
        }
    }

    @FXML
    void showUsers(ActionEvent event) {
        System.out.println("Showing Users view");
        hideAllViews();
        if (usersView != null) {
            usersView.setVisible(true);
            if (lblPageTitle != null)
                lblPageTitle.setText("Users Management");
        }
    }

    public void setupStage(Stage stage) {
        // Taille fixe (par exemple 1000x700)
        double width = 1000;
        double height = 700;

        stage.setWidth(width);
        stage.setHeight(height);

        // Taille minimale pour ne pas permettre trop petit
        stage.setMinWidth(900);
        stage.setMinHeight(600);

        // Centrage de la fenêtre sur l'écran
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);
    }

    @Override
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        // Mettre à jour l'interface si nécessaire avec les informations de
        // l'utilisateur
    }
}
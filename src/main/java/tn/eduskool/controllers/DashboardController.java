package tn.eduskool.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.Parent;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import tn.eduskool.entities.Activity;
import tn.eduskool.entities.Utilisateur;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import tn.eduskool.entities.Activity;
import tn.eduskool.services.ServiceActivity;

import java.util.List;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.fxml.Initializable;
import tn.eduskool.entities.Utilisateur;

import java.net.URL;
import java.util.ResourceBundle;

@SuppressWarnings("unused")
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
    private Button btnCreate;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnListen;

    @FXML
    private Button btnLoad;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnUsers;

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
    private HBox starBox;

    @FXML
    private TextField tfActivityId;

    @FXML
    private TextArea tfContenu;

    @FXML
    private TextField tfId;

    @FXML
    private VBox usersView;

    // Variables pour le déplacement de la fenêtre
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private TableView<Activity> activityTableView;

    @FXML
    private TableColumn<Activity, Integer> colId;

    @FXML
    private TableColumn<Activity, String> colTitre;

    @FXML
    private TableColumn<Activity, String> colDescription;

    @FXML
    private TableColumn<Activity, String> colDate;

    @FXML
    private TableColumn<Activity, Boolean> colIsApproved;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialisation du contrôleur...");

        // Configuration des contrôles de fenêtre
        setupWindowControls();

        // Configuration des boutons de navigation
        setupNavigationButtons();

        // Afficher le tableau de bord par défaut
        showDashboardView();

        // Debug: vérifier que tous les composants sont correctement chargés
        checkComponents();

        // Debug: ajouter des bordures de couleur pour visualiser les composants
        addDebugBorders();

        // Configure les colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        colIsApproved.setCellValueFactory(new PropertyValueFactory<>("approved"));
        colIsApproved.setCellFactory(column -> new TableCell<Activity, Boolean>() {
            private final Button btn = new Button();

            @Override
            protected void updateItem(Boolean approved, boolean empty) {
                super.updateItem(approved, empty);

                if (empty || approved == null) {
                    setGraphic(null);
                } else {
                    btn.setText(approved ? "Désapprouver" : "Approuver");
                    btn.setStyle(
                            "-fx-background-color: " + (approved ? "#ff4d4d" : "#4CAF50") + "; -fx-text-fill: white");

                    btn.setOnAction(event -> {
                        Activity activity = getTableView().getItems().get(getIndex());
                        boolean newStatus = !approved;
                        activity.setApproved(newStatus); // Toggle local object
                        getTableView().refresh(); // UI refresh

                        boolean success = ServiceActivity.updateApprovedStatus(activity.getId(), newStatus);
                        if (success) {
                            System.out.println("✅ Statut mis à jour dans la base !");
                        } else {
                            System.err.println("❌ Échec de la mise à jour du statut !");
                        }
                    });

                    setGraphic(btn);
                }
            }
        });

        // Écouteur pour afficher les détails de l'activité sélectionnée
        activityTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showActivityDetails(newSelection);
            }
        });

        // Charger les activités
        afficherActivites();
    }

    private void afficherActivites() {
        // Récupérer les activités depuis le service
        List<Activity> activities = ServiceActivity.getAllActivities();
        activityTableView.getItems().setAll(activities);
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
        }

        if (maximizeButton != null) {
            maximizeButton.setOnAction(event -> {
                System.out.println("Maximizing/restoring window");
                Stage stage = (Stage) maximizeButton.getScene().getWindow();
                stage.setMaximized(!stage.isMaximized());
            });
        }

        if (closeButton != null) {
            closeButton.setOnAction(event -> {
                System.out.println("Closing window");
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.close();
            });
        }
    }

    /**
     * Configure les boutons de navigation
     */
    private void setupNavigationButtons() {
        btnDashboard.setOnAction(this::showDashboard);
        btnUsers.setOnAction(this::showUsers);
        btnActivities.setOnAction(this::showActivities);
        btnComments.setOnAction(this::showComments);
        btnCourses.setOnAction(this::showCourses);
        btnReports.setOnAction(this::showReports);
        btnSettings.setOnAction(this::showSettings);
        btnLogout.setOnAction(this::handleLogout);

        // Configuration des boutons d'action
        btnCreate.setOnAction(this::handleCreate);
        btnUpdate.setOnAction(this::handleUpdate);
        btnDelete.setOnAction(this::handleDelete);
        btnLoad.setOnAction(this::handleLoad);
        btnListen.setOnAction(this::handleListen);
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
     * Ajoute des bordures colorées pour visualiser les composants (debug)
     */
    private void addDebugBorders() {
        if (dashboardView != null)
            dashboardView.setStyle("-fx-border-color: red; -fx-border-width: 1;");
        if (usersView != null)
            usersView.setStyle("-fx-border-color: blue; -fx-border-width: 1;");
        if (activitiesView != null)
            activitiesView.setStyle("-fx-border-color: green; -fx-border-width: 1;");
        if (commentsView != null)
            commentsView.setStyle("-fx-border-color: purple; -fx-border-width: 1;");
        if (coursesView != null)
            coursesView.setStyle("-fx-border-color: orange; -fx-border-width: 1;");
        if (reportsView != null)
            reportsView.setStyle("-fx-border-color: brown; -fx-border-width: 1;");
        if (settingsView != null)
            settingsView.setStyle("-fx-border-color: cyan; -fx-border-width: 1;");
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

    /**
     * Configure la scène principale
     */

    @FXML
    void handleCreate(ActionEvent event) {
        System.out.println("Create button clicked");
        // Implémentation de la création
    }

    @FXML
    void handleDelete(ActionEvent event) {
        System.out.println("Delete button clicked");
        // Implémentation de la suppression
    }

    @FXML
    void handleListen(ActionEvent event) {
        System.out.println("Listen button clicked");
        // Implémentation de l'écoute
    }

    @FXML
    void handleLoad(ActionEvent event) {
        System.out.println("Load button clicked");
        // Implémentation du chargement
    }

    @FXML
    void handleLogout(ActionEvent event) {
        System.out.println("Logout button clicked");
        // Implémentation de la déconnexion
    }

    @FXML
    void handleUpdate(ActionEvent event) {
        System.out.println("Update button clicked");
        // Implémentation de la mise à jour
    }

    @FXML
    void showActivities(ActionEvent event) {
        System.out.println("Showing Activities view");
        hideAllViews();
        if (activitiesView != null) {
            activitiesView.setVisible(true);
            if (lblPageTitle != null)
                lblPageTitle.setText("Activities Management");
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

    // 2. Méthode pour afficher les détails de l'activité sélectionnée
    private void showActivityDetails(Activity selectedActivity) {
        try {
            // Charger le FXML pour la vue des détails
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/details_view.fxml"));
            Parent detailsView = loader.load();

            // Obtenir le contrôleur et passer l'activité sélectionnée
            ActivityDetailController detailsController = loader.getController();
            detailsController.setActivity(selectedActivity);

            // Créer une nouvelle fenêtre pour afficher les détails
            Stage detailsStage = new Stage();
            detailsStage.setTitle("Détails de l'activité: " + selectedActivity.getTitre());
            detailsStage.setScene(new Scene(detailsView));
            detailsStage.initModality(Modality.APPLICATION_MODAL); // Rendre la fenêtre modale
            detailsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'afficher les détails de l'activité", e.getMessage());
        }
    }

    // Méthode utilitaire pour afficher des alertes
    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Utilisateur utilisateur;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        // Mettre à jour l'interface si nécessaire avec les informations de
        // l'utilisateur
    }

    @Override
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
}
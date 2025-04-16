package tn.eduskool.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.Parent;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import tn.eduskool.entities.Activity;
import tn.eduskool.services.ServiceActivity;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FrontController implements Initializable {

    @FXML
    private VBox activityDetailsPane;

    @FXML
    private Button btnActivities;

    @FXML
    private Button btnAddActivity;

    @FXML
    private Button btnCourses;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnNextPage;

    @FXML
    private Button btnNotifications;

    @FXML
    private Button btnPrevPage;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnStudents;

    @FXML
    private Button btnTeachers;

    @FXML
    private ComboBox<String> cmbFilter;

    @FXML
    private TableColumn<Activity, String> colActions;

    @FXML
    private TableColumn<Activity, String> colActiveActions;

    @FXML
    private TableColumn<Activity, String> colActiveDate;

    @FXML
    private TableColumn<Activity, Integer> colActiveId;

    @FXML
    private TableColumn<Activity, String> colActiveName;

    @FXML
    private TableColumn<Activity, Boolean> colActiveStatus;

    @FXML
    private TableColumn<Activity, String> colActiveType;

    @FXML
    private TableColumn<Activity, String> colCompActions;

    @FXML
    private TableColumn<Activity, String> colCompDate;

    @FXML
    private TableColumn<Activity, Integer> colCompId;

    @FXML
    private TableColumn<Activity, String> colCompName;

    @FXML
    private TableColumn<Activity, Boolean> colCompStatus;

    @FXML
    private TableColumn<Activity, String> colCompType;

    @FXML
    private TableColumn<Activity, String> colDate;

    @FXML
    private TableColumn<Activity, Integer> colId;

    @FXML
    private TableColumn<Activity, String> colName;

    @FXML
    private TableColumn<Activity, String> colSchActions;

    @FXML
    private TableColumn<Activity, String> colSchDate;

    @FXML
    private TableColumn<Activity, Integer> colSchId;

    @FXML
    private TableColumn<Activity, String> colSchName;

    @FXML
    private TableColumn<Activity, Boolean> colSchStatus;

    @FXML
    private TableColumn<Activity, String> colSchType;

    @FXML
    private TableColumn<Activity, Boolean> colStatus;

    @FXML
    private TableColumn<Activity, String> colType;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblId;

    @FXML
    private Label lblName;

    @FXML
    private Label lblPageInfo;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblType;

    @FXML
    private Label imagePathLabel;

    @FXML
    private ImageView activityImageView;

    @FXML
    private TabPane tabPane;

    @FXML
    private TableView<Activity> tblActiveActivities;

    @FXML
    private TableView<Activity> tblActivities;

    @FXML
    private TableView<Activity> tblCompletedActivities;

    @FXML
    private TableView<Activity> tblScheduledActivities;

    @FXML
    private TextField txtSearch;

    private ObservableList<Activity> allActivities = FXCollections.observableArrayList();
    private ObservableList<Activity> activeActivities = FXCollections.observableArrayList();
    private ObservableList<Activity> scheduledActivities = FXCollections.observableArrayList();
    private ObservableList<Activity> completedActivities = FXCollections.observableArrayList();

    private int currentPage = 1;
    private final int ITEMS_PER_PAGE = 10;

    // Image par défaut à utiliser lorsqu'aucune image n'est disponible
    private final String DEFAULT_IMAGE = "/images/default-activity.png";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing FrontController...");

        // Cacher le panneau de détails au démarrage
        activityDetailsPane.setVisible(false);

        // Configure filters
        setupFilters();

        // Configure table columns for all tables
        configureAllActivitiesTable();
        configureActiveActivitiesTable();
        configureScheduledActivitiesTable();
        configureCompletedActivitiesTable();

        // Load data
        loadAllActivities();

        // Set up button actions
        setupButtonActions();

        // Set up table selection listeners
        setupTableSelectionListeners();

        // Default to showing all activities
        updatePageInfo();

    }

    private void setupFilters() {
        cmbFilter.getItems().addAll("Tous", "Académique", "Culturel", "Sportif", "Social");
        cmbFilter.setValue("Tous");
        cmbFilter.setOnAction(event -> applyFilter());
    }

    private void applyFilter() {
        String filter = cmbFilter.getValue();
        currentPage = 1; // Reset to first page when filter changes

        if ("Tous".equals(filter)) {
            loadAllActivities();
        } else {
            filterActivitiesByType(filter);
        }
    }

    private void filterActivitiesByType(String activityType) {
        // Get all activities from service
        List<Activity> activities = ServiceActivity.getAllActivities();

        // Clear existing data
        allActivities.clear();
        activeActivities.clear();
        scheduledActivities.clear();
        completedActivities.clear();

        // Filter by type
        for (Activity activity : activities) {
            if (activity.getTypesActivity().equals(activityType)) {
                // Add to main list
                allActivities.add(activity);

                // Also add to specific category lists
                if (activity.isApproved()) {
                    activeActivities.add(activity);
                } else {
                    scheduledActivities.add(activity);
                }

                // For demo, activities with even IDs are "completed"
                if (activity.getId() % 2 == 0) {
                    completedActivities.add(activity);
                }
            }
        }

        // Update tables and pagination
        updateAllActivitiesTable();
        updateActiveActivitiesTable();
        updateScheduledActivitiesTable();
        updateCompletedActivitiesTable();
        updatePageInfo();
    }

    private void configureAllActivitiesTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typesActivity"));
        colDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("isApproved"));

        // Ajustement de la largeur des colonnes
        tblActivities.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void configureActiveActivitiesTable() {
        colActiveId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colActiveName.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colActiveType.setCellValueFactory(new PropertyValueFactory<>("typesActivity"));
        colActiveDate
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        colActiveStatus.setCellValueFactory(new PropertyValueFactory<>("isApproved"));

        tblActiveActivities.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void configureScheduledActivitiesTable() {
        colSchId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSchName.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colSchType.setCellValueFactory(new PropertyValueFactory<>("typesActivity"));
        colSchDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        colSchStatus.setCellValueFactory(new PropertyValueFactory<>("isApproved"));

        tblScheduledActivities.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void configureCompletedActivitiesTable() {
        colCompId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCompName.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colCompType.setCellValueFactory(new PropertyValueFactory<>("typesActivity"));
        colCompDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        colCompStatus.setCellValueFactory(new PropertyValueFactory<>("isApproved"));
        tblCompletedActivities.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void loadAllActivities() {
        try {
            // Récupération des activités depuis le service
            List<Activity> activities = ServiceActivity.getAllActivities();

            // Nettoyage des listes existantes
            allActivities.clear();
            activeActivities.clear();
            scheduledActivities.clear();
            completedActivities.clear();

            // Classement des activités dans les différentes listes
            for (Activity activity : activities) {
                allActivities.add(activity);

                // Activités en cours (approuvées)
                if (activity.isApproved()) {
                    activeActivities.add(activity);
                } else {
                    // Activités programmées (non approuvées)
                    scheduledActivities.add(activity);
                }

                // Pour la démonstration, les activités avec ID pair sont considérées comme
                // "terminées"
                if (activity.getId() % 2 == 0) {
                    completedActivities.add(activity);
                }
            }

            // Mise à jour des tableaux
            updateAllActivitiesTable();
            updateActiveActivitiesTable();
            updateScheduledActivitiesTable();
            updateCompletedActivitiesTable();

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des activités: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateAllActivitiesTable() {
        int fromIndex = (currentPage - 1) * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, allActivities.size());

        if (fromIndex <= toIndex) {
            ObservableList<Activity> pageItems = FXCollections.observableArrayList(
                    allActivities.subList(fromIndex, toIndex));
            tblActivities.setItems(pageItems);
        } else {
            tblActivities.setItems(FXCollections.observableArrayList());
        }
    }

    private void updateActiveActivitiesTable() {
        tblActiveActivities.setItems(activeActivities);
    }

    private void updateScheduledActivitiesTable() {
        tblScheduledActivities.setItems(scheduledActivities);
    }

    private void updateCompletedActivitiesTable() {
        tblCompletedActivities.setItems(completedActivities);
    }

    private void updatePageInfo() {
        int totalPages = (int) Math.ceil((double) allActivities.size() / ITEMS_PER_PAGE);
        lblPageInfo.setText("Page " + currentPage + " sur " + Math.max(1, totalPages));

        // Activer/désactiver les boutons de pagination
        btnPrevPage.setDisable(currentPage <= 1);
        btnNextPage.setDisable(currentPage >= totalPages);
    }

    private void setupButtonActions() {
        // Boutons de pagination
        btnPrevPage.setOnAction(e -> {
            if (currentPage > 1) {
                currentPage--;
                updateAllActivitiesTable();
                updatePageInfo();
            }
        });

        btnNextPage.setOnAction(e -> {
            int totalPages = (int) Math.ceil((double) allActivities.size() / ITEMS_PER_PAGE);
            if (currentPage < totalPages) {
                currentPage++;
                updateAllActivitiesTable();
                updatePageInfo();
            }
        });

        // Bouton de recherche
        btnSearch.setOnAction(e -> {
            String searchText = txtSearch.getText().trim().toLowerCase();
            if (searchText.isEmpty()) {
                loadAllActivities();
            } else {
                searchActivities(searchText);
            }
        });

        // Bouton d'ajout d'activité
        btnAddActivity.setOnAction(e -> openAddActivityForm());

        // Boutons d'édition et de suppression

        // Boutons de navigation dans le sidebar
        btnStudents.setOnAction(e -> navigateToStudents());
        btnTeachers.setOnAction(e -> navigateToTeachers());
        btnCourses.setOnAction(e -> navigateToCourses());
        btnLogout.setOnAction(e -> logout());

        // Boutons d'en-tête
        btnNotifications.setOnAction(e -> showNotifications());
        btnProfile.setOnAction(e -> showProfile());
    }

    private void setupTableSelectionListeners() {
        tblActivities.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showActivityDetails(newSelection);
            }
        });

        tblActiveActivities.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        showActivityDetails(newSelection);
                    }
                });

        tblScheduledActivities.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        showActivityDetails(newSelection);
                    }
                });

        tblCompletedActivities.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        showActivityDetails(newSelection);
                    }
                });
    }

    private void showActivityDetails(Activity activity) {
        // Afficher le panneau de détails
        activityDetailsPane.setVisible(true);

        // Mise à jour des labels avec les informations de l'activité
        lblId.setText(String.valueOf(activity.getId()));
        lblName.setText(activity.getTitre());
        lblType.setText(activity.getTypesActivity());
        lblDate.setText(activity.getDate().toString());

        // Configuration du statut avec formatage approprié
        String status = activity.isApproved() ? "En cours" : "Programmée";
        lblStatus.setText(status);
        lblStatus.getStyleClass().clear();
        lblStatus.getStyleClass().add(activity.isApproved() ? "status-active" : "status-scheduled");

        // Affichage de la description
        lblDescription.setText(activity.getDescription());

        // Chargement de l'image
        loadActivityImage(activity);

        // TODO: Chargement des participants (à implémenter selon les besoins)
    }

    private void loadActivityImage(Activity activity) {
        try {
            // Récupérer le nom du fichier image de l'activité
            String imageFileName = activity.getImageFileName();

            if (imageFileName != null && !imageFileName.isEmpty()) {
                // Utiliser le même chemin que dans l'autre contrôleur qui fonctionne
                File imageFile = new File("uploads-img/" + imageFileName);

                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    activityImageView.setImage(image);
                    imagePathLabel.setText(imageFileName);
                    return;
                } else {
                    System.err.println("Le fichier image n'existe pas: " + imageFile.getAbsolutePath());
                    imagePathLabel.setText("Image non trouvée: " + imageFileName);
                }
            }

            // Si aucune image n'est disponible ou si le fichier n'existe pas, charger
            // l'image par défaut
            Image defaultImage = new Image(getClass().getResourceAsStream(DEFAULT_IMAGE));
            activityImageView.setImage(defaultImage);
            imagePathLabel.setText("Aucune image disponible");

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image: " + e.getMessage());
            e.printStackTrace();

            // En cas d'erreur, charger l'image par défaut
            Image defaultImage = new Image(getClass().getResourceAsStream(DEFAULT_IMAGE));
            activityImageView.setImage(defaultImage);
            imagePathLabel.setText("Erreur de chargement d'image");
        }
    }

    private void searchActivities(String searchText) {
        // Récupération des activités depuis le service
        List<Activity> activities = ServiceActivity.getAllActivities();

        // Nettoyage des listes existantes
        allActivities.clear();
        activeActivities.clear();
        scheduledActivities.clear();
        completedActivities.clear();

        // Filtrage des activités selon le texte de recherche
        for (Activity activity : activities) {
            if (activity.getTitre().toLowerCase().contains(searchText) ||
                    activity.getDescription().toLowerCase().contains(searchText) ||
                    activity.getTypesActivity().toLowerCase().contains(searchText)) {

                // Ajout à la liste principale
                allActivities.add(activity);

                // Ajout aux listes spécifiques
                if (activity.isApproved()) {
                    activeActivities.add(activity);
                } else {
                    scheduledActivities.add(activity);
                }

                // Pour la démonstration, les activités avec ID pair sont considérées comme
                // "terminées"
                if (activity.getId() % 2 == 0) {
                    completedActivities.add(activity);
                }
            }
        }

        // Mise à jour des tableaux et de la pagination
        currentPage = 1; // Reset to first page when searching
        updateAllActivitiesTable();
        updateActiveActivitiesTable();
        updateScheduledActivitiesTable();
        updateCompletedActivitiesTable();
        updatePageInfo();
    }

    // Méthodes pour la navigation (à implémenter selon vos besoins)
    private void openAddActivityForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/activity_view.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle
            Scene currentScene = btnAddActivity.getScene();

            // Remplacer le contenu de la scène actuelle
            currentScene.setRoot(root);

            // Optionnel: Récupérer le contrôleur de la nouvelle vue si vous avez besoin
            // d'interagir avec
            // ActivityController controller = loader.getController();
            // controller.initData(); // Si vous avez besoin d'initialiser des données

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de activity_view.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void editSelectedActivity() {
        System.out.println("Editing selected activity...");
        // TODO: Implémenter l'édition de l'activité sélectionnée
    }

    private void deleteSelectedActivity() {
        System.out.println("Deleting selected activity...");
        // TODO: Implémenter la suppression de l'activité sélectionnée avec confirmation
    }

    private void navigateToDashboard() {
        System.out.println("Navigating to dashboard...");
        // TODO: Implémenter la navigation vers le tableau de bord
    }

    private void navigateToStudents() {
        System.out.println("Navigating to students...");
        // TODO: Implémenter la navigation vers la gestion des étudiants
    }

    private void navigateToTeachers() {
        System.out.println("Navigating to teachers...");
        // TODO: Implémenter la navigation vers la gestion des enseignants
    }

    private void navigateToCourses() {
        System.out.println("Navigating to courses...");
        // TODO: Implémenter la navigation vers la gestion des cours
    }

    private void navigateToSchedule() {
        System.out.println("Navigating to schedule...");
        // TODO: Implémenter la navigation vers l'emploi du temps
    }

    private void navigateToReports() {
        System.out.println("Navigating to reports...");
        // TODO: Implémenter la navigation vers les rapports
    }

    private void navigateToSettings() {
        System.out.println("Navigating to settings...");
        // TODO: Implémenter la navigation vers les paramètres
    }

    private void logout() {
        System.out.println("Logging out...");
        // TODO: Implémenter la déconnexion
    }

    private void showNotifications() {
        System.out.println("Showing notifications...");
        // TODO: Implémenter l'affichage des notifications
    }

    private void showProfile() {
        System.out.println("Showing profile...");
        // TODO: Implémenter l'affichage du profil
    }
}
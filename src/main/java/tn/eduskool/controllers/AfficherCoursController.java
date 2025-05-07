package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.eduskool.entities.Cours;
import tn.eduskool.entities.Theme;
import tn.eduskool.services.CoursService;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class AfficherCoursController {

    @FXML
    private TableView<Cours> coursTable;

    @FXML
    private TableColumn<Cours, Integer> idColumn;

    @FXML
    private TableColumn<Cours, String> titreColumn;

    @FXML
    private TableColumn<Cours, LocalDateTime> dateHeureColumn;

    @FXML
    private TableColumn<Cours, String> enseignantColumn;

    @FXML
    private TableColumn<Cours, Theme> themeColumn;

    @FXML
    private TableColumn<Cours, String> fichierColumn; // Nouvelle colonne pour fichier

    @FXML
    private TableColumn<Cours, Void> actionsColumn;

    private CoursService coursService;
    private ObservableList<Cours> coursList;

    @FXML
    public void initialize() {
        coursService = new CoursService();

        // Initialize columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));

        // Format dateHeure column
        dateHeureColumn.setCellValueFactory(new PropertyValueFactory<>("dateHeure"));
        dateHeureColumn.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            @Override
            protected void updateItem(LocalDateTime dateTime, boolean empty) {
                super.updateItem(dateTime, empty);
                if (empty || dateTime == null) {
                    setText(null);
                } else {
                    setText(formatter.format(dateTime));
                }
            }
        });

        enseignantColumn.setCellValueFactory(new PropertyValueFactory<>("enseignant"));
        themeColumn.setCellValueFactory(new PropertyValueFactory<>("theme"));
        fichierColumn.setCellValueFactory(new PropertyValueFactory<>("fichier")); // Ajout de la colonne fichier

        // Set up the actions column with icon-based buttons for Ajouter, Modifier, Supprimer
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button addButton = new Button();
            private final Button editButton = new Button();
            private final Button deleteButton = new Button();

            {
                // Load icons
                ImageView addIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/add.png")));
                ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/edit.png")));
                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/delete.png")));

                // Set icon sizes (adjust as needed)
                addIcon.setFitWidth(16);
                addIcon.setFitHeight(16);
                editIcon.setFitWidth(16);
                editIcon.setFitHeight(16);
                deleteIcon.setFitWidth(16);
                deleteIcon.setFitHeight(16);

                // Set icons to buttons
                addButton.setGraphic(addIcon);
                editButton.setGraphic(editIcon);
                deleteButton.setGraphic(deleteIcon);

                // Style buttons (background color for visual distinction)
                addButton.setStyle("-fx-background-color: #2922ec;"); // Vert pour Ajouter
                editButton.setStyle("-fx-background-color: #30689f;"); // Bleu pour Modifier
                deleteButton.setStyle("-fx-background-color: #6eddff;"); // Bleu clair pour Supprimer

                // Add tooltips for clarity
                addButton.setTooltip(new Tooltip("Ajouter un nouveau cours"));
                editButton.setTooltip(new Tooltip("Modifier ce cours"));
                deleteButton.setTooltip(new Tooltip("Supprimer ce cours"));

                // Add event handlers
                addButton.setOnAction(event -> {
                    handleAddCours(); // Ouvre le formulaire d'ajout
                });

                editButton.setOnAction(event -> {
                    Cours cours = getTableView().getItems().get(getIndex());
                    handleEditCours(cours);
                });

                deleteButton.setOnAction(event -> {
                    Cours cours = getTableView().getItems().get(getIndex());
                    handleDeleteCours(cours);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, addButton, editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });

        // Load data
        loadCoursData();
    }

    private void loadCoursData() {
        try {
            List<Cours> cours = coursService.recuperer();
            coursList = FXCollections.observableArrayList(cours);
            coursTable.setItems(coursList);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des cours", e.getMessage());
        }
    }

    @FXML
    private void handleAddCours() {
        try {
            // Charger le fichier FXML pour l'ajout de cours
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddCours.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Stage stage = new Stage();
            stage.setTitle("Ajouter un nouveau cours");
            stage.setScene(new Scene(root));

            // Configurer comme fenêtre modale
            stage.initModality(Modality.APPLICATION_MODAL);

            // Afficher la fenêtre et attendre sa fermeture
            stage.showAndWait();

            // Rafraîchir les données après l'ajout
            loadCoursData();

        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire d'ajout", e.getMessage());
        }
    }

    private void handleEditCours(Cours cours) {
        try {
            // Charger le fichier FXML pour l'édition de cours
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditCours.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur et passer le cours à éditer
            EditCoursController controller = loader.getController();
            controller.setCoursToEdit(cours);

            // Créer une nouvelle scène
            Stage stage = new Stage();
            stage.setTitle("Modifier le cours");
            stage.setScene(new Scene(root));

            // Configurer comme fenêtre modale
            stage.initModality(Modality.APPLICATION_MODAL);

            // Afficher la fenêtre et attendre sa fermeture
            stage.showAndWait();

            // Rafraîchir les données après la modification
            loadCoursData();

        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire d'édition", e.getMessage());
        }
    }

    private void handleDeleteCours(Cours cours) {
        try {
            // Confirmation de suppression
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supprimer le cours");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer le cours: " + cours.getTitre() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                coursService.supprimer(cours.getId());
                loadCoursData();
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la suppression", e.getMessage());
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
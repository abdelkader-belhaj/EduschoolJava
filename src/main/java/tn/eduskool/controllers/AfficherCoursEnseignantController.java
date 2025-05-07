package tn.eduskool.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.eduskool.entities.Cours;
import tn.eduskool.entities.Theme;
import tn.eduskool.services.CoursService;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AfficherCoursEnseignantController {

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
    private TableColumn<Cours, Void> actionsColumn;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> searchCriteriaComboBox;

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

                // Set icon sizes
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

                // Style buttons
                addButton.setStyle("-fx-background-color: #2922ec;");
                editButton.setStyle("-fx-background-color: #30689f;");
                deleteButton.setStyle("-fx-background-color: #6eddff;");

                // Add tooltips
                addButton.setTooltip(new Tooltip("Ajouter un nouveau cours"));
                editButton.setTooltip(new Tooltip("Modifier ce cours"));
                deleteButton.setTooltip(new Tooltip("Supprimer ce cours"));

                // Add event handlers
                addButton.setOnAction(event -> {
                    handleAddCours();
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
                    setGraphic(buttons); // Fixed typo here (was 'button', now 'buttons')
                }
            }
        });

        // Initialize ComboBox with search criteria
        searchCriteriaComboBox.setItems(FXCollections.observableArrayList("Titre", "Thème", "Enseignant"));
        searchCriteriaComboBox.getSelectionModel().selectFirst();

        // Load data and setup search and sort
        loadCoursData();
        setupSearchAndSort();
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

    private void setupSearchAndSort() {
        // Create FilteredList for search functionality
        FilteredList<Cours> filteredData = new FilteredList<>(coursList, p -> true);

        // Add listener to search field and ComboBox
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(cours -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                String selectedCriteria = searchCriteriaComboBox.getSelectionModel().getSelectedItem();

                switch (selectedCriteria != null ? selectedCriteria : "Titre") {
                    case "Titre":
                        return cours.getTitre().toLowerCase().contains(lowerCaseFilter);
                    case "Enseignant":
                        return cours.getEnseignant().toLowerCase().contains(lowerCaseFilter);
                    case "Thème":
                        return cours.getTheme() != null &&
                                cours.getTheme().toString().toLowerCase().contains(lowerCaseFilter);
                    default:
                        return true;
                }
            });
        });

        // Update filter when criteria changes
        searchCriteriaComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(cours -> {
                String searchText = searchField.getText();
                if (searchText == null || searchText.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = searchText.toLowerCase();

                switch (newValue != null ? newValue : "Titre") {
                    case "Titre":
                        return cours.getTitre().toLowerCase().contains(lowerCaseFilter);
                    case "Enseignant":
                        return cours.getEnseignant().toLowerCase().contains(lowerCaseFilter);
                    case "Thème":
                        return cours.getTheme() != null &&
                                cours.getTheme().toString().toLowerCase().contains(lowerCaseFilter);
                    default:
                        return true;
                }
            });
        });

        // Create SortedList for sorting functionality
        SortedList<Cours> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList comparator to the TableView comparator
        sortedData.comparatorProperty().bind(coursTable.comparatorProperty());

        // Set the sorted and filtered data to the table
        coursTable.setItems(sortedData);

        // Configure dateHeureColumn for proper sorting
        dateHeureColumn.setComparator(Comparator.comparing(LocalDateTime::toString, Comparator.naturalOrder()));

        // Set default sort by date (descending, newest first)
        coursTable.getSortOrder().clear();
        coursTable.getSortOrder().add(dateHeureColumn);
        dateHeureColumn.setSortType(TableColumn.SortType.DESCENDING);
        coursTable.sort();
    }

    @FXML
    private void handleAddCours() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddCours.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter un nouveau cours");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadCoursData();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire d'ajout", e.getMessage());
        }
    }

    private void handleEditCours(Cours cours) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditCours.fxml"));
            Parent root = loader.load();

            EditCoursController controller = loader.getController();
            controller.setCoursToEdit(cours);

            Stage stage = new Stage();
            stage.setTitle("Modifier le cours");
            stage.setScene(new Scene(root));

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            loadCoursData();
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'ouverture du formulaire d'édition", e.getMessage());
        }
    }

    private void handleDeleteCours(Cours cours) {
        try {
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
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            // Charger FrontEnseignant.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FrontEnseignantController.fxml"));
            Parent frontEnseignantView = loader.load();
            // Obtenir la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // Définir la nouvelle scène
            Scene scene = new Scene(frontEnseignantView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de FrontEnseignant.fxml");
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
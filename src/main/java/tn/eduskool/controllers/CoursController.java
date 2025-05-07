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
import javafx.stage.Stage;
import tn.eduskool.entities.Cours;
import tn.eduskool.entities.Theme;
import tn.eduskool.services.CoursService;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class CoursController {

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
    private TableColumn<Cours, String> fichierColumn;

    @FXML
    private TableColumn<Cours, Void> downloadColumn; // Nouvelle colonne pour téléchargement

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
        fichierColumn.setCellValueFactory(new PropertyValueFactory<>("fichier"));

        // Optional: Afficher seulement le nom du fichier pour la colonne fichier
        fichierColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String fichier, boolean empty) {
                super.updateItem(fichier, empty);
                if (empty || fichier == null) {
                    setText(null);
                } else {
                    setText(new File(fichier).getName());
                }
            }
        });

        // Configurer la colonne de téléchargement
        downloadColumn.setCellFactory(column -> new TableCell<>() {
            private final Button downloadButton = new Button("Télécharger");

            {
                downloadButton.setStyle("-fx-background-color:  #FFC0CB; -fx-text-fill: white;");
                downloadButton.setOnAction(event -> {
                    Cours cours = getTableView().getItems().get(getIndex());
                    handleDownload(cours);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(downloadButton);
                }
            }
        });

        // Initialize ComboBox with search criteria
        searchCriteriaComboBox.setItems(FXCollections.observableArrayList("Titre", "Thème", "Enseignant", "Fichier"));
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

                // Filtrer selon le critère sélectionné
                switch (selectedCriteria != null ? selectedCriteria : "Titre") {
                    case "Titre":
                        return cours.getTitre().toLowerCase().contains(lowerCaseFilter);
                    case "Enseignant":
                        return cours.getEnseignant().toLowerCase().contains(lowerCaseFilter);
                    case "Thème":
                        return cours.getTheme() != null &&
                                cours.getTheme().toString().toLowerCase().contains(lowerCaseFilter);
                    case "Fichier":
                        return cours.getFichier() != null &&
                                cours.getFichier().toLowerCase().contains(lowerCaseFilter);
                    default:
                        return true;
                }
            });
        });

        // Mettre à jour le filtre lorsque le critère change
        searchCriteriaComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(cours -> {
                String searchText = searchField.getText();
                if (searchText == null || searchText.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = searchText.toLowerCase();

                // Filtrer selon le nouveau critère
                switch (newValue != null ? newValue : "Titre") {
                    case "Titre":
                        return cours.getTitre().toLowerCase().contains(lowerCaseFilter);
                    case "Enseignant":
                        return cours.getEnseignant().toLowerCase().contains(lowerCaseFilter);
                    case "Thème":
                        return cours.getTheme() != null &&
                                cours.getTheme().toString().toLowerCase().contains(lowerCaseFilter);
                    case "Fichier":
                        return cours.getFichier() != null &&
                                cours.getFichier().toLowerCase().contains(lowerCaseFilter);
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

    private void handleDownload(Cours cours) {
        if (cours.getFichier() == null || cours.getFichier().isEmpty()) {
            showAlert("Erreur", "Aucun fichier", "Aucun fichier PDF n'est associé à ce cours.");
            return;
        }

        File file = new File(cours.getFichier());
        if (!file.exists()) {
            showAlert("Erreur", "Fichier introuvable", "Le fichier PDF n'existe pas à l'emplacement spécifié : " + cours.getFichier());
            return;
        }

        try {
            Desktop.getDesktop().open(file); // Ouvre le fichier avec l'application par défaut
        } catch (IOException e) {
            showAlert("Erreur", "Erreur d'ouverture", "Impossible d'ouvrir le fichier PDF : " + e.getMessage());
        }
    }
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            // Charger FrontEnseignant.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            Parent frontEnseignantView = loader.load();
            // Obtenir la scène actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // Définir la nouvelle scène
            Scene scene = new Scene(frontEnseignantView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de MainView.fxml");
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
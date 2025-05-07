package tn.eduskool.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tn.eduskool.entities.Theme;
import tn.eduskool.services.ThemeService;

import java.sql.SQLException;
import java.util.Optional;

public class AfficherThemeController {
    @FXML
    private TableView<Theme> themeTable;
    @FXML
    private TableColumn<Theme, Integer> idCol;
    @FXML
    private TableColumn<Theme, String> titreCol;
    @FXML
    private TableColumn<Theme, Void> actionsCol;

    private ThemeService themeService = new ThemeService();
    private ObservableList<Theme> themesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurer les colonnes
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));

        // Configurer la colonne d'actions avec icônes pour Ajouter, Modifier, Supprimer
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button addButton = new Button();
            private final Button editButton = new Button();
            private final Button deleteButton = new Button();

            {
                // Charger les icônes
                ImageView addIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/add.png")));
                ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/edit.png")));
                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/delete.png")));

                // Définir la taille des icônes (ajustez si nécessaire)
                addIcon.setFitWidth(16);
                addIcon.setFitHeight(16);
                editIcon.setFitWidth(16);
                editIcon.setFitHeight(16);
                deleteIcon.setFitWidth(16);
                deleteIcon.setFitHeight(16);

                // Associer les icônes aux boutons
                addButton.setGraphic(addIcon);
                editButton.setGraphic(editIcon);
                deleteButton.setGraphic(deleteIcon);

                // Style des boutons (couleurs de fond pour distinction visuelle)
                addButton.setStyle("-fx-background-color: #2922ec;"); // Vert pour Ajouter
                editButton.setStyle("-fx-background-color: #30689f;"); // Bleu pour Modifier
                deleteButton.setStyle("-fx-background-color: #6eddff;"); // Bleu clair pour Supprimer

                // Ajouter des infobulles pour plus de clarté
                addButton.setTooltip(new Tooltip("Ajouter un nouveau thème"));
                editButton.setTooltip(new Tooltip("Modifier ce thème"));
                deleteButton.setTooltip(new Tooltip("Supprimer ce thème"));

                // Action pour le bouton Ajouter
                addButton.setOnAction(event -> {
                    handleAddTheme();
                });

                // Action pour le bouton Modifier
                editButton.setOnAction(event -> {
                    Theme theme = getTableView().getItems().get(getIndex());
                    showEditDialog(theme);
                });

                // Action pour le bouton Supprimer
                deleteButton.setOnAction(event -> {
                    Theme theme = getTableView().getItems().get(getIndex());
                    showDeleteConfirmation(theme);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, addButton, editButton, deleteButton));
                }
            }
        });

        // Charger les données
        loadThemes();
    }

    private void loadThemes() {
        try {
            themesList.clear();
            themesList.addAll(themeService.recuperer());
            themeTable.setItems(themesList);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors du chargement des thèmes", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAddTheme() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nouveau Thème");
        dialog.setHeaderText("Ajouter un nouveau thème");
        dialog.setContentText("Titre du thème:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(titre -> {
            if (titre == null || titre.trim().isEmpty()) {
                showAlert("Erreur", "Champ vide", "Le titre ne peut pas être vide", Alert.AlertType.WARNING);
                return;
            }

            try {
                Theme nouveauTheme = new Theme();
                nouveauTheme.setTitre(titre.trim());
                themeService.ajouter(nouveauTheme);
                loadThemes();
                showAlert("Succès", "Thème ajouté", "Le thème a été ajouté avec succès", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de l'ajout", e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void showEditDialog(Theme theme) {
        TextInputDialog dialog = new TextInputDialog(theme.getTitre());
        dialog.setTitle("Modifier Thème");
        dialog.setHeaderText("Modification du thème ID: " + theme.getId());
        dialog.setContentText("Nouveau titre:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newTitre -> {
            if (newTitre.isEmpty()) {
                showAlert("Erreur", "Champ vide", "Le titre ne peut pas être vide", Alert.AlertType.WARNING);
                return;
            }

            try {
                theme.setTitre(newTitre);
                themeService.modifier(theme);
                loadThemes();
                showAlert("Succès", "Thème modifié", "Le thème a été modifié avec succès", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la modification", e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void showDeleteConfirmation(Theme theme) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le thème: " + theme.getTitre());
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce thème? Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                themeService.supprimer(theme.getId());
                loadThemes();
                showAlert("Succès", "Thème supprimé", "Le thème a été supprimé avec succès", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Erreur", "Erreur lors de la suppression", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
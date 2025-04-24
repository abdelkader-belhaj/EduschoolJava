package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.eduskool.entities.Devoir;
import tn.eduskool.services.DevoirService;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.text.Text;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ListeDevoirsController {

    @FXML private TableView<Devoir> devoirTable;
    @FXML private TableColumn<Devoir, String> idCol;
    @FXML private TableColumn<Devoir, String> titreCol;
    @FXML private TableColumn<Devoir, String> descriptionCol;
    @FXML private TableColumn<Devoir, String> dateLimiteCol;
    @FXML private TableColumn<Devoir, String> fichierCol;
    @FXML private TableColumn<Devoir, Void> actionsCol;
    @FXML private TableColumn<Devoir, Void> colAction;

    private final DevoirService devoirService = new DevoirService();

    @FXML
    public void initialize() {
        // Configuration des colonnes
        idCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        titreCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitre()));
        descriptionCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        dateLimiteCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDatelimite().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        fichierCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFichier()));

        // Colonne description avec texte multilignes
        descriptionCol.setCellFactory(tc -> {
            TableCell<Devoir, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(descriptionCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

        // Ajouter les colonnes d'actions
        ajouterBoutonModifierEtSupprimer();
        ajouterBoutonVoirSoumissions();

        chargerDevoirs();
        devoirTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void ajouterBoutonModifierEtSupprimer() {
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button modifierBtn = new Button("Modifier");
            private final Button supprimerBtn = new Button("Supprimer");
            private final HBox container = new HBox(10, modifierBtn, supprimerBtn);

            {
                // Supprimer les classes par défaut
                modifierBtn.getStyleClass().removeAll("button");
                supprimerBtn.getStyleClass().removeAll("button");

                // Ajouter nos classes personnalisées
                modifierBtn.getStyleClass().add("action-button-modifier");
                supprimerBtn.getStyleClass().add("action-button-supprimer");

                container.setStyle("-fx-padding: 5;");

                modifierBtn.setOnAction(event -> modifierDevoir(getTableView().getItems().get(getIndex())));
                supprimerBtn.setOnAction(event -> supprimerDevoir(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });
    }

    private void ajouterBoutonVoirSoumissions() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnVoir = new Button("Voir Soumissions");

            {
                btnVoir.getStyleClass().add("action-button-soumettre"); // ou une classe personnalisée si tu veux
                btnVoir.setOnAction(event -> {
                    Devoir devoir = getTableView().getItems().get(getIndex());
                    afficherSoumissions(devoir);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnVoir);
            }
        });
    }


    private void modifierDevoir(Devoir devoir) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierDevoir.fxml"));
            Parent root = loader.load();
            ModifierDevoirController controller = loader.getController();
            controller.initData(devoir);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(devoirTable.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Devoir");
            stage.setOnHidden(e -> chargerDevoirs());
            stage.showAndWait();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir l'éditeur");
        }
    }

    private void supprimerDevoir(Devoir devoir) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le devoir");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce devoir ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (devoirService.supprimer(devoir.getId())) {
                showAlert("Succès", "Devoir supprimé avec succès");
                chargerDevoirs();
            } else {
                showAlert("Erreur", "Échec de la suppression");
            }
        }
    }

    private void afficherSoumissions(Devoir devoir) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SoumissionsDevoirEnseignant.fxml"));
            Parent root = loader.load();

            ListeSoumissionsEnseignantController controller = loader.getController();
            controller.initData(devoir);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Soumissions pour le devoir : " + devoir.getTitre());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'afficher les soumissions");
        }
    }

    private void chargerDevoirs() {
        List<Devoir> devoirs = devoirService.recuperer();
        devoirTable.getItems().setAll(devoirs);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void ouvrirAjoutDevoir() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterDevoir.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un devoir");
            stage.showAndWait();

            chargerDevoirs();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir le formulaire d'ajout");
        }
    }
}

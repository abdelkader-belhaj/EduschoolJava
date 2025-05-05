package tn.eduskool.controllers;

import javafx.application.Platform;
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
import tn.eduskool.entities.Utilisateur;
import tn.eduskool.services.DevoirService;
import javafx.beans.property.SimpleStringProperty;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ListeDevoirsController implements BaseController {
    private Utilisateur utilisateur;

    @FXML
    private TableView<Devoir> devoirTable;
    @FXML
    private TableColumn<Devoir, String> idCol;
    @FXML
    private TableColumn<Devoir, String> titreCol;
    @FXML
    private TableColumn<Devoir, String> descriptionCol;
    @FXML
    private TableColumn<Devoir, String> dateLimiteCol;
    @FXML
    private TableColumn<Devoir, String> fichierCol;
    @FXML
    private TableColumn<Devoir, Void> actionsCol;
    @FXML
    private TableColumn<Devoir, Void> colAction;

    private final DevoirService devoirService = new DevoirService();

    @FXML
    public void initialize() {
        // Configuration des colonnes de base
        configurerColonnes();
        // Chargement initial des données
        Platform.runLater(this::chargerDevoirs);
    }

    private void configurerColonnes() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionCol.setCellValueFactory(cellData -> {
            String desc = cellData.getValue().getDescription();
            return new SimpleStringProperty(desc != null ? desc : "");
        });
        dateLimiteCol.setCellValueFactory(cellData -> {
            Devoir devoir = cellData.getValue();
            return new SimpleStringProperty(devoir != null ? devoir.getFormattedDate() : "");
        });
        fichierCol.setCellValueFactory(new PropertyValueFactory<>("fichier"));

        // Configuration des colonnes d'action
        actionsCol.setCellFactory(col -> new TableCell<Devoir, Void>() {
            private final Button editBtn = new Button("Modifier");
            private final Button deleteBtn = new Button("Supprimer");
            private final HBox buttonsBox = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.getStyleClass().add("action-button-modifier");
                deleteBtn.getStyleClass().add("action-button-supprimer");

                editBtn.setOnAction(e -> {
                    Devoir devoir = getTableView().getItems().get(getIndex());
                    modifierDevoir(devoir);
                });

                deleteBtn.setOnAction(e -> {
                    Devoir devoir = getTableView().getItems().get(getIndex());
                    supprimerDevoir(devoir);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonsBox);
            }
        });

        colAction.setCellFactory(col -> new TableCell<Devoir, Void>() {
            private final Button viewBtn = new Button("Voir Soumissions");
            {
                viewBtn.getStyleClass().add("action-button-modifier");
                viewBtn.setOnAction(e -> {
                    Devoir devoir = getTableView().getItems().get(getIndex());
                    afficherSoumissions(devoir);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : viewBtn);
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
            stage.setTitle("Modifier Devoir");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Rafraîchir la liste après modification
            chargerDevoirs();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Impossible d'ouvrir l'éditeur: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void supprimerDevoir(Devoir devoir) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce devoir ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                devoirService.supprimer(devoir.getId());
                chargerDevoirs();
            } catch (Exception e) {
                Alert erreur = new Alert(Alert.AlertType.ERROR);
                erreur.setTitle("Erreur");
                erreur.setContentText("Erreur lors de la suppression du devoir");
                erreur.showAndWait();
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

    public void chargerDevoirs() {
        try {
            if (utilisateur != null) {
                List<Devoir> devoirs = devoirService.recupererParEnseignant(utilisateur.getIdUtilisateur());
                if (devoirs != null) {
                    devoirTable.getItems().setAll(devoirs);
                } else {
                    System.err.println("La liste des devoirs est null");
                }
            } else {
                System.err.println("L'utilisateur n'est pas initialisé");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les devoirs: " + e.getMessage());
        }
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
            if (loader.getLocation() == null) {
                System.err.println("Le fichier AjouterDevoir.fxml n'a pas été trouvé");
                return;
            }
            Parent root = loader.load();

            // Récupérer le contrôleur et lui passer l'utilisateur connecté
            AjouterDevoirController controller = loader.getController();
            controller.setUtilisateur(utilisateur);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un devoir");
            stage.showAndWait();

            chargerDevoirs();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le formulaire d'ajout: " + e.getMessage());
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
}

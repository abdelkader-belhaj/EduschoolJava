package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.eduskool.entities.SoumissionDevoir;
import tn.eduskool.entities.Devoir;
import tn.eduskool.services.SoumissionDevoirService;

import java.util.List;

public class ListeSoumissionsController {

    @FXML private TableView<SoumissionDevoir> tableSoumissions;
    @FXML private TableColumn<SoumissionDevoir, String> colDevoir;
    @FXML private TableColumn<SoumissionDevoir, String> colDate;
    @FXML private TableColumn<SoumissionDevoir, String> colFichier;
    @FXML private TableColumn<SoumissionDevoir, Integer> colNote;
    @FXML private TableColumn<SoumissionDevoir, Void> colActions;

    private final SoumissionDevoirService soumissionService = new SoumissionDevoirService();
    private Devoir devoir;

    @FXML
    public void initialize() {
        configurerColonnes();
        chargerSoumissions();
    }

    private void configurerColonnes() {
        colDevoir.setCellValueFactory(cellData -> {
            Devoir d = cellData.getValue().getDevoir();
            return new javafx.beans.property.SimpleStringProperty(d != null ? d.getTitre() : "N/A");
        });

        colDate.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getDateSoumission().toString())
        );

        colFichier.setCellValueFactory(new PropertyValueFactory<>("fichier"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnModifier = new Button("✏️");
            private final Button btnSupprimer = new Button("🗑");
            private final HBox pane = new HBox(10, btnModifier, btnSupprimer);

            {
                btnModifier.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                btnSupprimer.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

                btnModifier.setOnAction(event -> modifierSoumission());
                btnSupprimer.setOnAction(event -> supprimerSoumission());
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }

            private void modifierSoumission() {
                SoumissionDevoir soum = getTableView().getItems().get(getIndex());
                ouvrirFenetreModification(soum);
            }

            private void supprimerSoumission() {
                SoumissionDevoir soum = getTableView().getItems().get(getIndex());
                if (soumissionService.supprimer(soum.getId())) {
                    rafraichirTable();
                }
            }
        });
    }

    private void ouvrirFenetreModification(SoumissionDevoir soumission) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierSoumission.fxml"));
            Parent root = loader.load();

            ModifierSoumissionController controller = loader.getController();
            controller.setSoumission(soumission);
            controller.setListeSoumissionsController(this);

            Stage stage = new Stage();
            stage.setTitle("Modifier Soumission");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de modification");
        }
    }

    public void chargerSoumissions() {
        List<SoumissionDevoir> soumissions = soumissionService.recuperer();
        tableSoumissions.getItems().setAll(soumissions);
    }

    public void chargerSoumissionsParDevoir(Devoir devoir) {
        this.devoir = devoir;
        if (devoir != null) {
            List<SoumissionDevoir> soumissions = soumissionService.recupererParDevoirId(devoir.getId());
            tableSoumissions.getItems().setAll(soumissions);
        }
    }

    public void rafraichirTable() {
        if (devoir != null) {
            chargerSoumissionsParDevoir(devoir);
        } else {
            chargerSoumissions();
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
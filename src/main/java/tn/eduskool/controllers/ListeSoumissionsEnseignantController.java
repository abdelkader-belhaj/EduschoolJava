package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import tn.eduskool.entities.SoumissionDevoir;
import tn.eduskool.entities.Devoir;
import tn.eduskool.services.SoumissionDevoirService;

import java.util.List;

public class ListeSoumissionsEnseignantController {

    @FXML
    private TableView<SoumissionDevoir> tableSoumissions;

    @FXML
    private TableColumn<SoumissionDevoir, String> colDate;

    @FXML
    private TableColumn<SoumissionDevoir, String> colFichier;

    @FXML
    private TableColumn<SoumissionDevoir, Integer> colNote;

    private final SoumissionDevoirService soumissionService = new SoumissionDevoirService();
    private Devoir devoir;

    @FXML
    public void initialize() {
        // Activer l'édition pour la TableView
        tableSoumissions.setEditable(true);

        // Initialisation des colonnes
        colDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDateSoumission().toString()));

        colFichier.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFichier()));

        // Configuration de la colonne Note
        colNote.setCellValueFactory(cellData -> {
            Integer note = cellData.getValue().getNote();
            return new SimpleIntegerProperty(note != null ? note : 0).asObject();
        });

        // Factory pour l'édition
        colNote.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter() {
            @Override
            public Integer fromString(String value) {
                try {
                    return value.isEmpty() ? null : Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }));

        // Gestion de la modification
        colNote.setOnEditCommit(event -> {
            SoumissionDevoir soumission = event.getRowValue();
            Integer newNote = event.getNewValue();

            // Validation de la note
            if (newNote != null && (newNote < 0 || newNote > 20)) {
                System.out.println("Note invalide: doit être entre 0 et 20");
                return;
            }

            // Mise à jour
            soumission.setNote(newNote);
            soumissionService.attribuerNote(soumission.getId(), newNote);

            // Rafraîchir la table
            tableSoumissions.refresh();
        });
    }

    public void initData(Devoir devoir) {
        this.devoir = devoir;
        chargerSoumissionsParDevoir();
    }

    private void chargerSoumissionsParDevoir() {
        List<SoumissionDevoir> soumissions = soumissionService.recupererParDevoirId(devoir.getId());
        tableSoumissions.getItems().setAll(soumissions);
    }
}
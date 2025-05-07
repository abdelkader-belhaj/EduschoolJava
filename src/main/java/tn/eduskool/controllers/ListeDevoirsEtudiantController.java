package tn.eduskool.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import tn.eduskool.entities.Devoir;
import tn.eduskool.entities.Utilisateur;
import tn.eduskool.services.DevoirService;
import java.util.List;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import tn.eduskool.services.SoumissionDevoirService;
import tn.eduskool.entities.SoumissionDevoir;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.beans.binding.Bindings;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ListeDevoirsEtudiantController implements BaseController {
    @FXML
    private TableView<Devoir> devoirTable;
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
    private TextField searchField; // Ensure this matches the fx:id in the FXML file
    @FXML
    private Button btnNotifications; // Ensure this matches the fx:id in the FXML file

    private final DevoirService devoirService = new DevoirService();
    private Utilisateur utilisateur;
    private final SoumissionDevoirService soumissionService = new SoumissionDevoirService();
    private final Gson gson = new Gson();
    private static final String TRANSLATE_SERVICE_URL = "http://127.0.0.1:5000/translate";
    private List<Devoir> devoirsProches;

    @FXML
    public void initialize() {
        configurerColonnes();
        chargerDevoirs();
        verifierDevoirsProches();
        ajouterRecherche(); // This method uses searchField
        ajouterTraduction();
    }

    private void configurerColonnes() {
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateLimiteCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedDate()));
        fichierCol.setCellValueFactory(new PropertyValueFactory<>("fichier"));

        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button downloadBtn = new Button("Télécharger");
            private final Button submitBtn = new Button("Soumettre");
            private final HBox box = new HBox(5, downloadBtn, submitBtn);

            {
                downloadBtn.setOnAction(e -> telechargerDevoir(getTableView().getItems().get(getIndex())));
                submitBtn.setOnAction(e -> soumettreSolution(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void ajouterRecherche() {
        ObservableList<Devoir> observableDevoirs = FXCollections.observableArrayList(devoirService.recuperer());
        FilteredList<Devoir> filteredData = new FilteredList<>(observableDevoirs, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(devoir -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return devoir.getTitre().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Devoir> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(devoirTable.comparatorProperty());
        devoirTable.setItems(sortedData);
    }

    private void ajouterTraduction() {
        devoirTable.setRowFactory(tv -> {
            TableRow<Devoir> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem traduireFrItem = new MenuItem("Traduire en Français");
            traduireFrItem.setOnAction(event -> {
                Devoir devoir = row.getItem();
                if (devoir != null) {
                    String texteTraduit = traduireDescription(devoir.getDescription(), "fr");
                    if (!texteTraduit.equals(devoir.getDescription())) {
                        devoir.setDescription(texteTraduit);
                        devoirTable.refresh();
                    }
                }
            });

            MenuItem traduireEnItem = new MenuItem("Traduire en Anglais");
            traduireEnItem.setOnAction(event -> {
                Devoir devoir = row.getItem();
                if (devoir != null) {
                    String texteTraduit = traduireDescription(devoir.getDescription(), "en");
                    if (!texteTraduit.equals(devoir.getDescription())) {
                        devoir.setDescription(texteTraduit);
                        devoirTable.refresh();
                    }
                }
            });

            contextMenu.getItems().addAll(traduireFrItem, traduireEnItem);

            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );

            return row;
        });
    }

    private String traduireDescription(String texteOriginal, String langueCible) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("text", texteOriginal);
            requestBody.addProperty("lang", langueCible);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TRANSLATE_SERVICE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
                return jsonResponse.get("translated_text").getAsString();
            } else {
                showAlert("Erreur de traduction", "Impossible de traduire le texte. Code d'erreur: " + response.statusCode());
                return texteOriginal;
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur de traduction", "Le service de traduction est indisponible.");
            return texteOriginal;
        }
    }

    private void verifierDevoirsProches() {
        devoirsProches = devoirService.recuperer();
    }

    @FXML
    private void afficherNotifications() {
        try {
            ContextMenu contextMenu = new ContextMenu();

            LocalDate today = LocalDate.now();
            for (Devoir devoir : devoirsProches) {
                if (devoir.getDatelimite() != null) {
                    long joursRestants = ChronoUnit.DAYS.between(today, devoir.getDatelimite());

                    if (joursRestants >= 0 && joursRestants <= 2) {
                        MenuItem item = new MenuItem(devoir.getTitre() + " (échéance dans " +
                                (joursRestants == 0 ? "aujourd'hui" : joursRestants + " jours") + ")");
                        contextMenu.getItems().add(item);
                    }
                }
            }

            if (contextMenu.getItems().isEmpty()) {
                MenuItem noNotif = new MenuItem("Aucune échéance proche");
                contextMenu.getItems().add(noNotif);
            }

            contextMenu.show(btnNotifications, javafx.geometry.Side.BOTTOM, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'affichage des notifications: " + e.getMessage());
        }
    }

    @Override
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        chargerDevoirs();
    }

    public void chargerDevoirs() {
        try {
            // Debugging: Log the start of the method
            System.out.println("Chargement des devoirs...");

            // Fetch the list of assignments
            List<Devoir> devoirs = devoirService.recuperer();

            // Check if the list is null or empty
            if (devoirs == null) {
                System.out.println("Erreur: La liste des devoirs est null.");
                showAlert("Erreur", "Impossible de charger les devoirs. La liste est vide.");
                return;
            }

            if (devoirs.isEmpty()) {
                System.out.println("Information: Aucun devoir disponible.");
                showAlert("Information", "Aucun devoir disponible.");
                return;
            }

            // Debugging: Log the size of the retrieved list
            System.out.println("Nombre de devoirs récupérés: " + devoirs.size());

            // Populate the table
            devoirTable.getItems().setAll(devoirs);
        } catch (Exception e) {
            // Debugging: Print the stack trace for the exception
            e.printStackTrace();

            // Show an error alert to the user
            showAlert("Erreur", "Impossible de charger les devoirs. Détails: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    private void telechargerDevoir(Devoir devoir) {
        try {
            File sourceFile = new File("uploads/devoirs/" + devoir.getFichier());
            if (!sourceFile.exists()) {
                showAlert("Erreur", "Le fichier n'existe pas");
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName(devoir.getFichier());
            File destFile = fileChooser.showSaveDialog(null);

            if (destFile != null) {
                Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                showAlert("Succès", "Fichier téléchargé avec succès");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du téléchargement: " + e.getMessage());
        }
    }

    private void soumettreSolution(Devoir devoir) {
        try {
            Path uploadsPath = Paths.get("uploads/soumissions");
            Files.createDirectories(uploadsPath);

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                String fileName = "soumission_" + devoir.getId() + "_" +
                        System.currentTimeMillis() + ".pdf";

                Path destinationPath = uploadsPath.resolve(fileName);
                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                SoumissionDevoir soumission = new SoumissionDevoir();
                soumission.setDevoir(devoir);
                soumission.setFichier(fileName);
                soumission.setDateSoumission(LocalDateTime.now());

                if (soumissionService.ajouter(soumission)) {
                    showAlert("Succès", "Solution soumise avec succès");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la manipulation des fichiers: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la soumission: " + e.getMessage());
        }
    }

    @FXML
    private void ouvrirListeSoumissions() {
        try {
            // Load the ListeSoumissions.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeSoumissions.fxml"));
            Parent root = loader.load();

            // Create a new stage for the ListeSoumissions view
            Stage stage = new Stage();
            stage.setTitle("Liste des Soumissions");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la liste des soumissions.");
        }
    }
}

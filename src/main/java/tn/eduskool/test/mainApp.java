package tn.eduskool.test;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class mainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login_view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Charger tous les fichiers CSS nécessaires
            scene.getStylesheets().addAll(
                    getClass().getResource("/activities.css").toExternalForm(),
                    getClass().getResource("/front.css").toExternalForm(),
                    getClass().getResource("/dashboard.css").toExternalForm(),
                    getClass().getResource("/style.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle("EduSkool");
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

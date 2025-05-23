package tn.eduskool.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFx extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // 👇 Change juste le chemin FXML ici
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StatistiqueDevoirs.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Soumission des Devoirs");
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Erreur de chargement: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
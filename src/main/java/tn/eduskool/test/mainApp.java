package tn.eduskool.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.eduskool.views.ActivityView;

public class mainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        ActivityView activityView = new ActivityView();
        Scene scene = activityView.createScene(primaryStage);

        primaryStage.setTitle("Modern Activity Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

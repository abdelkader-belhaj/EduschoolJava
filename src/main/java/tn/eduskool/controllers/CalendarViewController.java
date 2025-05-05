package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import tn.eduskool.services.ServiceActivity;
import java.util.List;
import tn.eduskool.entities.Activity;
import java.util.stream.Collectors;

public class CalendarViewController {

    @FXML
    private WebView webView;

    @FXML
    public void initialize() {
        WebEngine engine = webView.getEngine();
        String htmlContent = getClass().getResource("/web/calendar.html").toExternalForm();
        engine.load(htmlContent);

        engine.getLoadWorker().stateProperty().addListener((obs, old, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                loadActivities();
            }
        });
    }

    private void loadActivities() {
        List<Activity> activities = ServiceActivity.getAllActivities();
        String eventsJson = convertActivitiesToJson(activities);
        webView.getEngine().executeScript("updateEvents(" + eventsJson + ")");
    }

    private String convertActivitiesToJson(List<Activity> activities) {
        return activities.stream()
                .map(a -> String.format(
                        "{\"title\":\"%s\",\"start\":\"%s\",\"description\":\"%s\"}",
                        escapeJsonString(a.getTitre()),
                        a.getDate().toString(),
                        escapeJsonString(a.getDescription())))
                .collect(Collectors.joining(",", "[", "]"));
    }

    private String escapeJsonString(String input) {
        if (input == null)
            return "";
        return input.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}

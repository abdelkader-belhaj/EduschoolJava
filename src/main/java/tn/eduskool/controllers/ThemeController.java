package tn.eduskool.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import tn.eduskool.entities.Theme;
import tn.eduskool.services.ThemeService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ThemeController implements Initializable {

    @FXML
    private ListView<String> listViewThemes;

    private ThemeService themeService = new ThemeService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            for (Theme theme : themeService.recuperer()) {
                listViewThemes.getItems().add(theme.getTitre());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

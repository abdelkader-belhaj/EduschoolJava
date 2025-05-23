package tn.eduskool.services;

import tn.eduskool.tools.DatabaseConnection;
import tn.eduskool.entities.Theme;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThemeService implements IServices<Theme> {
    private Connection connect;

    public ThemeService() {
        connect = DatabaseConnection.connect();
    }
    @Override
    public void ajouter(Theme theme) throws SQLException {
        String sql = "INSERT INTO theme(titre) VALUES (?)";
        PreparedStatement ps = connect.prepareStatement(sql);
        ps.setString(1, theme.getTitre());

        ps.executeUpdate();
        System.out.println("Theme ajouté");

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM theme WHERE id = ?";
        PreparedStatement ps = connect.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Theme supprimé");



    }

    @Override
    public void modifier(Theme theme) throws SQLException {
        String sql = "UPDATE theme SET titre = '" + theme.getTitre() + "' WHERE id = " + theme.getId();
        Statement ste = connect.createStatement();
        ste.executeUpdate(sql); // Exécuter la mise à jour
        System.out.println("Thème modifié");
    }


    @Override
    public List<Theme> recuperer() throws SQLException {
        String sql = "SELECT * FROM theme";
        Statement ste = connect.createStatement();
        ResultSet rs = ste.executeQuery(sql);
        List<Theme> themes = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String titre = rs.getString("titre");
            Theme theme = new Theme(id, titre);
            themes.add(theme);
        }
        return themes; // Corrigez ceci pour retourner 'themes' au lieu de 'null'
    }
}

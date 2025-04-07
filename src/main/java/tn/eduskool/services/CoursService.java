package tn.eduskool.services;

import tn.eduskool.database.DatabaseConnection;
import tn.eduskool.entities.Cours;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;


public class CoursService implements IServices<Cours> {

    private Connection conn;

    public CoursService() {
        conn = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public void ajouter(Cours cours) throws SQLException {
        String sql = "INSERT INTO cours(titre, date_heure, enseignant, theme) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, cours.getTitre());
        ps.setTimestamp(2, Timestamp.valueOf(cours.getDateHeure()));
        ps.setString(3, cours.getEnseignant());
        ps.setString(4, cours.getTheme());
        ps.executeUpdate();
        System.out.println("Cour ajouté");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM cours WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Cour supprimé");
    }

    @Override
    public void modifier(Cours cours) throws SQLException {
        // Créer la requête SQL en concaténant les valeurs directement
        String sql = "UPDATE cours SET  titre = '" + cours.getTitre() + "'" +
                ", enseignant = '" + cours.getEnseignant() + "'" +
                ", date_heure = '" + Timestamp.valueOf(cours.getDateHeure()) + "'" +
                ", theme = '" + cours.getTheme() + "'" +
                " WHERE id = " + cours.getId();

        // Créer un Statement pour exécuter la requête
        Statement st = conn.createStatement();
        st.executeUpdate(sql); // Exécuter la mise à jour
        System.out.println("Cours modifié");
    }






    @Override
    public List<Cours> recuperer() throws SQLException {
        String sql = "SELECT * FROM cours";
        Statement st = conn.createStatement();
        ResultSet rs = conn.createStatement().executeQuery(sql);
        List<Cours> listCours = new ArrayList<Cours>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String titre = rs.getString("titre");
            LocalDate dateHeure = rs.getTimestamp("date_heure").toLocalDateTime().toLocalDate();
            String enseignant = rs.getString("enseignant");
            String theme = rs.getString("theme");
            Cours cours = new Cours(id,titre, dateHeure.atStartOfDay(),enseignant,theme);
            listCours.add(cours);
        }

        return null;
    }}


package tn.eduskool.services;

import tn.eduskool.entities.Theme;
import tn.eduskool.tools.DatabaseConnection;
import tn.eduskool.entities.Cours;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CoursService implements IServices<Cours> {

    private Connection  connect;

    public CoursService() {
        connect = DatabaseConnection.connect();
    }

    @Override
    public void ajouter(Cours cours) throws SQLException {
        String sql = "INSERT INTO cours(titre, date_heure, enseignant, theme, fichier) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps =  connect.prepareStatement(sql);
        ps.setString(1, cours.getTitre());
        ps.setTimestamp(2, Timestamp.valueOf(cours.getDateHeure()));
        ps.setString(3, cours.getEnseignant());
        ps.setInt(4, cours.getTheme().getId());
        ps.setString(5, cours.getFichier()); // Ajout du champ fichier
        ps.executeUpdate();
        System.out.println("Cours ajouté");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM cours WHERE id = ?";
        PreparedStatement ps =  connect.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Cours supprimé");
    }

    @Override
    public void modifier(Cours cours) throws SQLException {
        String sql = "UPDATE cours SET titre = ?, enseignant = ?, date_heure = ?, theme = ?, fichier = ? WHERE id = ?";
        PreparedStatement ps =  connect.prepareStatement(sql);
        ps.setString(1, cours.getTitre());
        ps.setString(2, cours.getEnseignant());
        ps.setTimestamp(3, Timestamp.valueOf(cours.getDateHeure()));
        ps.setInt(4, cours.getTheme().getId());
        ps.setString(5, cours.getFichier()); // Ajout du champ fichier
        ps.setInt(6, cours.getId());
        ps.executeUpdate();
        System.out.println("Cours modifié");
    }

    @Override
    public List<Cours> recuperer() throws SQLException {
        String sql = "SELECT c.id, c.titre, c.date_heure, c.enseignant, c.theme, c.fichier, t.titre AS theme_nom " +
                "FROM cours c JOIN theme t ON c.theme = t.id";
        Statement st =  connect.createStatement();
        ResultSet rs = st.executeQuery(sql);
        List<Cours> listCours = new ArrayList<>();

        while (rs.next()) {
            int id = rs.getInt("id");
            String titre = rs.getString("titre");
            LocalDateTime dateHeure = rs.getTimestamp("date_heure").toLocalDateTime();
            String enseignant = rs.getString("enseignant");
            String fichier = rs.getString("fichier"); // Récupération du champ fichier

            Theme theme = new Theme(rs.getInt("theme"), rs.getString("theme_nom"));
            Cours cours = new Cours(id, titre, dateHeure, enseignant, theme, fichier);
            listCours.add(cours);
        }

        return listCours;
    }
}
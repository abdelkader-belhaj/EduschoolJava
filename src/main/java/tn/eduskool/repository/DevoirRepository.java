package tn.eduskool.repository;

import tn.eduskool.entities.Devoir;
import tn.eduskool.tools.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DevoirRepository {

    public void save(Devoir devoir) throws SQLException {
        String query = "INSERT INTO devoirs (titre, description, datelimite, fichier, idEnseignant) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, devoir.getTitre());
            stmt.setString(2, devoir.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(devoir.getDateLimite()));
            stmt.setString(4, devoir.getFichier());
            stmt.setInt(5, devoir.getIdEnseignant());
            stmt.executeUpdate();
        }
    }

    public void update(Devoir devoir) throws SQLException {
        String query = "UPDATE devoirs SET titre=?, description=?, datelimite=?, fichier=? WHERE id=?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, devoir.getTitre());
            stmt.setString(2, devoir.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(devoir.getDateLimite()));
            stmt.setString(4, devoir.getFichier());
            stmt.setInt(5, devoir.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM devoirs WHERE id=?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Devoir> findAll() throws SQLException {
        List<Devoir> devoirs = new ArrayList<>();
        String query = "SELECT * FROM devoirs";
        try (Connection conn = DatabaseConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                devoirs.add(mapResultSetToDevoir(rs));
            }
        }
        return devoirs;
    }

    public List<Devoir> findByEnseignantId(int enseignantId) throws SQLException {
        List<Devoir> devoirs = new ArrayList<>();
        String query = "SELECT * FROM devoirs WHERE idEnseignant = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, enseignantId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                devoirs.add(mapResultSetToDevoir(rs));
            }
        }
        return devoirs;
    }

    private Devoir mapResultSetToDevoir(ResultSet rs) throws SQLException {
        Devoir devoir = new Devoir();
        devoir.setId(rs.getInt("id"));
        devoir.setTitre(rs.getString("titre"));
        devoir.setDescription(rs.getString("description"));
        devoir.setDateLimite(rs.getTimestamp("datelimite").toLocalDateTime());
        devoir.setFichier(rs.getString("fichier"));
        devoir.setIdEnseignant(rs.getInt("idEnseignant"));
        return devoir;
    }
}

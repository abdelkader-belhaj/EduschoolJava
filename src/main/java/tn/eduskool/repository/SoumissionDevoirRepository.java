package tn.eduskool.repository;

import tn.eduskool.entities.SoumissionDevoir;
import tn.eduskool.tools.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SoumissionDevoirRepository {

    public List<SoumissionDevoir> findByDevoirId(int devoirId) {
        List<SoumissionDevoir> soumissions = new ArrayList<>();
        String query = "SELECT * FROM soumissiondevoir WHERE devoir_id = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, devoirId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                soumissions.add(mapResultSetToSoumission(rs));
            }
            return soumissions;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des soumissions", e);
        }
    }

    private SoumissionDevoir mapResultSetToSoumission(ResultSet rs) throws SQLException {
        SoumissionDevoir soumission = new SoumissionDevoir();
        soumission.setId(rs.getInt("id"));
        soumission.setDateSoumission(rs.getTimestamp("dateSoumission").toLocalDateTime());
        soumission.setFichier(rs.getString("fichier"));
        soumission.setNote(rs.getObject("note", Integer.class));
        soumission.setDevoirId(rs.getInt("devoir_id"));
        return soumission;
    }

    public void save(SoumissionDevoir soumission) throws SQLException {
        String query = soumission.getId() == 0
                ? "INSERT INTO soumissiondevoir (dateSoumission, fichier, note, devoir_id) VALUES (?, ?, ?, ?)"
                : "UPDATE soumissiondevoir SET dateSoumission=?, fichier=?, note=?, devoir_id=? WHERE id=?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(soumission.getDateSoumission()));
            stmt.setString(2, soumission.getFichier());
            if (soumission.getNote() != null) {
                stmt.setInt(3, soumission.getNote());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setInt(4, soumission.getDevoirId());
            if (soumission.getId() != 0) {
                stmt.setInt(5, soumission.getId());
            }
            stmt.executeUpdate();
        }
    }

    public Optional<SoumissionDevoir> findById(int id) throws SQLException {
        String query = "SELECT * FROM soumissiondevoir WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToSoumission(rs));
            }
            return Optional.empty();
        }
    }

    public void deleteById(int id) throws SQLException {
        String query = "DELETE FROM soumissiondevoir WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<SoumissionDevoir> findByEtudiantId(int etudiantId) throws SQLException {
        List<SoumissionDevoir> result = new ArrayList<>();
        String query = "SELECT * FROM soumissiondevoir WHERE etudiant_id = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, etudiantId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(mapResultSetToSoumission(rs));
            }
        }
        return result;
    }

    public List<SoumissionDevoir> findAll() throws SQLException {
        List<SoumissionDevoir> soumissions = new ArrayList<>();
        String query = "SELECT * FROM soumissiondevoir";
        try (Connection conn = DatabaseConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                soumissions.add(mapResultSetToSoumission(rs));
            }
        }
        return soumissions;
    }

    public void attribuerNote(int soumissionId, Integer note) throws SQLException {
        String query = "UPDATE soumissiondevoir SET note = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            if (note != null) {
                stmt.setInt(1, note);
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setInt(2, soumissionId);
            stmt.executeUpdate();
        }
    }
}

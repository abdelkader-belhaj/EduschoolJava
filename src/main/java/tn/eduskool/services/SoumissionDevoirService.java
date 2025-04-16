package tn.eduskool.services;

import tn.eduskool.entities.Devoir;
import tn.eduskool.entities.SoumissionDevoir;
import tn.eduskool.tools.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SoumissionDevoirService implements IService<SoumissionDevoir> {

    private Connection cnx;

    public SoumissionDevoirService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    @Override
    public boolean ajouter(SoumissionDevoir soumissionDevoir) {
        String query = "INSERT INTO soumissiondevoir (dateSoumission, fichier, note, devoir_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, Timestamp.valueOf(soumissionDevoir.getDateSoumission()));
            stmt.setString(2, soumissionDevoir.getFichier());
            if (soumissionDevoir.getNote() != null) {
                stmt.setInt(3, soumissionDevoir.getNote());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setInt(4, soumissionDevoir.getDevoir().getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                soumissionDevoir.setId(rs.getInt(1));
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean supprimer(int id) {
        String query = "DELETE FROM soumissiondevoir WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modifier(SoumissionDevoir soumissionDevoir) {
        String query = "UPDATE soumissiondevoir SET dateSoumission = ?, fichier = ?, note = ?, devoir_id = ? WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(soumissionDevoir.getDateSoumission()));
            stmt.setString(2, soumissionDevoir.getFichier());
            if (soumissionDevoir.getNote() != null) {
                stmt.setInt(3, soumissionDevoir.getNote());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setInt(4, soumissionDevoir.getDevoir().getId());
            stmt.setInt(5, soumissionDevoir.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modifier(SoumissionDevoir soumissionDevoir, String ancienFichier) {
        return modifier(soumissionDevoir);
    }

    @Override
    public List<SoumissionDevoir> recuperer() {
        List<SoumissionDevoir> soumissions = new ArrayList<>();
        String query = "SELECT * FROM soumissiondevoir";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            DevoirService devoirService = new DevoirService();

            while (rs.next()) {
                int devoirId = rs.getInt("devoir_id");
                Devoir devoir = devoirService.getById(devoirId);

                SoumissionDevoir soumission = new SoumissionDevoir(
                        rs.getInt("id"),
                        rs.getTimestamp("dateSoumission").toLocalDateTime(),
                        rs.getString("fichier"),
                        rs.getObject("note") != null ? rs.getInt("note") : null,
                        devoir
                );
                soumissions.add(soumission);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return soumissions;
    }

    public List<SoumissionDevoir> recupererParDevoirId(int devoirId) {
        List<SoumissionDevoir> soumissions = new ArrayList<>();
        String query = "SELECT * FROM soumissiondevoir WHERE devoir_id = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, devoirId);
            ResultSet rs = stmt.executeQuery();

            DevoirService devoirService = new DevoirService();

            while (rs.next()) {
                SoumissionDevoir s = new SoumissionDevoir();
                s.setId(rs.getInt("id"));
                s.setDateSoumission(rs.getTimestamp("dateSoumission").toLocalDateTime());
                s.setFichier(rs.getString("fichier"));

                // Meilleure gestion des valeurs NULL
                int note = rs.getInt("note");
                s.setNote(rs.wasNull() ? null : note);

                // Chargement du devoir
                Devoir devoir = devoirService.getById(rs.getInt("devoir_id"));
                s.setDevoir(devoir);

                soumissions.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return soumissions;
    }

    public void attribuerNote(int soumissionId, Integer note) {
        // Validation de la note
        if (note != null && (note < 0 || note > 20)) {
            System.out.println("La note doit être entre 0 et 20 ou null");
            return;
        }

        String sql = "UPDATE soumissiondevoir SET note = ? WHERE id = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            if (note != null) {
                stmt.setInt(1, note);
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setInt(2, soumissionId);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Note mise à jour avec succès pour ID: " + soumissionId);
            } else {
                System.out.println("Aucune soumission trouvée avec ID: " + soumissionId);
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la mise à jour de la note: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
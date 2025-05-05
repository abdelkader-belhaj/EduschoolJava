package tn.eduskool.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import tn.eduskool.entities.Commentaire;
import tn.eduskool.entities.Utilisateur;
import tn.eduskool.tools.DatabaseConnection;

public class ServiceCommentaire {

    // Récupérer les commentaires par activity_id
    public static List<Commentaire> getCommentairesByActivityId(int activityId) {
        List<Commentaire> list = new ArrayList<>();
        String sql = "SELECT c.*, u.nom, u.prenom FROM commentaire c " +
                "LEFT JOIN utilisateur u ON c.user_id = u.id_utilisateur " +
                "WHERE c.activity_id = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, activityId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Utilisateur user = null;
                if (rs.getString("nom") != null) {
                    user = new Utilisateur();
                    user.setNom(rs.getString("nom"));
                    user.setPrenom(rs.getString("prenom"));
                }

                Commentaire c = new Commentaire(
                        rs.getInt("id"),
                        rs.getInt("activity_id"),
                        rs.getString("contenu"),
                        rs.getInt("note"),
                        user);
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Créer un commentaire et retourner son ID généré
    public static int createCommentaireReturnId(int activityId, String contenu, int note) {
        int generatedId = -1;
        String sql = "INSERT INTO commentaire (activity_id, contenu, note) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, activityId);
            stmt.setString(2, contenu);
            stmt.setInt(3, note);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    // Récupérer tous les commentaires
    public static List<Commentaire> getAllCommentaires() {
        List<Commentaire> list = new ArrayList<>();
        String sql = "SELECT * FROM commentaire";
        try (Connection conn = DatabaseConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Commentaire c = new Commentaire(
                        rs.getInt("id"),
                        rs.getInt("activity_id"),
                        rs.getString("contenu"),
                        rs.getInt("note"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Récupérer un commentaire par son ID
    public static Commentaire getCommentaireById(int id) {
        String sql = "SELECT * FROM commentaire WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Commentaire(
                        rs.getInt("id"),
                        rs.getInt("activity_id"),
                        rs.getString("contenu"),
                        rs.getInt("note"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Aucun commentaire trouvé
    }

    // Mettre à jour un commentaire
    public static boolean updateCommentaire(int id, int newActivityId, String newContenu, int newNote) {
        String sql = "UPDATE commentaire SET activity_id = ?, contenu = ?, note = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newActivityId);
            stmt.setString(2, newContenu);
            stmt.setInt(3, newNote);
            stmt.setInt(4, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Supprimer un commentaire
    public static boolean deleteCommentaire(int id) {
        String sql = "DELETE FROM commentaire WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

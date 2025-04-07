package tn.eduskool.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import tn.eduskool.entities.Activity;
import tn.eduskool.tools.DatabaseConnection;

public class ServiceActivity {

    // Créer une activité
    public static boolean createActivity(String titre, String description, Date date, String imageFileName,
            boolean isApproved, String typesActivity, Date createdAt) {
        String sql = "INSERT INTO activity (titre, description, date, image_file_name, is_approved, types_activity, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) { // stmt : C'est une instance de
                                                                       // PreparedStatement, qui est utilisée pour
                                                                       // exécuter la requête SQL.
            stmt.setString(1, titre);
            stmt.setString(2, description);
            stmt.setTimestamp(3, new Timestamp(date.getTime()));
            stmt.setString(4, imageFileName);
            stmt.setBoolean(5, isApproved);
            stmt.setString(6, typesActivity);
            stmt.setTimestamp(7, new Timestamp(createdAt.getTime()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Récupérer toutes les activités
    public static List<Activity> getAllActivities() {
        List<Activity> list = new ArrayList<>();
        String sql = "SELECT * FROM activity";
        try (Connection conn = DatabaseConnection.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Activity a = new Activity(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getTimestamp("date"),
                        rs.getString("image_file_name"),
                        rs.getBoolean("is_approved"),
                        rs.getString("types_activity"),
                        rs.getTimestamp("created_at"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Mettre à jour une activité
    public static boolean updateActivity(int id, String newTitre, String newDescription, Date newDate,
            String newImageFileName, boolean newIsApproved, String newTypesActivity) {
        String sql = "UPDATE activity SET titre = ?, description = ?, date = ?, image_file_name = ?, is_approved = ?, types_activity = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newTitre);
            stmt.setString(2, newDescription);
            stmt.setTimestamp(3, new Timestamp(newDate.getTime()));
            stmt.setString(4, newImageFileName);
            stmt.setBoolean(5, newIsApproved);
            stmt.setString(6, newTypesActivity);
            stmt.setInt(7, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Supprimer une activité
    public static boolean deleteActivity(int id) {
        String sql = "DELETE FROM activity WHERE id = ?";
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

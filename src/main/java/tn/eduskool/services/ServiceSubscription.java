package tn.eduskool.services;
import tn.eduskool.entities.Subscription;
import tn.eduskool.tools.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ServiceSubscription {
    // Créer une souscription
    public static boolean createSubscription(Date startDate, Date endDate, String status) {
        String sql = "INSERT INTO subscription (startDate, endDate, status) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(startDate.getTime()));
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));
            stmt.setString(3, status);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lire toutes les souscriptions
    public static List<Subscription> getAllSubscriptions() {
        List<Subscription> list = new ArrayList<>();
        String sql = "SELECT * FROM subscription";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Subscription sub = new Subscription(
                        rs.getInt("id"),
                        rs.getDate("startDate"),
                        rs.getDate("endDate"),
                        rs.getString("status"));
                list.add(sub);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Mettre à jour une souscription
    public static boolean updateSubscription(int id, Date newStartDate, Date newEndDate, String newStatus) {
        String sql = "UPDATE subscription SET startDate = ?, endDate = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(newStartDate.getTime()));
            stmt.setDate(2, new java.sql.Date(newEndDate.getTime()));
            stmt.setString(3, newStatus);
            stmt.setInt(4, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Supprimer une souscription
    public static boolean deleteSubscription(int id) {
        String sql = "DELETE FROM subscription WHERE id = ?";
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

package tn.eduskool.services;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import tn.eduskool.entities.Pack;
import tn.eduskool.tools.DatabaseConnection;
public class ServicePack {
    // Créer un pack
    public static boolean createPack(String name, float price, int duration, String description) {
        String sql = "INSERT INTO pack (name, price, duration, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setFloat(2, price);
            stmt.setInt(3, duration);
            stmt.setString(4, description);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Récupérer tous les packs
    public static List<Pack> getAllPacks() {
        List<Pack> list = new ArrayList<>();
        String sql = "SELECT * FROM pack";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pack p = new Pack(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getFloat("price"),
                        rs.getInt("duration"),
                        rs.getString("description"));
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Mettre à jour un pack
    public static boolean updatePack(int id, String newName, float newPrice, int newDuration, String newDescription) {
        String sql = "UPDATE pack SET name = ?, price = ?, duration = ?, description = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setFloat(2, newPrice);
            stmt.setInt(3, newDuration);
            stmt.setString(4, newDescription);
            stmt.setInt(5, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Supprimer un pack
    public static boolean deletePack(int id) {
        String sql = "DELETE FROM pack WHERE id = ?";
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

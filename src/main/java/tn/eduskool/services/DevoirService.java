package tn.eduskool.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.eduskool.tools.DatabaseConnection;
import tn.eduskool.entities.Devoir;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class DevoirService implements IService<Devoir> {

    private Connection cnx;

    public DevoirService() {
        cnx = DatabaseConnection.getInstance().getCnx();
    }

    public boolean ajouter(Devoir devoir) {
        String query = "INSERT INTO devoirs (titre, description, datelimite, fichier) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, devoir.getTitre());
            stmt.setString(2, devoir.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(devoir.getDatelimite()));
            stmt.setString(4, devoir.getFichier());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getFileNameById(int id) {
        String query = "SELECT fichier FROM devoirs WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("fichier");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean supprimer(int id) {
        String fileName = getFileNameById(id);

        String query = "DELETE FROM devoirs WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, id);
            boolean success = stmt.executeUpdate() > 0;

            if (success && fileName != null) {
                try {
                    Path filePath = Paths.get("uploads/devoirs/" + fileName);
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    System.err.println("Erreur suppression fichier: " + e.getMessage());
                }
            }
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modifier(Devoir devoir, String ancienFichier) {
        String query = "UPDATE devoirs SET titre = ?, description = ?, datelimite = ?, fichier = ? WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, devoir.getTitre());
            stmt.setString(2, devoir.getDescription());
            stmt.setTimestamp(3, Timestamp.valueOf(devoir.getDatelimite()));
            stmt.setString(4, devoir.getFichier());
            stmt.setInt(5, devoir.getId());

            boolean success = stmt.executeUpdate() > 0;

            if (success && ancienFichier != null && !ancienFichier.equals(devoir.getFichier())) {
                try {
                    Path oldFilePath = Paths.get("uploads/devoirs/" + ancienFichier);
                    Files.deleteIfExists(oldFilePath);
                } catch (IOException e) {
                    System.err.println("Erreur suppression ancien fichier: " + e.getMessage());
                }
            }
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<Devoir> recupererObservable() {
        return FXCollections.observableArrayList(this.recuperer());
    }

    public List<Devoir> recuperer() {
        List<Devoir> devoirs = new ArrayList<>();
        String query = "SELECT * FROM devoirs";

        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Devoir devoir = new Devoir(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getTimestamp("datelimite").toLocalDateTime(),
                        rs.getString("fichier")
                );
                devoirs.add(devoir);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération: " + e.getMessage());
        }

        return devoirs;
    }
    @Override
    public boolean modifier(Devoir devoir) {
        return modifier(devoir, null); // ou selon ton cas, tu peux même gérer différemment
    }
    public Devoir getById(int id) {
        Devoir devoir = null;
        String query = "SELECT * FROM devoirs WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                devoir = new Devoir(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getTimestamp("datelimite").toLocalDateTime(),
                        rs.getString("fichier")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return devoir;
    }

}
package tn.eduskool.services;

import tn.eduskool.entities.Utilisateur;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class UtilisateurService {
    private Connection connection;

    public UtilisateurService(Connection connection) {
        this.connection = connection;
    }

    public int inscrireEtudiant(Utilisateur utilisateur) {
        return inscrireUtilisateur(utilisateur, Utilisateur.TypeUtilisateur.ETUDIANT);
    }

    public int inscrireEnseignant(Utilisateur utilisateur) {
        return inscrireUtilisateur(utilisateur, Utilisateur.TypeUtilisateur.ENSEIGNANT);
    }

    private int inscrireUtilisateur(Utilisateur utilisateur, Utilisateur.TypeUtilisateur type) {
        // Modifier la requête SQL pour inclure le bon nom de colonne pour l'ID
        String sql = "INSERT INTO utilisateur (nom, prenom, cin, email, mot_de_passe, adresse, date_naissance, telephone, photo, type_utilisateur) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, utilisateur.getNom());
            pstmt.setString(2, utilisateur.getPrenom());
            pstmt.setInt(3, utilisateur.getCin());
            pstmt.setString(4, utilisateur.getEmail());
            pstmt.setString(5, utilisateur.getMotDePasse());
            pstmt.setString(6, utilisateur.getAdresse());
            pstmt.setObject(7, utilisateur.getDateNaissance());
            pstmt.setInt(8, utilisateur.getTelephone());
            pstmt.setString(9, utilisateur.getPhoto());
            pstmt.setString(10, type.getValue()); // Utiliser getValue() au lieu de toString()

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during user registration: " + e.getMessage());
            e.printStackTrace(); // Ajouter pour plus de détails
        }

        return -1;
    }

    public boolean updatePhoto(int idUtilisateur, String photoPath) {
        // Corriger le nom de la colonne ID
        String sql = "UPDATE utilisateur SET photo = ? WHERE id_utilisateur = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, photoPath);
            pstmt.setInt(2, idUtilisateur);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la photo: " + e.getMessage());
            e.printStackTrace(); // Ajouter pour plus de détails
            return false;
        }
    }

    public boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
        }

        return false;
    }

    public boolean cinExiste(int cin) {
        String sql = "SELECT COUNT(*) FROM utilisateur WHERE cin = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, cin);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking CIN existence: " + e.getMessage());
        }

        return false;
    }
}
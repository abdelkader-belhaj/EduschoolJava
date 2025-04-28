package tn.eduskool.services;

import tn.eduskool.entities.Utilisateur;
import tn.eduskool.entities.Utilisateur.TypeUtilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LoginService {
    private Connection connection;

    public LoginService(Connection connection) {
        this.connection = connection;
    }

    public Utilisateur login(String email, String motDePasse, TypeUtilisateur roleAttendu) {
        String sql = "SELECT * FROM utilisateur WHERE email = ? AND mot_de_passe = ? AND type_utilisateur = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, motDePasse); // Dans un système réel, utilisez le hachage des mots de passe
            pstmt.setString(3, roleAttendu.getValue());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Création de l'utilisateur à partir des données récupérées
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setIdUtilisateur(rs.getInt("id_utilisateur"));
                    utilisateur.setNom(rs.getString("nom"));
                    utilisateur.setPrenom(rs.getString("prenom"));
                    utilisateur.setCin(rs.getInt("cin"));
                    utilisateur.setEmail(rs.getString("email"));
                    utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
                    utilisateur.setAdresse(rs.getString("adresse"));
                    utilisateur.setDateNaissance(rs.getObject("date_naissance", LocalDate.class));
                    utilisateur.setTelephone(rs.getInt("telephone"));
                    utilisateur.setPhoto(rs.getString("photo"));
                    utilisateur.setTypeUtilisateur(TypeUtilisateur.fromValue(rs.getString("type_utilisateur")));
                    utilisateur.setVerified(rs.getBoolean("is_verified"));

                    // Vérifier si date_creation existe dans le ResultSet avant de l'utiliser
                    if (rs.getMetaData().getColumnCount() > 11) {
                        utilisateur.setDateCreation(rs.getObject("date_creation", LocalDateTime.class));
                    }

                    return utilisateur;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion: " + e.getMessage());
            e.printStackTrace();
        }

        return null; // Authentification échouée
    }
}
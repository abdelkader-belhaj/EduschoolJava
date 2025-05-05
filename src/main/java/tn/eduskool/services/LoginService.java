package tn.eduskool.services;

import tn.eduskool.entities.Utilisateur;
import tn.eduskool.entities.Utilisateur.TypeUtilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LoginService {
    private Connection connection;

    public LoginService(Connection connection) {
        this.connection = connection;
    }

    public Utilisateur login(String email, String password, TypeUtilisateur role) {
        try {
            // Après avoir récupéré l'utilisateur de la base de données
            Utilisateur user = null;
            String sql = "SELECT * FROM utilisateur WHERE email = ? AND mot_de_passe = ? AND type_utilisateur = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, email);
                pstmt.setString(2, password); // Dans un système réel, utilisez le hachage des mots de passe
                pstmt.setString(3, role.getValue());

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // Création de l'utilisateur à partir des données récupérées
                        user = new Utilisateur();
                        user.setIdUtilisateur(rs.getInt("id_utilisateur"));
                        user.setNom(rs.getString("nom"));
                        user.setPrenom(rs.getString("prenom"));
                        user.setCin(rs.getInt("cin"));
                        user.setEmail(rs.getString("email"));
                        user.setMotDePasse(rs.getString("mot_de_passe"));
                        user.setAdresse(rs.getString("adresse"));
                        user.setDateNaissance(rs.getObject("date_naissance", LocalDate.class));
                        user.setTelephone(rs.getInt("telephone"));
                        user.setPhoto(rs.getString("photo"));
                        user.setTypeUtilisateur(TypeUtilisateur.fromValue(rs.getString("type_utilisateur")));
                        user.setVerified(rs.getBoolean("is_verified"));

                        // Vérifier si date_creation existe dans le ResultSet avant de l'utiliser
                        if (rs.getMetaData().getColumnCount() > 11) {
                            user.setDateCreation(rs.getObject("date_creation", LocalDateTime.class));
                        }
                    }
                }
            }

            if (user != null) {
                // Définir explicitement le type d'utilisateur
                switch (role) {
                    case ADMIN:
                        user.setType_Utilisateur("admin");
                        break;
                    case ENSEIGNANT:
                        user.setType_Utilisateur("enseiagnt");
                        break;
                    case ETUDIANT:
                        user.setType_Utilisateur("etudiant");
                        break;
                }
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
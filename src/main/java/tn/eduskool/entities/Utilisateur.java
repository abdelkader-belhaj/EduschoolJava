package tn.eduskool.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Utilisateur {

    // Attributs correspondant aux colonnes de la table
    private int idUtilisateur;
    private String nom;
    private String prenom;
    private int cin;
    private String email;
    private String motDePasse;
    private String adresse;
    private LocalDate dateNaissance;
    private int telephone;
    private TypeUtilisateur typeUtilisateur;
    private boolean isVerified;
    private LocalDateTime dateCreation;
    private String photo;

    // Énumération pour les types d'utilisateur
    public enum TypeUtilisateur {
        ADMIN("admin"),
        ETUDIANT("etudiant"),
        ENSEIGNANT("enseiagnt"); // J'ai conservé la même orthographe que dans votre SQL

        private final String value;

        TypeUtilisateur(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static TypeUtilisateur fromValue(String value) {
            for (TypeUtilisateur type : values()) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Type d'utilisateur invalide: " + value);
        }
    }

    // Constructeur par défaut
    public Utilisateur() {
        this.isVerified = false;
        this.dateCreation = LocalDateTime.now();
    }

    // Constructeur avec paramètres
    public Utilisateur(String nom, String prenom, int cin, String email, String motDePasse,
                       String adresse, LocalDate dateNaissance, int telephone, TypeUtilisateur typeUtilisateur) {
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.email = email;
        this.motDePasse = motDePasse;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
        this.typeUtilisateur = typeUtilisateur;
        this.isVerified = false;
        this.dateCreation = LocalDateTime.now();
    }

    // Getters et Setters
    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public TypeUtilisateur getTypeUtilisateur() {
        return typeUtilisateur;
    }

    public void setTypeUtilisateur(TypeUtilisateur typeUtilisateur) {
        this.typeUtilisateur = typeUtilisateur;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "idUtilisateur=" + idUtilisateur +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", cin=" + cin +
                ", email='" + email + '\'' +
                ", adresse='" + adresse + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", telephone=" + telephone +
                ", typeUtilisateur=" + typeUtilisateur +
                ", isVerified=" + isVerified +
                ", dateCreation=" + dateCreation +
                '}';
    }
}
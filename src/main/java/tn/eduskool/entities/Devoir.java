package tn.eduskool.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Devoir {
    private int id;
    private String titre;
    private String description;
    private LocalDateTime dateLimite;
    private String fichier;
    private Integer idEnseignant;

    public Devoir() {
    }

    public Devoir(String titre, String description, LocalDateTime dateLimite, String fichier) {
        this.titre = titre;
        this.description = description;
        this.dateLimite = dateLimite;
        this.fichier = fichier;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(LocalDateTime datelimite) {
        this.dateLimite = datelimite;
    }

    // Alias methods pour la compatibilité avec la base de données
    public LocalDateTime getDatelimite() {
        return getDateLimite();
    }

    public void setDatelimite(LocalDateTime datelimite) {
        setDateLimite(datelimite);
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public Integer getIdEnseignant() {
        return idEnseignant;
    }

    public void setIdEnseignant(Integer idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    public String getFormattedDate() {
        return dateLimite != null ? dateLimite.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "";
    }
}

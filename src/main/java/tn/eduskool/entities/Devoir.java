package tn.eduskool.entities;

import java.time.LocalDateTime;
 Gestion-Lecon
import jakarta.persistence.*;

import java.util.List;

import java.time.format.DateTimeFormatter;
main

public class Devoir {
    private int id;
    private String titre;
    private String description;
Gestion-Lecon
    private LocalDateTime datelimite;
    private String fichier;

    @OneToMany(mappedBy = "devoir", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SoumissionDevoir> soumissions;

    // Getter & Setter pour soumissions
    public List<SoumissionDevoir> getSoumissions() {
        return soumissions;
    }

    public void setSoumissions(List<SoumissionDevoir> soumissions) {
        this.soumissions = soumissions;
    }

    // Constructeurs

    private LocalDateTime dateLimite;
    private String fichier;
    private Integer idEnseignant;
    main

    public Devoir() {
    }

 Gestion-Lecon
    public Devoir(String titre, String description, LocalDateTime datelimite, String fichier) {
        this.titre = titre;
        this.description = description;
        this.datelimite = datelimite;
        this.fichier = fichier;
    }

    public Devoir(int id, String titre, String description, LocalDateTime datelimite, String fichier) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.datelimite = datelimite;

    public Devoir(String titre, String description, LocalDateTime dateLimite, String fichier) {
        this.titre = titre;
        this.description = description;
        this.dateLimite = dateLimite;
 main
        this.fichier = fichier;
    }

    // Getters et Setters
Gestion-Lecon

main
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

 Gestion-Lecon
    public LocalDateTime getDatelimite() {
        return datelimite;
    }

    public void setDatelimite(LocalDateTime datelimite) {
        this.datelimite = datelimite;

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
 main
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

Gestion-Lecon
    // toString

    @Override
    public String toString() {
        return "Devoir{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", datelimite=" + datelimite +
                ", fichier='" + fichier + '\'' +
                '}';
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
    main
}

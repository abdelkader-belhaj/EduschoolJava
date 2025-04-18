package tn.eduskool.entities;

import java.time.LocalDateTime;

public class Devoir {
    private int id;
    private String titre;
    private String description;
    private LocalDateTime datelimite;
    private String fichier;

    // Constructeurs

    public Devoir() {
    }

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

    public LocalDateTime getDatelimite() {
        return datelimite;
    }

    public void setDatelimite(LocalDateTime datelimite) {
        this.datelimite = datelimite;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

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

}

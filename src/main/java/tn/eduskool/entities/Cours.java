package tn.eduskool.entities;

import java.time.LocalDateTime;

public class Cours {

    private int id;
    private String titre;
    private LocalDateTime date_heure;
    private String enseignant;
    private Theme theme;
    private String fichier; // Attribut renommé de pdfPath à fichier

    // Constructeur par défaut
    public Cours() {
    }

    // Constructeur avec tous les attributs, y compris fichier
    public Cours(int id, String titre, LocalDateTime date_heure, String enseignant, Theme theme, String fichier) {
        this.id = id;
        this.titre = titre;
        this.date_heure = date_heure;
        this.enseignant = enseignant;
        this.theme = theme;
        this.fichier = fichier;
    }

    // Constructeur sans id, avec fichier
    public Cours(String titre, LocalDateTime date_heure, String enseignant, Theme theme, String fichier) {
        this.titre = titre;
        this.date_heure = date_heure;
        this.enseignant = enseignant;
        this.theme = theme;
        this.fichier = fichier;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public LocalDateTime getDateHeure() {
        return date_heure;
    }

    public void setDateHeure(LocalDateTime date_heure) {
        this.date_heure = date_heure;
    }

    public String getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(String enseignant) {
        this.enseignant = enseignant;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", dateHeure=" + date_heure +
                ", enseignant='" + enseignant + '\'' +
                ", theme='" + theme + '\'' +
                ", fichier='" + fichier + '\'' +
                '}';
    }
}
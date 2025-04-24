package tn.eduskool.entities;

import java.time.LocalDateTime;

public class Cours {

    private int id;

    private String titre;

    private LocalDateTime dateHeure;

    private String enseignant;

    private Theme theme; // plus de relation, juste une chaîne de caractères

    // Constructeur par défaut
    public Cours() {
    }

    public Cours(int id, String titre, LocalDateTime dateHeure, String enseignant, Theme theme) {
        this.id = id;
        this.titre = titre;
        this.dateHeure = dateHeure;
        this.enseignant = enseignant;
        this.theme = theme;
    }

    public Cours(String titre, LocalDateTime dateHeure, String enseignant, Theme theme) {
        this.titre = titre;
        this.dateHeure = dateHeure;
        this.enseignant = enseignant;
        this.theme = theme;
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
        return dateHeure;
    }

    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure = dateHeure;
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

    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", dateHeure=" + dateHeure +
                ", enseignant='" + enseignant + '\'' +
                ", theme='" + theme + '\'' +
                '}';
    }

}

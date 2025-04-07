package tn.eduskool.entities;

import java.time.LocalDateTime;

public class Cours {

    private Long id;

    private String titre;

    private LocalDateTime dateHeure;

    private String enseignant;

    private String theme; // plus de relation, juste une chaîne de caractères

    // Constructeur par défaut
    public Cours() {
    }

    // Getters et Setters
    public Long getId() {
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

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}

package tn.eduskool.entities;

public class Theme {

Gestion-Lecon
    private int id;

    private Long id;
main

    private String titre;

    // Constructeur par défaut
    public Theme() {
    }

 Gestion-Lecon
    public Theme(int id, String titre) {
        this.id = id;
        this.titre = titre;
    }

    public Theme(String titre) {
        this.titre = titre;
    }

    // Getters et Setters
    public int getId() {

    // Getters et Setters
    public Long getId() {
 main
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    @Override
    public String toString() {
 Gestion-Lecon
        return "Theme{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                '}';

        return titre != null ? titre : "Unnamed Theme";
main
    }
}

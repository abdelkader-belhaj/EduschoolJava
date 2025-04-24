package tn.eduskool.entities;

public class Theme {

    private int id;

    private String titre;

    // Constructeur par défaut
    public Theme() {
    }

    public Theme(int id, String titre) {
        this.id = id;
        this.titre = titre;
    }

    public Theme(String titre) {
        this.titre = titre;
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

    @Override
    public String toString() {
        return "Theme{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                '}';
    }
}

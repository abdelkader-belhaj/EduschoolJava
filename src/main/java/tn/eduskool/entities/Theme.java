package tn.eduskool.entities;

public class Theme {

    private Long id;

    private String titre;

    // Constructeur par défaut
    public Theme() {
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

    @Override
    public String toString() {
        return titre != null ? titre : "Unnamed Theme";
    }
}

package tn.eduskool.entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Devoir {
    private int id;
    private String titre;
    private String description;
    private LocalDateTime datelimite;
    private String fichier;
    private int idEnseignant;
    private transient Utilisateur enseignant;
    private List<SoumissionDevoir> soumissions = new ArrayList<>();

    // Constructeurs
    public Devoir() {
    }

    public Devoir(int id) {
        this.id = id;
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

    public int getIdEnseignant() {
        return idEnseignant;
    }

    public void setIdEnseignant(int idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    public Utilisateur getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Utilisateur enseignant) {
        if (enseignant == null || !enseignant.getTypeUtilisateur().equals(Utilisateur.TypeUtilisateur.ENSEIGNANT)) {
            throw new IllegalArgumentException("L'utilisateur doit être un enseignant");
        }
        this.enseignant = enseignant;
        this.idEnseignant = enseignant.getIdUtilisateur();
    }

    public List<SoumissionDevoir> getSoumissions() {
        return soumissions;
    }

    public void setSoumissions(List<SoumissionDevoir> soumissions) {
        this.soumissions = soumissions;
    }

    // Méthodes pour JavaFX
    public StringProperty titreProperty() {
        return new SimpleStringProperty(titre);
    }

    public StringProperty dateLimiteStringProperty() {
        return new SimpleStringProperty(getFormattedDate());
    }

    public String getFormattedDate() {
        return datelimite.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    @Override
    public String toString() {
        return "Devoir{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", datelimite=" + datelimite +
                ", fichier='" + fichier + '\'' +
                ", idEnseignant=" + idEnseignant +
                '}';
    }
}
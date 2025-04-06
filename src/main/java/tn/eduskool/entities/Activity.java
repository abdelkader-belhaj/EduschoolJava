package tn.eduskool.entities;

import java.util.Date;

public class Activity {
    private int id;
    private String titre;
    private String description;
    private Date date;
    private String imageFileName;
    private boolean isApproved;
    private String typesActivity;
    private Date createdAt;

    public Activity(int id, String titre, String description, Date date, String imageFileName, boolean isApproved,
            String typesActivity, Date createdAt) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.imageFileName = imageFileName;
        this.isApproved = isApproved;
        this.typesActivity = typesActivity;
        this.createdAt = createdAt;
    }

    // Getters et setters
    public int getId() {
        return id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public String getTypesActivity() {
        return typesActivity;
    }

    public void setTypesActivity(String typesActivity) {
        this.typesActivity = typesActivity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "----------------------------------------\n" +
                "ID: " + id + "\n" +
                "Titre: " + titre + "\n" +
                "Description: " + description + "\n" +
                "Date: " + date + "\n" +
                "Image: " + imageFileName + "\n" +
                "Approuvé: " + (isApproved ? "Oui" : "Non") + "\n" +
                "Type d'activité: " + typesActivity + "\n" +
                "Créée le: " + createdAt + "\n" +
                "----------------------------------------\n";
    }

}

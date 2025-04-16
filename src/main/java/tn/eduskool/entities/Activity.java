package tn.eduskool.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

public class Activity {
    private int id;
    private String titre;
    private String description;
    private LocalDateTime date;
    private String imageFileName;
    private boolean isApproved;
    private String typesActivity;
    private LocalDateTime createdAt;

    // Liste des commentaires
    private List<Commentaire> commentaires = new ArrayList<>();

    public Activity(int id, String titre, String description, LocalDateTime date, String imageFileName,
            boolean isApproved,
            String typesActivity, LocalDateTime createdAt) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.imageFileName = imageFileName;
        this.isApproved = isApproved;
        this.typesActivity = typesActivity;
        this.createdAt = createdAt;
    }

    // Getters et Setters (standard)
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
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

    

    public boolean getApproved() {
        return isApproved;
    }

    public String getTypesActivity() {
        return typesActivity;
    }

    public void setTypesActivity(String typesActivity) {
        this.typesActivity = typesActivity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Gestion des commentaires
    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void addCommentaire(Commentaire commentaire) {
        commentaire.setActivityId(this.id); // lier le commentaire à l'activité
        commentaires.add(commentaire);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------------------\n")
                .append("ID: ").append(id).append("\n")
                .append("Titre: ").append(titre).append("\n")
                .append("Description: ").append(description).append("\n")
                .append("Date: ").append(date).append("\n")
                .append("Image: ").append(imageFileName).append("\n")
                .append("Approuvé: ").append(isApproved ? "Oui" : "Non").append("\n")
                .append("Type d'activité: ").append(typesActivity).append("\n")
                .append("Créée le: ").append(createdAt).append("\n");

        if (commentaires.isEmpty()) {
            sb.append("Commentaires: Aucun commentaire\n");
        } else {
            sb.append("Commentaires:\n");
            for (Commentaire c : commentaires) {
                sb.append("   - ").append(c).append("\n");
            }
        }

        sb.append("----------------------------------------\n");
        return sb.toString();
    }
}

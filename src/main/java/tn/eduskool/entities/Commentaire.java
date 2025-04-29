package tn.eduskool.entities;

public class Commentaire {
    private int id;
    private int activityId;
    private String contenu;
    private int note;
    private Utilisateur utilisateur; // Ajout de la relation avec l'utilisateur

    public Commentaire() {
    }

    public Commentaire(int id, int activityId, String contenu, int note) {
        this.id = id;
        this.activityId = activityId;
        this.contenu = contenu;
        this.note = note;
    }

    // Constructeur avec utilisateur
    public Commentaire(int id, int activityId, String contenu, int note, Utilisateur utilisateur) {
        this.id = id;
        this.activityId = activityId;
        this.contenu = contenu;
        this.note = note;
        this.utilisateur = utilisateur;
    }

    // Getters et Setters existants
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    // Nouveaux getters et setters pour l'utilisateur
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public String toString() {
        String userName = utilisateur != null ? utilisateur.getNom() : "Anonyme";
        return "----------------------------------------\n" +
                "ID: " + id + "\n" +
                "Activity ID: " + activityId + "\n" +
                "Auteur: " + userName + "\n" +
                "Contenu: " + contenu + "\n" +
                "Note: " + note + "\n" +
                "----------------------------------------\n";
    }
}

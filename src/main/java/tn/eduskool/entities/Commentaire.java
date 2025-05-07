package tn.eduskool.entities;

public class Commentaire {
    private int id;
 Gestion-Lecon
    private int activityId; // Référence à l'activité concernée
    private String contenu;
    private int note;

    private int activityId;
    private String contenu;
    private int note;
    private Utilisateur utilisateur; // Ajout de la relation avec l'utilisateur

    public Commentaire() {
    }
 main

    public Commentaire(int id, int activityId, String contenu, int note) {
        this.id = id;
        this.activityId = activityId;
        this.contenu = contenu;
        this.note = note;
    }

 Gestion-Lecon
    // Getters
    // Constructeur avec utilisateur
    public Commentaire(int id, int activityId, String contenu, int note, Utilisateur utilisateur) {
        this.id = id;
        this.activityId = activityId;
        this.contenu = contenu;
        this.note = note;
        this.utilisateur = utilisateur;
    }

    // Getters et Setters existants
 main
    public int getId() {
        return id;
    }

Gestion-Lecon

    public void setId(int id) {
        this.id = id;
    }

 main
    public int getActivityId() {
        return activityId;
    }

 Gestion-Lecon

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

 main
    public String getContenu() {
        return contenu;
    }

 Gestion-Lecon

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

 main
    public int getNote() {
        return note;
    }
 Gestion-Lecon
    // Setters
    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public void setNote(int note) {
        this.note = note;

    public void setNote(int note) {
        this.note = note;
    }

    // Nouveaux getters et setters pour l'utilisateur
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
 main
    }

    @Override
    public String toString() {
 Gestion-Lecon
        return "----------------------------------------\n" +
                "ID: " + id + "\n" +
                "Activity ID: " + activityId + "\n" +

        String userName = utilisateur != null ? utilisateur.getNom() : "Anonyme";
        return "----------------------------------------\n" +
                "ID: " + id + "\n" +
                "Activity ID: " + activityId + "\n" +
                "Auteur: " + userName + "\n" +
main
                "Contenu: " + contenu + "\n" +
                "Note: " + note + "\n" +
                "----------------------------------------\n";
    }
}

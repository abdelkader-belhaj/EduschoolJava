package tn.esprit.entities;

public class Commentaire {
    private int id;
    private int activityId; // Référence à l'activité concernée
    private String contenu;
    private int note;

    public Commentaire(int id, int activityId, String contenu, int note) {
        this.id = id;
        this.activityId = activityId;
        this.contenu = contenu;
        this.note = note;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getActivityId() {
        return activityId;
    }

    public String getContenu() {
        return contenu;
    }

    public int getNote() {
        return note;
    }

    // Setters
    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public void setNote(int note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "----------------------------------------\n" +
                "ID: " + id + "\n" +
                "Activity ID: " + activityId + "\n" +
                "Contenu: " + contenu + "\n" +
                "Note: " + note + "\n" +
                "----------------------------------------\n";
    }
}

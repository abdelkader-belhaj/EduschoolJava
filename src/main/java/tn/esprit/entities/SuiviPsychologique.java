package tn.esprit.entities;

import java.sql.Date;

public class SuiviPsychologique {
    private int id;
    private Date date_suivi;
    private String suivi_type;
    private String etat_emotionnel;
    private String nom_participant;
    private String nom_psychologue;

    // constructor

    public SuiviPsychologique() {
    }

    public SuiviPsychologique(Date date_suivi, String suivi_type, String etat_emotionnel, String nom_participant,
            String nom_psychologue) {
        this.date_suivi = date_suivi;
        this.suivi_type = suivi_type;
        this.etat_emotionnel = etat_emotionnel;
        this.nom_participant = nom_participant;
        this.nom_psychologue = nom_psychologue;
    }

    public SuiviPsychologique(int id, Date date_suivi, String suivi_type, String etat_emotionnel,
            String nom_participant, String nom_psychologue) {
        this.id = id;
        this.date_suivi = date_suivi;
        this.suivi_type = suivi_type;
        this.etat_emotionnel = etat_emotionnel;
        this.nom_participant = nom_participant;
        this.nom_psychologue = nom_psychologue;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate_suivi() {
        return date_suivi;
    }

    public void setDate_suivi(Date date_suivi) {
        this.date_suivi = date_suivi;
    }

    public String getSuivi_type() {
        return suivi_type;
    }

    public void setSuivi_type(String suivi_type) {
        this.suivi_type = suivi_type;
    }

    public String getEtat_emotionnel() {
        return etat_emotionnel;
    }

    public void setEtat_emotionnel(String etat_emotionnel) {
        this.etat_emotionnel = etat_emotionnel;
    }

    public String getNom_participant() {
        return nom_participant;
    }

    public void setNom_participant(String nom_participant) {
        this.nom_participant = nom_participant;
    }

    public String getNom_psychologue() {
        return nom_psychologue;
    }

    public void setNom_psychologue(String nom_psychologue) {
        this.nom_psychologue = nom_psychologue;
    }
    // display

    @Override
    public String toString() {
        return "SuiviPsychologique{" +
                "id=" + id +
                ", date_suivi=" + date_suivi +
                ", suivi_type='" + suivi_type + '\'' +
                ", etat_emotionnel='" + etat_emotionnel + '\'' +
                ", nom_participant='" + nom_participant + '\'' +
                ", nom_psychologue='" + nom_psychologue + '\'' +
                '}';
    }
}
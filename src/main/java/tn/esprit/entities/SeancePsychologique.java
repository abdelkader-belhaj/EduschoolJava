package tn.esprit.entities;

import java.sql.Date;

public class SeancePsychologique {
    private int id;
    private Date dateSeance;
    private String typeSeance;
    private int duree;
    private String nom_participant;
    private String nom_psychologue;
    private String status;

    public SeancePsychologique() {
    }

    public SeancePsychologique(int id, Date dateSeance, String typeSeance, int duree, String nom_participant,
            String nom_psychologue, String status) {
        this.id = id;
        this.dateSeance = dateSeance;
        this.typeSeance = typeSeance;
        this.duree = duree;
        this.nom_participant = nom_participant;
        this.nom_psychologue = nom_psychologue;
        this.status = status;
    }

    public SeancePsychologique(Date dateSeance, String typeSeance, int duree, String nom_participant,
            String nom_psychologue, String status) {
        this.dateSeance = dateSeance;
        this.typeSeance = typeSeance;
        this.duree = duree;
        this.nom_participant = nom_participant;
        this.nom_psychologue = nom_psychologue;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateSeance() {
        return dateSeance;
    }

    public void setDateSeance(Date dateSeance) {
        this.dateSeance = dateSeance;
    }

    public String getTypeSeance() {
        return typeSeance;
    }

    public void setTypeSeance(String typeSeance) {
        this.typeSeance = typeSeance;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SeancePsychologique{" +
                "id=" + id +
                ", dateSeance=" + dateSeance +
                ", typeSeance='" + typeSeance + '\'' +
                ", duree=" + duree +
                ", nom_participant='" + nom_participant + '\'' +
                ", nom_psychologue='" + nom_psychologue + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
package tn.eduskool.entities;

import java.time.LocalDateTime;

public class SoumissionDevoir {
    private int id;
    private LocalDateTime dateSoumission;
    private String fichier;
    private Integer note;
    private Devoir devoir;

    public Devoir getDevoir() {
        return devoir;
    }

    public void setDevoir(Devoir devoir) {
        this.devoir = devoir;
    }

    // Constructeurs

    public SoumissionDevoir() {
        this.dateSoumission = LocalDateTime.now(); // Date actuelle par défaut
    }

    public SoumissionDevoir(LocalDateTime dateSoumission, String fichier, Integer note) {
        this.dateSoumission = dateSoumission;
        this.fichier = fichier;
        this.note = note;
    }

    public SoumissionDevoir(int id, LocalDateTime dateSoumission, String fichier, Integer note) {
        this.id = id;
        this.dateSoumission = dateSoumission;
        this.fichier = fichier;
        this.note = note;
    }

    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateSoumission() {
        return dateSoumission;
    }

    public void setDateSoumission(LocalDateTime dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }
    public SoumissionDevoir(int id, LocalDateTime dateSoumission, String fichier, Integer note, Devoir devoir) {
        this.id = id;
        this.dateSoumission = dateSoumission;
        this.fichier = fichier;
        this.note = note;
        this.devoir = devoir;
    }


    // toString

    @Override
    public String toString() {
        return "SoumissionDevoir{" +
                "id=" + id +
                ", dateSoumission=" + dateSoumission +
                ", fichier='" + fichier + '\'' +
                ", note=" + note +
                '}';
    }
}

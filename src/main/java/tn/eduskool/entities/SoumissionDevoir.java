package tn.eduskool.entities;

import java.time.LocalDateTime;

 Gestion-Lecon
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


main
public class SoumissionDevoir {
    private int id;
    private LocalDateTime dateSoumission;
    private String fichier;
    private Integer note;
Gestion-Lecon

    @ManyToOne
    @JoinColumn(name = "devoir_id")
    private Devoir devoir;

    // Getter & Setter pour devoir
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


    private Integer devoir_id;
    private Devoir devoir;
    private int etudiantId;

    // Constructors
    public SoumissionDevoir() {
    }

    // Getters and Setters
main
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

 Gestion-Lecon
    // toString

    @Override
    public String toString() {
        return "SoumissionDevoir{" +
                "id=" + id +
                ", dateSoumission=" + dateSoumission +
                ", fichier='" + fichier + '\'' +
                ", note=" + note +
                '}';

    public Integer getDevoirId() {
        return devoir_id;
    }

    public void setDevoirId(Integer devoir_id) {
        this.devoir_id = devoir_id;
    }

    public Devoir getDevoir() {
        return devoir;
    }

    public void setDevoir(Devoir devoir) {
        this.devoir = devoir;
        if (devoir != null) {
            this.devoir_id = devoir.getId();
        }
    }

    public int getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(int etudiantId) {
        this.etudiantId = etudiantId;
 main
    }
}

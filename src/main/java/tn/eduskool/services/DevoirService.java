package tn.eduskool.services;

import tn.eduskool.entities.Devoir;
import tn.eduskool.repository.DevoirRepository;
import java.sql.SQLException;
import java.util.List;

public class DevoirService {
    private final DevoirRepository devoirRepository;

    public DevoirService() {
        this.devoirRepository = new DevoirRepository();
    }

    public void ajouter(Devoir devoir) {
        try {
            devoirRepository.save(devoir);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout du devoir", e);
        }
    }

    public void modifier(Devoir devoir) {
        try {
            devoirRepository.update(devoir);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification du devoir", e);
        }
    }

    public void supprimer(int id) {
        try {
            devoirRepository.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du devoir", e);
        }
    }

    public List<Devoir> recuperer() {
        try {
            return devoirRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des devoirs", e);
        }
    }

    public List<Devoir> recupererParEnseignant(int enseignantId) {
        try {
            return devoirRepository.findByEnseignantId(enseignantId);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des devoirs", e);
        }
    }
}
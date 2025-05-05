package tn.eduskool.services;

import tn.eduskool.repository.SoumissionDevoirRepository;
import tn.eduskool.entities.SoumissionDevoir;
import java.sql.SQLException;
import java.util.List;

public class SoumissionDevoirService {
    private final SoumissionDevoirRepository soumissionRepository;

    public SoumissionDevoirService() {
        this.soumissionRepository = new SoumissionDevoirRepository();
    }

    public void attribuerNote(int soumissionId, Integer note) {
        try {
            soumissionRepository.attribuerNote(soumissionId, note);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'attribution de la note", e);
        }
    }

    public List<SoumissionDevoir> recupererParDevoirId(int devoirId) {
        try {
            return soumissionRepository.findByDevoirId(devoirId);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des soumissions", e);
        }
    }

    public void save(SoumissionDevoir soumission) {
        try {
            soumissionRepository.save(soumission);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de la soumission", e);
        }
    }

    public SoumissionDevoir findById(int id) {
        try {
            return soumissionRepository.findById(id).orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la soumission", e);
        }
    }

    public boolean ajouter(SoumissionDevoir soumission) {
        try {
            soumissionRepository.save(soumission);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifier(SoumissionDevoir soumission) {
        try {
            soumissionRepository.save(soumission);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimer(int id) {
        try {
            soumissionRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<SoumissionDevoir> recuperer() {
        try {
            return soumissionRepository.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des soumissions", e);
        }
    }

    public List<SoumissionDevoir> recupererParEtudiant(int etudiantId) {
        try {
            return soumissionRepository.findByEtudiantId(etudiantId);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des soumissions", e);
        }
    }
}
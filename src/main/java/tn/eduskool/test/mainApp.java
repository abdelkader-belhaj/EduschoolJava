package tn.eduskool.test;

import tn.eduskool.entities.Devoir;
import tn.eduskool.entities.SoumissionDevoir;
import tn.eduskool.services.DevoirService;
import tn.eduskool.services.SoumissionDevoirService;
import tn.eduskool.tools.DatabaseConnection;

import java.time.LocalDateTime;
import java.util.List;

public class mainApp {

    public static void main(String[] args) {
        DatabaseConnection dbc = DatabaseConnection.getInstance();
        DevoirService ds = new DevoirService();
        Devoir devoir = new Devoir();
        devoir.setTitre("Devoir mousi9ia");
        devoir.setDescription("TP sur les classes");
        devoir.setDatelimite(LocalDateTime.of(2025, 5, 5, 23, 59));
        devoir.setFichier("tp_java_oop.pdf");
        ds.ajouter(devoir);

        SoumissionDevoirService sds = new SoumissionDevoirService();

        SoumissionDevoir soumission = new SoumissionDevoir();
        soumission.setDateSoumission(LocalDateTime.now());
        soumission.setFichier("tp_java_solution.pdf");
        soumission.setNote(null); // pas encore corrigé
        soumission.setDevoir(devoir);

        sds.ajouter(soumission);
        System.out.println("\n===== SOUMISSION AJOUTÉE =====");
        System.out.println(soumission);




    }

}
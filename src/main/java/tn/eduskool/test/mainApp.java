package tn.eduskool.test;

import tn.eduskool.entities.Cours;
import tn.eduskool.entities.Theme;
import tn.eduskool.services.CoursService;
import tn.eduskool.services.ThemeService;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class mainApp {
    public static void main(String[] args) throws SQLException {

        // Créer une instance de CoursService
          CoursService cs = new CoursService();

        // Créer un objet Cours avec les données à modifier
         LocalDateTime dt = LocalDateTime.now();
         Theme t = new Theme("fjjfrf");

        //Cours cours = new Cours(2,"hahahahhahahha",dt,"zayneb","histoire");
        Cours c1= new Cours("maryem",dt,"fjdkfd",t);

        try {
        cs.ajouter(c1);
        // Appeler la méthode modifier pour mettre à jour les données dans la base
        //cs.modifier(cours);

        // Afficher un message de confirmation
        //} catch (SQLException e) {
        // Gérer les erreurs de SQL
        //  System.out.println(e.getMessage());
        //}
       //ThemeService ts = new ThemeService();
        //Theme t = new Theme(1, "jddcd");
        //Theme t = new Theme(1, "test");
        //try {
            //ts.ajouter(t);
            //ts.modifier(t);
            //ts.supprimer(1);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

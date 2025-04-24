package tn.eduskool.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import tn.eduskool.controllers.BaseController;
import tn.eduskool.entities.Utilisateur;

public class DatabaseConnection implements BaseController {

    public static Connection connect() {
        String url = "jdbc:mysql://127.0.0.1:3306/eduskool";
        String username = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connexion réussie !");
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected Utilisateur utilisateur;

    @Override
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    @Override
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
}
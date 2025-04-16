package tn.eduskool.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {


    public final String url = "jdbc:mysql://127.0.0.1:3306/eduskool?serverVersion=10.4.32-MariaDB&charset=utf8mb4";
    public final String username = "root";
    public final String password = "";
    private Connection cnx;


    public static DatabaseConnection myDtaBase;

    private DatabaseConnection() {
        try {
            cnx = DriverManager.getConnection(url, username, password);
            System.out.println("cnx établis avec succées");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public static DatabaseConnection getInstance () {
        if (myDtaBase == null)
            myDtaBase = new DatabaseConnection();
        return myDtaBase;
    }

    public Connection getCnx() {
        return cnx;
    }

    public void setCnx(Connection cnx) {
        this.cnx = cnx;
    }
}

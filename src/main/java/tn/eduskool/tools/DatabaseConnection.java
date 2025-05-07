package tn.eduskool.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
 Gestion-Lecon


        String url = "jdbc:mysql://127.0.0.1:3306/eduskool";
        String username = "root";
        String password = "";
        private Connection cnx;
        public static DatabaseConnection mydatabase;
        public DatabaseConnection(){

            try {
                cnx= DriverManager.getConnection(url, username, password);
                System.out.println("connexion etablie");
            } catch (SQLException e) {
                // throw new RuntimeException(e);
                System.out.println(e.getMessage());
            }
        }
        public static DatabaseConnection getInstance(){
            if(mydatabase==null)
                mydatabase=new DatabaseConnection();
            return mydatabase;
        }



        public Connection getCnx() {
            return cnx;
        }

        public void setCnx(Connection cnx) {
            this.cnx = cnx;
        }
    }



=======
    private static final String URL = "jdbc:mysql://localhost:3306/edu01";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Erreur de connexion: " + e.getMessage());
            throw new RuntimeException("Impossible de se connecter à la base de données", e);
        }
    }
}
 main

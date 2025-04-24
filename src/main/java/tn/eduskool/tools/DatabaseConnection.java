package tn.eduskool.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {


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




/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.util;

/**
 *
 * @author chori
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static Connection cnx = null;

    public static Connection obtener() throws SQLException, ClassNotFoundException {
        if (cnx == null) {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                String myDB = "jdbc:oracle:thin:@localhost:1521:XE";
                String user = "root2";
                String password = "123";
                cnx = (Connection) DriverManager.getConnection(myDB,user,password);
                System.out.println("connected");
            } catch (ClassNotFoundException ex) {
                throw new ClassCastException(ex.getMessage());
            }
        }
        return cnx;
    }

    public static void cerrar() throws SQLException {
        if (cnx != null) {
            cnx.close();
        }
    }
}

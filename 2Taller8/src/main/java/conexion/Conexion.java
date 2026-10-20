/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/tienda?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Escribe tu contraseña de MySQL si tienes una

    public static Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("¡Conexión exitosa a la base de datos tienda!");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el driver de MySQL -> " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error de conexión a la base de datos -> " + e.getMessage());
        }
        return conexion;
    }
}
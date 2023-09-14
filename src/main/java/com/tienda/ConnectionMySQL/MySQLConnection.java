package com.tienda.ConnectionMySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    // Datos de conexión a la base de datos
    private static final String HOST = "20.115.17.217"; // Cambia esto si tu base de datos está en otro servidor
    private static final String PORT = "3306"; // Puerto de MySQL (por defecto es 3306)
    private static final String DATABASE = "TiendaPC"; // Reemplaza "nombre_base_de_datos" por el nombre de tu base de datos
    private static final String USER = "root"; // Reemplaza "tu_usuario" por el nombre de usuario de tu base de datos
    private static final String PASSWORD = "root"; // Reemplaza "tu_contraseña" por la contraseña de tu base de datos
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE;


    private static Connection connection;

    // Constructor privado para evitar que se creen instancias de esta clase
    private MySQLConnection() {

    }
    /**
     * Obtiene la conexión a la base de datos establecida.
     *
     * @return Objeto Connection que representa la conexión a la base de datos MySQL.
     */
    // Método para obtener la instancia de la conexión
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Si la conexión no ha sido creada o ha sido cerrada, se crea una nueva instancia
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
    /**
     * Cierra la conexión a la base de datos si está abierta.
     */
    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

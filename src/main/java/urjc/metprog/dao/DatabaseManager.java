package main.java.urjc.metprog.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    
    // Ruta a la base de datos (se guardará en la carpeta data/ de la raíz del proyecto)
    private static final String DB_URL = "jdbc:sqlite:data/game_database.db";

    /**
     * Obtiene una conexión a la base de datos.
     * @return Connection objeto de conexión JDBC
     * @throws SQLException si ocurre un error al conectar
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Inicializa las tablas necesarias para el funcionamiento del juego.
     * Solo crea las tablas si no existen previamente.
     */
    public static void createTables() {
        // En SQLite, las tablas necesitan al menos una columna. 
        // Empezamos con un ID autoincremental como clave primaria.
        
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                           + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                           + "nick TEXT NOT NULL UNIQUE, "
                           + "name TEXT NOT NULL, "
                           + "password TEXT NOT NULL, "
                           + "num_reg INTEGER NOT NULL, "
                           + "character_id INTEGER"
                           + ");";
                           
        String sqlPersonajes = "CREATE TABLE IF NOT EXISTS personajes ("
                             + "id INTEGER PRIMARY KEY AUTOINCREMENT"
                             + ");";
                             
        String sqlDesafios = "CREATE TABLE IF NOT EXISTS desafios ("
                           + "id INTEGER PRIMARY KEY AUTOINCREMENT"
                           + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Ejecutar las sentencias de creación
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlPersonajes);
            stmt.execute(sqlDesafios);
            
            System.out.println("Tablas inicializadas correctamente (usuarios, personajes, desafios).");
            
        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }
}

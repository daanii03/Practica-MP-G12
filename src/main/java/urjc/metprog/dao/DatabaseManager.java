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
                           + "num_reg TEXT NOT NULL, "
                           + "character_id INTEGER"
                           + ");";
                           
        String sqlPersonajes = "CREATE TABLE IF NOT EXISTS personajes ("
                             + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                             + "user_id INTEGER NOT NULL, "
                             + "name TEXT NOT NULL, "
                             + "race TEXT NOT NULL CHECK(race IN ('werewolf', 'vampire', 'hunter')), "
                             + "gold INTEGER NOT NULL, "
                             + "life INTEGER NOT NULL CHECK (life BETWEEN 0 AND 5), "
                             + "power INTEGER NOT NULL CHECK (power BETWEEN 1 AND 5), "
                             // TODO: añadir columna para inventario
                             + "special_skill TEXT NOT NULL CHECK(special_skill IN ('discipline', 'gift', 'talent')), "
                             + "FOREIGN KEY(user_id) REFERENCES usuarios(id)"
                             + ");";
                             
        String sqlDesafios = "CREATE TABLE IF NOT EXISTS desafios ("
                           + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                           + "challenger_id INTEGER NOT NULL, "
                           + "challenged_id INTEGER NOT NULL, "
                           + "rounds_played INTEGER NOT NULL, "
                           + "combat_date TEXT NOT NULL, "
                           + "winner_id INTEGER, "
                           + "challenger_kept_minions INTEGER DEFAULT 0 CHECK (challenger_kept_minions IN (0, 1)), "
                           + "challenged_kept_minions INTEGER DEFAULT 0 CHECK (challenged_kept_minions IN (0, 1)), "
                           + "gold_won INTEGER NOT NULL, "
                           + "FOREIGN KEY(challenger_id) REFERENCES usuarios(id), "
                           + "FOREIGN KEY(challenged_id) REFERENCES usuarios(id), "
                           + "FOREIGN KEY(winner_id) REFERENCES usuarios(id)"
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

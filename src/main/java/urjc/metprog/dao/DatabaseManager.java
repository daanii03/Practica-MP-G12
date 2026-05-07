package main.java.urjc.metprog.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase responsable de gestionar la conexión y la estructura de la base de
 * datos SQLite.
 * Centraliza la creación de tablas, la inicialización de datos base (catálogo)
 * y proporciona conexiones al resto de la capa DAO.
 * 
 * @author Ivo Hagemann Martínez
 * @version 1.0
 */
public class DatabaseManager {

    private static String dbUrl = "jdbc:sqlite:data/game_database.db";
    private static Connection keepAliveConnection;


    /**
     * Configura el manager para usar una base de datos en memoria compartida.
     * Esto permite que las tablas persistan entre diferentes conexiones durante los tests.
     */
    public static void useTestDatabase() {
        dbUrl = "jdbc:sqlite:file:testdb?mode=memory&cache=shared";
        try {
            // Mantenemos una conexión abierta para que la base de datos no se borre
            if (keepAliveConnection == null || keepAliveConnection.isClosed()) {
                keepAliveConnection = DriverManager.getConnection(dbUrl);
            }
        } catch (SQLException e) {
            System.err.println("Error al mantener la conexión de test: " + e.getMessage());
        }
    }

    /**
     * Permite volver a la base de datos real si fuera necesario.
     */
    public static void useDefaultDatabase() {
        dbUrl = "jdbc:sqlite:data/game_database.db";
        try {
            if (keepAliveConnection != null && !keepAliveConnection.isClosed()) {
                keepAliveConnection.close();
                keepAliveConnection = null;
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión de test: " + e.getMessage());
        }
    }

    /**
     * Obtiene una conexión a la base de datos.
     * 
     * @return Connection objeto de conexión JDBC
     * @throws SQLException si ocurre un error al conectar
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }

    /**
     * Inicializa las tablas necesarias para el funcionamiento del juego.
     * Solo crea las tablas si no existen previamente.
     */
    public static void createTables() {

        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nick TEXT NOT NULL UNIQUE, "
                + "name TEXT NOT NULL, "
                + "password TEXT NOT NULL, "
                + "num_reg TEXT, "
                + "character_id INTEGER, "
                + "victories INTEGER DEFAULT 0, "
                + "role TEXT DEFAULT 'user', "
                + "is_blocked INTEGER DEFAULT 0"
                + ");";

        String sqlPersonajes = "CREATE TABLE IF NOT EXISTS personajes ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER NOT NULL, "
                + "name TEXT NOT NULL, "
                + "race TEXT NOT NULL CHECK(race IN ('werewolf', 'vampire', 'hunter')), "
                + "gold INTEGER NOT NULL, "
                + "life INTEGER NOT NULL CHECK (life BETWEEN 0 AND 5), "
                + "power INTEGER NOT NULL CHECK (power BETWEEN 1 AND 5), "
                + "special_skill TEXT NOT NULL CHECK(special_skill IN ('discipline', 'gift', 'talent')), "
                + "skill_name TEXT NOT NULL, "
                + "skill_attack INTEGER NOT NULL CHECK (skill_attack BETWEEN 1 AND 3), "
                + "skill_defense INTEGER NOT NULL CHECK (skill_defense BETWEEN 1 AND 3), "
                + "skill_cost INTEGER DEFAULT 0, "
                + "age INTEGER, "
                + "blood_points INTEGER, "
                + "rage INTEGER, "
                + "will INTEGER, "
                + "FOREIGN KEY(user_id) REFERENCES usuarios(id)"
                + ");";

        String sqlEquipment = "CREATE TABLE IF NOT EXISTS equipamiento ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "character_id INTEGER NOT NULL, "
                + "type TEXT NOT NULL CHECK(type IN ('weapon', 'armor')), "
                + "name TEXT NOT NULL, "
                + "attack_modifier INTEGER NOT NULL CHECK (attack_modifier BETWEEN 0 AND 3), "
                + "defense_modifier INTEGER NOT NULL CHECK (defense_modifier BETWEEN 0 AND 3), "
                + "is_one_hand INTEGER, "
                + "is_active INTEGER DEFAULT 0 CHECK (is_active IN (0, 1)), "
                + "FOREIGN KEY(character_id) REFERENCES personajes(id) ON DELETE CASCADE"
                + ");";

        String sqlMinions = "CREATE TABLE IF NOT EXISTS esbirros ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "character_id INTEGER NOT NULL, "
                + "type TEXT NOT NULL CHECK(type IN ('human', 'ghoul', 'demon')), "
                + "name TEXT NOT NULL, "
                + "health INTEGER NOT NULL CHECK (health BETWEEN 1 AND 3), "
                + "loyalty TEXT CHECK(loyalty IN ('ALTA', 'NORMAL', 'BAJA')), "
                + "dependence INTEGER, "
                + "pact_description TEXT, "
                + "FOREIGN KEY(character_id) REFERENCES personajes(id) ON DELETE CASCADE"
                + ");";

        String sqlModifiers = "CREATE TABLE IF NOT EXISTS modificadores ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "character_id INTEGER NOT NULL, "
                + "type TEXT NOT NULL CHECK(type IN ('strength', 'weakness')), "
                + "name TEXT NOT NULL, "
                + "value INTEGER NOT NULL CHECK (value BETWEEN 1 AND 5), "
                + "FOREIGN KEY(character_id) REFERENCES personajes(id) ON DELETE CASCADE"
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

        String sqlCatalogo = "CREATE TABLE IF NOT EXISTS catalogo_equipo ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL UNIQUE, "
                + "type TEXT NOT NULL CHECK(type IN ('weapon', 'armor')), "
                + "attack INTEGER NOT NULL, "
                + "defense INTEGER NOT NULL, "
                + "is_one_hand INTEGER DEFAULT 0"
                + ");";

        // TABLA NUEVA: desafíos pendientes de validación o respuesta
        String sqlDesafiosPendientes = "CREATE TABLE IF NOT EXISTS desafios_pendientes ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "challenger_nick TEXT NOT NULL, "
                + "challenged_nick TEXT NOT NULL, "
                + "gold_bet INTEGER NOT NULL, "
                + "status TEXT NOT NULL DEFAULT 'PENDING_VALIDATION', "
                + "created_at TEXT NOT NULL, "
                + "modifiers TEXT"
                + ");";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            stmt.execute(sqlUsuarios);
            stmt.execute(sqlPersonajes);
            stmt.execute(sqlEquipment);
            stmt.execute(sqlMinions);
            stmt.execute(sqlModifiers);
            stmt.execute(sqlDesafios);
            stmt.execute(sqlCatalogo);
            stmt.execute(sqlDesafiosPendientes); // <-- AÑADIDO

            seedCatalog(conn);

        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }

    /**
     * Rellena el catálogo con armas y armaduras iniciales si está vacío.
     */
    private static void seedCatalog(Connection conn) {
        String checkSql = "SELECT COUNT(*) FROM catalogo_equipo";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(checkSql)) {

            if (rs.next() && rs.getInt(1) > 0) {
                return; // Ya hay datos
            }

            String insertSql = "INSERT INTO catalogo_equipo (name, type, attack, defense, is_one_hand) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                // 5 ARMAS
                String[][] weapons = {
                        { "Espada Celestial", "3", "1", "1" },
                        { "Daga de las Sombras", "2", "0", "1" },
                        { "Hacha de Guerra Pesada", "3", "0", "0" },
                        { "Arco de Cazador", "2", "1", "0" },
                        { "Cuchillo de Plata", "1", "1", "1" }
                };
                for (String[] w : weapons) {
                    pstmt.setString(1, w[0]);
                    pstmt.setString(2, "weapon");
                    pstmt.setInt(3, Integer.parseInt(w[1]));
                    pstmt.setInt(4, Integer.parseInt(w[2]));
                    pstmt.setInt(5, Integer.parseInt(w[3]));
                    pstmt.addBatch();
                }

                // 5 ARMADURAS
                String[][] armors = {
                        { "Armadura de Placas de Ébano", "0", "3" },
                        { "Cota de Malla Reforzada", "0", "2" },
                        { "Capa de Invisibilidad", "1", "2" },
                        { "Escudo de Roble Antiguo", "0", "2" },
                        { "Manto de Sangre", "2", "1" }
                };
                for (String[] a : armors) {
                    pstmt.setString(1, a[0]);
                    pstmt.setString(2, "armor");
                    pstmt.setInt(3, Integer.parseInt(a[1]));
                    pstmt.setInt(4, Integer.parseInt(a[2]));
                    pstmt.setInt(5, 0); // No aplica a armaduras
                    pstmt.addBatch();
                }

                pstmt.executeBatch();
                System.out.println("Catálogo de equipo inicializado con 10 objetos.");
            }
        } catch (SQLException e) {
            System.err.println("Error al sembrar el catálogo: " + e.getMessage());
        }
    }
}

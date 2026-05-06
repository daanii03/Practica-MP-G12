package test.main.java.urjc.metprog.dao;

import main.java.urjc.metprog.dao.DatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {

    @BeforeEach
    void setUp() {
        // Usar base de datos en memoria para cada test
        DatabaseManager.useTestDatabase();
    }

    @AfterEach
    void tearDown() {
        // Volver a la base de datos real por seguridad
        DatabaseManager.useDefaultDatabase();
    }

    @Test
    void createTables_CreatesAllRequiredTables() throws SQLException {
        DatabaseManager.createTables();

        try (Connection conn = DatabaseManager.getConnection()) {
            String[] tables = {
                "usuarios", "personajes", "equipamiento", "esbirros", 
                "modificadores", "desafios", "catalogo_equipo", "desafios_pendientes"
            };

            for (String table : tables) {
                assertTrue(tableExists(conn, table), "La tabla " + table + " debería existir");
            }
        }
    }

    @Test
    void createTables_SeedsCatalogCorrectly() throws SQLException {
        DatabaseManager.createTables();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM catalogo_equipo")) {
            
            assertTrue(rs.next());
            assertEquals(10, rs.getInt(1), "El catálogo debería tener 10 objetos iniciales");
        }
    }

    private boolean tableExists(Connection conn, String tableName) throws SQLException {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next();
        }
    }
}

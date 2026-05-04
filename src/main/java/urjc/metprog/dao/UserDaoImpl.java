package main.java.urjc.metprog.dao;

import main.java.urjc.metprog.models.user.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.java.urjc.metprog.models.user.UserBuilder;

/**
 * Implementación de la interfaz UserDao utilizando SQLite como motor de
 * persistencia.
 * Gestiona el ciclo de vida de los datos de usuario y su estado en el juego.
 * 
 * 
 * @author Ivo Hagemann Martínez
 * @version 1.0
 */
public class UserDaoImpl implements UserDao {

    @Override
    public void insertUser(User user) {
        String sql = "INSERT INTO usuarios (nick, name, password, num_reg, character_id, victories, role, is_blocked) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getNick());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRegistrationNumber());

            pstmt.setNull(5, java.sql.Types.INTEGER);
            pstmt.setInt(6, user.getVictories());
            pstmt.setString(7, user.getRole());
            pstmt.setInt(8, user.isBlocked() ? 1 : 0);

            pstmt.executeUpdate();
            System.out.println("Usuario " + user.getNick() + " insertado correctamente en la base de datos.");

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            throw new RuntimeException("Error al registrar el usuario en la base de datos: " + e.getMessage());
        }
    }

    @Override
    public void updateUser(User user) {
        // Actualizamos nombre, contraseña, victorias, rol y bloqueo buscando al usuario
        // por su nick (que es único)
        String sql = "UPDATE usuarios SET name = ?, password = ?, victories = ?, role = ?, is_blocked = ? WHERE nick = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getVictories());
            pstmt.setString(4, user.getRole());
            pstmt.setInt(5, user.isBlocked() ? 1 : 0);
            pstmt.setString(6, user.getNick());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Usuario " + user.getNick() + " actualizado correctamente.");
            } else {
                System.out.println("No se ha encontrado el usuario " + user.getNick() + " para actualizar.");
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public void deleteUser(String nick) {
        String sql = "DELETE FROM usuarios WHERE nick = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nick);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Usuario " + nick + " borrado correctamente.");
            } else {
                System.out.println("No se ha encontrado el usuario " + nick + " para borrar.");
            }

        } catch (SQLException e) {
            System.err.println("Error al borrar usuario: " + e.getMessage());
        }
    }

    @Override
    public User getUser(String nick) {
        String sql = "SELECT * FROM usuarios WHERE nick = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nick);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new UserBuilder()
                            .setName(rs.getString("name"))
                            .setNick(rs.getString("nick"))
                            .setPassword(rs.getString("password"))
                            .setRegistrationNumber(rs.getString("num_reg"))
                            .setVictories(rs.getInt("victories"))
                            .setRole(rs.getString("role"))
                            .setBlocked(rs.getInt("is_blocked") == 1)
                            .build();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al recuperar usuario: " + e.getMessage());
        }
        return null;
    }

    @Override
    public User getUserByRegistrationNumber(String numReg) {
        String sql = "SELECT * FROM usuarios WHERE num_reg = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, numReg);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new UserBuilder()
                            .setName(rs.getString("name"))
                            .setNick(rs.getString("nick"))
                            .setPassword(rs.getString("password"))
                            .setRegistrationNumber(rs.getString("num_reg"))
                            .setVictories(rs.getInt("victories"))
                            .setRole(rs.getString("role"))
                            .setBlocked(rs.getInt("is_blocked") == 1)
                            .build();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al recuperar usuario por número de registro: " + e.getMessage());
        }
        return null;
    }

    @Override
    public java.util.List<User> getGlobalRanking() {
        java.util.List<User> ranking = new java.util.ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY victories DESC";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ranking.add(new UserBuilder()
                        .setName(rs.getString("name"))
                        .setNick(rs.getString("nick"))
                        .setPassword(rs.getString("password"))
                        .setRegistrationNumber(rs.getString("num_reg"))
                        .setVictories(rs.getInt("victories"))
                        .setRole(rs.getString("role"))
                        .setBlocked(rs.getInt("is_blocked") == 1)
                        .build());
            }
        } catch (SQLException e) {
            System.err.println("Error al recuperar el ranking global: " + e.getMessage());
        }
        return ranking;
    }

    /**
     * Obtiene el ID numérico de la base de datos de un usuario por su nick.
     * Necesario para operaciones que usan IDs enteros (como ChallengeDao).
     *
     * @param nick Nick del usuario.
     * @return El ID del usuario, o -1 si no existe.
     */
    public int getUserId(String nick) {
        String sql = "SELECT id FROM usuarios WHERE nick = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nick);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el ID del usuario: " + e.getMessage());
        }
        return -1;
    }
}

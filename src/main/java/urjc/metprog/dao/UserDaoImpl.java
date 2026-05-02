package main.java.urjc.metprog.dao;

import main.java.urjc.metprog.models.user.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.java.urjc.metprog.models.user.UserBuilder;

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
            
            // TODO: Si el usuario ya tiene un personaje asociado al registrarse,
            // aquí deberíamos guardar su ID. Por ahora, asumimos que el usuario
            // se crea sin personaje y se le asigna después, por lo que insertamos NULL.
            pstmt.setNull(5, java.sql.Types.INTEGER);
            pstmt.setInt(6, user.getVictories());
            pstmt.setString(7, user.getRole());
            pstmt.setInt(8, user.isBlocked() ? 1 : 0);
            
            pstmt.executeUpdate();
            System.out.println("Usuario " + user.getNick() + " insertado correctamente en la base de datos.");
            
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
        }
    }

    @Override
    public void updateUser(User user) {
        // Actualizamos nombre, contraseña, victorias, rol y bloqueo buscando al usuario por su nick (que es único)
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
}

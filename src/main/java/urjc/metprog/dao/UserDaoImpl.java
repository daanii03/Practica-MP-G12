package main.java.urjc.metprog.dao;

import main.java.urjc.metprog.models.user.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {

    @Override
    public void insertUser(User user) {
        String sql = "INSERT INTO usuarios (nick, name, password, num_reg, character_id) VALUES (?, ?, ?, ?, ?)";
        
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
            
            pstmt.executeUpdate();
            System.out.println("Usuario " + user.getNick() + " insertado correctamente en la base de datos.");
            
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
        }
    }

    @Override
    public void updateUser(User user) {
        // Actualizamos nombre y contraseña buscando al usuario por su nick (que es único)
        String sql = "UPDATE usuarios SET name = ?, password = ? WHERE nick = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getNick());
            
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
    public void deleteUser(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Usuario con ID " + id + " borrado correctamente.");
            } else {
                System.out.println("No se ha encontrado un usuario con ID " + id + " para borrar.");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al borrar usuario: " + e.getMessage());
        }
    }
}

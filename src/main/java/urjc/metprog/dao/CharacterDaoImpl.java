package main.java.urjc.metprog.dao;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.characters.Hunter;
import main.java.urjc.metprog.models.characters.Vampire;
import main.java.urjc.metprog.models.characters.Werewolf;
import main.java.urjc.metprog.models.skills.Discipline;
import main.java.urjc.metprog.models.skills.Gift;
import main.java.urjc.metprog.models.skills.Talent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CharacterDaoImpl implements CharacterDao {

    @Override
    public void insertCharacter(Character character, String ownerNick) {
        int userId = getUserIdByNick(ownerNick);
        
        if (userId == -1) {
            System.err.println("Error: No se puede insertar el personaje porque el dueño '" + ownerNick + "' no existe en la base de datos.");
            return;
        }

        String sqlInsert = "INSERT INTO personajes (user_id, name, race, gold, life, power, special_skill) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, character.getName());
            
            // Determinar raza
            String race = "vampire"; // default fallback
            if (character instanceof Werewolf) {
                race = "werewolf";
            } else if (character instanceof Hunter) {
                race = "hunter";
            }
            pstmt.setString(3, race);
            
            // Oro (usando checkGold según la clase)
            pstmt.setInt(4, character.checkGold());
            
            // Vida y poder
            pstmt.setInt(5, character.getLife());
            pstmt.setInt(6, character.getPower());
            
            // Determinar habilidad especial
            String specialSkillType = "discipline"; // default fallback
            if (character.getSpecialSkill() instanceof Gift) {
                specialSkillType = "gift";
            } else if (character.getSpecialSkill() instanceof Talent) {
                specialSkillType = "talent";
            }
            pstmt.setString(7, specialSkillType);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Recuperar el ID del personaje insertado
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int characterId = generatedKeys.getInt(1);
                        character.setId(characterId);
                        
                        // Enlazar en la tabla usuarios
                        linkCharacterToUser(userId, characterId);
                        
                        System.out.println("Personaje '" + character.getName() + "' insertado correctamente y enlazado al usuario '" + ownerNick + "'.");
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar personaje: " + e.getMessage());
        }
    }

    @Override
    public Character getCharacter(int id) {
        String sql = "SELECT * FROM personajes WHERE id = ?";
        return fetchCharacterByQuery(sql, id, null);
    }

    @Override
    public Character getCharacterByOwner(String ownerNick) {
        int userId = getUserIdByNick(ownerNick);
        if (userId == -1) return null;
        
        String sql = "SELECT * FROM personajes WHERE user_id = ?";
        return fetchCharacterByQuery(sql, userId, null);
    }

    @Override
    public void deleteCharacter(int id) {
        String sql = "DELETE FROM personajes WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Personaje con ID " + id + " borrado correctamente.");
                // TODO: Habría que poner a NULL el character_id del usuario que lo poseía
            } else {
                System.out.println("No se ha encontrado un personaje con ID " + id + " para borrar.");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al borrar personaje: " + e.getMessage());
        }
    }

    // ==========================================
    // MÉTODOS AUXILIARES PRIVADOS
    // ==========================================

    private int getUserIdByNick(String nick) {
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
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
        return -1;
    }

    private void linkCharacterToUser(int userId, int characterId) {
        String sql = "UPDATE usuarios SET character_id = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, characterId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error al enlazar personaje con usuario: " + e.getMessage());
        }
    }

    private Character fetchCharacterByQuery(String sql, int paramId, String paramStr) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (paramStr == null) {
                pstmt.setInt(1, paramId);
            } else {
                pstmt.setString(1, paramStr);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String race = rs.getString("race");
                    int life = rs.getInt("life");
                    int power = rs.getInt("power");
                    String specialSkillType = rs.getString("special_skill");
                    
                    // ========================================================
                    // MEDIDA TEMPORAL: Como la base de datos no almacena aún
                    // los detalles de habilidades reales, ponemos valores 
                    // válidos por defecto para que el Builder de Java no lance 
                    // un IllegalArgumentException y cuelgue el programa.
                    // ========================================================
                    
                    CharacterBuilder builder = new CharacterBuilder()
                            .id(id)
                            .name(name)
                            .life(life)
                            .power(power);
                            
                    // FIX TEMPORAL: Instanciar habilidades vacías según el tipo
                    if ("werewolf".equalsIgnoreCase(race)) {
                        builder.specialSkill(new Gift("Temporal", 0, 1, 1));
                        return new Werewolf(builder);
                    } else if ("hunter".equalsIgnoreCase(race)) {
                        builder.specialSkill(new Talent("Temporal", 1, 1));
                        return new Hunter(builder);
                    } else { // vampiro por defecto
                        builder.specialSkill(new Discipline("Temporal", 1, 1, 1));
                        return new Vampire(builder);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al recuperar personaje: " + e.getMessage());
        }
        return null;
    }
}

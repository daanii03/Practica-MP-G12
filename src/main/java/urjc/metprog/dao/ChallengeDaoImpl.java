package main.java.urjc.metprog.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChallengeDaoImpl implements ChallengeDao {

    @Override
    public void addChallenge(int challengerId, int challengedId, int roundsPlayed, String combatDate, 
                             Integer winnerId, int challengerKeptMinions, int challengedKeptMinions, int goldWon) {
                             
        String sql = "INSERT INTO desafios (challenger_id, challenged_id, rounds_played, combat_date, " +
                     "winner_id, challenger_kept_minions, challenged_kept_minions, gold_won) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                     
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, challengerId);
            pstmt.setInt(2, challengedId);
            pstmt.setInt(3, roundsPlayed);
            pstmt.setString(4, combatDate);
            
            if (winnerId != null) {
                pstmt.setInt(5, winnerId);
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }
            
            pstmt.setInt(6, challengerKeptMinions);
            pstmt.setInt(7, challengedKeptMinions);
            pstmt.setInt(8, goldWon);
            
            pstmt.executeUpdate();
            System.out.println("Desafío guardado correctamente en el historial.");
            
        } catch (SQLException e) {
            System.err.println("Error al insertar el desafío: " + e.getMessage());
        }
    }

    @Override
    public void updateChallenge(int challengeId, int roundsPlayed, String combatDate, 
                                Integer winnerId, int challengerKeptMinions, int challengedKeptMinions, int goldWon) {
                                
        String sql = "UPDATE desafios SET rounds_played = ?, combat_date = ?, winner_id = ?, " +
                     "challenger_kept_minions = ?, challenged_kept_minions = ?, gold_won = ? " +
                     "WHERE id = ?";
                     
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, roundsPlayed);
            pstmt.setString(2, combatDate);
            
            if (winnerId != null) {
                pstmt.setInt(3, winnerId);
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }
            
            pstmt.setInt(4, challengerKeptMinions);
            pstmt.setInt(5, challengedKeptMinions);
            pstmt.setInt(6, goldWon);
            pstmt.setInt(7, challengeId);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Desafío " + challengeId + " actualizado correctamente.");
            } else {
                System.out.println("No se encontró el desafío con ID " + challengeId + ".");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar el desafío: " + e.getMessage());
        }
    }

    @Override
    public void deleteChallenge(int challengeId) {
        String sql = "DELETE FROM desafios WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, challengeId);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Desafío " + challengeId + " eliminado correctamente.");
            } else {
                System.out.println("No se encontró el desafío con ID " + challengeId + ".");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al borrar el desafío: " + e.getMessage());
        }
    }
}

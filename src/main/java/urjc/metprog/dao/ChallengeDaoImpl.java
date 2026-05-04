package main.java.urjc.metprog.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import main.java.urjc.metprog.models.challenges.Challenge;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public LocalDateTime getLastLossDate(int userId) {
        // Busca el combate más reciente donde el usuario participó pero NO fue el ganador
        String sql = "SELECT combat_date FROM desafios "
                   + "WHERE (challenger_id = ? OR challenged_id = ?) "
                   + "AND winner_id IS NOT NULL "
                   + "AND winner_id != ? "
                   + "ORDER BY combat_date DESC LIMIT 1";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String dateStr = rs.getString("combat_date");
                    return LocalDateTime.parse(dateStr);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar la última derrota del usuario: " + e.getMessage());
        }
        return null; // Nunca ha perdido o no tiene historial
    }

    @Override
    public java.util.List<String[]> getChallengeHistoryByUser(int userId) {
        java.util.List<String[]> history = new java.util.ArrayList<>();

        // JOIN triple: desafios + usuarios (challenger) + usuarios (challenged) + usuarios (winner opcional)
        String sql = "SELECT "
                   + "  uc.nick  AS challenger_nick, "
                   + "  ud.nick  AS challenged_nick, "
                   + "  d.rounds_played, "
                   + "  d.combat_date, "
                   + "  uw.nick  AS winner_nick, "
                   + "  d.gold_won "
                   + "FROM desafios d "
                   + "JOIN usuarios uc ON d.challenger_id = uc.id "
                   + "JOIN usuarios ud ON d.challenged_id = ud.id "
                   + "LEFT JOIN usuarios uw ON d.winner_id  = uw.id "
                   + "WHERE d.challenger_id = ? OR d.challenged_id = ? "
                   + "ORDER BY d.combat_date DESC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String[] row = {
                        rs.getString("challenger_nick"),   // [0]
                        rs.getString("challenged_nick"),   // [1]
                        String.valueOf(rs.getInt("rounds_played")), // [2]
                        rs.getString("combat_date"),       // [3]
                        rs.getString("winner_nick"),       // [4] null = empate
                        String.valueOf(rs.getInt("gold_won")) // [5]
                    };
                    history.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el historial de combates: " + e.getMessage());
        }
        return history;
    }
    public void savePendingChallenge(Challenge challenge) {
        String sql = "INSERT INTO desafios_pendientes " +
                "(challenger_nick, challenged_nick, gold_bet, status, created_at) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, challenge.getChallenger().getNick());
            pstmt.setString(2, challenge.getChallenged().getNick());
            pstmt.setInt(3, challenge.getGoldBet());
            pstmt.setString(4, challenge.getStatus().name());
            pstmt.setString(5, challenge.getCreatedAt().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar desafío pendiente: " + e.getMessage());
        }
    }

    public void updatePendingChallengeStatus(String challengerNick, String newStatus) {
        String sql = "UPDATE desafios_pendientes SET status = ? " +
                "WHERE challenger_nick = ? AND status != 'FINISHED' AND status != 'REJECTED'";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setString(2, challengerNick);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado: " + e.getMessage());
        }
    }

    public List<String[]> getPendingChallengesFromDb() {
        List<String[]> result = new ArrayList<>();
        String sql = "SELECT challenger_nick, challenged_nick, gold_bet, status, created_at " +
                "FROM desafios_pendientes " +
                "WHERE status = 'PENDING_VALIDATION' OR status = 'PUBLISHED'";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                result.add(new String[]{
                        rs.getString("challenger_nick"),
                        rs.getString("challenged_nick"),
                        String.valueOf(rs.getInt("gold_bet")),
                        rs.getString("status"),
                        rs.getString("created_at")
                });
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar desafíos pendientes: " + e.getMessage());
        }
        return result;
    }
}

    package main.java.urjc.metprog.dao;

    public interface ChallengeDao {

        /**
         * Añade un nuevo registro de desafío (historial de combate) a la base de datos.
         */
        void addChallenge(int challengerId, int challengedId, int roundsPlayed, String combatDate,
                          Integer winnerId, int challengerKeptMinions, int challengedKeptMinions, int goldWon);

        /**
         * Actualiza los datos de un desafío existente usando su ID.
         */
        void updateChallenge(int challengeId, int roundsPlayed, String combatDate,
                             Integer winnerId, int challengerKeptMinions, int challengedKeptMinions, int goldWon);

        /**
         * Elimina un registro de desafío por su ID.
         */
        void deleteChallenge(int challengeId);

        /**
         * Obtiene la fecha y hora de la última derrota de un usuario en un combate.
         * Devuelve null si nunca ha perdido.
         */
        java.time.LocalDateTime getLastLossDate(int userId);

        /**
         * Obtiene el historial de combates en los que ha participado un usuario.
         * Cada fila contiene: [challenger_nick, challenged_nick, rounds, combat_date, winner_nick, gold_won].
         * winner_nick es null si el combate fue un empate.
         *
         * @param userId ID del usuario cuyos combates se quieren listar.
         * @return Lista de arrays con los datos de cada combate.
         */
        java.util.List<String[]> getChallengeHistoryByUser(int userId);

        /**
         * Obtiene todos los desafíos pendientes de validación o publicados desde la base de datos.
         *
         * @return Lista de arrays con los datos de cada desafío pendiente.
         */
        java.util.List<String[]> getPendingChallengesFromDb();
    }

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
    }

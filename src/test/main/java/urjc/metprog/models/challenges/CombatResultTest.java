package test.main.java.urjc.metprog.models.challenges;

import main.java.urjc.metprog.models.challenges.Challenge;
import main.java.urjc.metprog.models.challenges.CombatResult;
import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CombatResultTest {

    // ══════════════════════════════════════════════════════════════════════
    // Stubs manuales
    // ══════════════════════════════════════════════════════════════════════

    static class StubCharacter extends Character {
        private int gold;

        StubCharacter(int gold) {
            this.gold = gold;
        }

        @Override public int checkGold() { return gold; }
        @Override public void increaseGold(int amount) { gold += amount; }
        @Override public void decreaseGold(int amount) { gold -= Math.min(gold, amount); }

        // Métodos abstractos mínimos para compilar
        @Override public int getLife() { return 10; }
        @Override public void decreaseLife(int amount) {}
        @Override public int getPower() { return 10; }
        @Override public String getName() { return "Dummy"; }
        @Override public int getSpecialAttack() { return 0; }
        @Override public int getSpecialDefense() { return 0; }
    }

    static class StubUser extends User {
        private final String nick;
        private final Character character;

        StubUser(String nick, Character character) {
            this.nick = nick;
            this.character = character;
        }

        @Override public String getNick() { return nick; }
        @Override public String getName() { return nick; }
        @Override public Character getCharacter() { return character; }
    }

    // ── Fixtures ───────────────────────────────────────────────────────────

    private StubUser challenger;
    private StubUser challenged;
    private Challenge challenge;
    private LocalDateTime date;
    private List<String> log;

    @BeforeEach
    void setUp() {
        challenger = new StubUser("Player1", new StubCharacter(100));
        challenged = new StubUser("Player2", new StubCharacter(100));

        // Creamos un reto válido con una apuesta de 30 de oro
        challenge = new Challenge(challenger, challenged, 30);

        date = LocalDateTime.of(2023, 10, 5, 15, 30);
        log = Arrays.asList("Round 1", "Round 2");
    }

    // ══════════════════════════════════════════════════════════════════════
    // Tests
    // ══════════════════════════════════════════════════════════════════════

    @Test
    void isDraw() {
        // Test con empate (winner es null)
        CombatResult drawResult = new CombatResult(
                challenge, null, 3, date, log, false, false);
        assertTrue(drawResult.isDraw());

        // Test con ganador
        CombatResult winResult = new CombatResult(
                challenge, challenger, 3, date, log, false, false);
        assertFalse(winResult.isDraw());
    }

    @Test
    void getChallenge() {
        CombatResult result = new CombatResult(
                challenge, null, 1, date, log, false, false);
        assertEquals(challenge, result.getChallenge());
    }

    @Test
    void getWinner() {
        CombatResult result = new CombatResult(
                challenge, challenger, 1, date, log, false, false);
        assertEquals(challenger, result.getWinner());
    }

    @Test
    void getRounds() {
        CombatResult result = new CombatResult(
                challenge, null, 5, date, log, false, false);
        assertEquals(5, result.getRounds());
    }

    @Test
    void getDate() {
        CombatResult result = new CombatResult(
                challenge, null, 1, date, log, false, false);
        assertEquals(date, result.getDate());
    }

    @Test
    void getRoundLog() {
        CombatResult result = new CombatResult(
                challenge, null, 1, date, log, false, false);
        assertEquals(log, result.getRoundLog());
        assertEquals(2, result.getRoundLog().size());
    }

    @Test
    void getGoldWon() {
        // Si hay empate, el oro ganado es 0
        CombatResult drawResult = new CombatResult(
                challenge, null, 1, date, log, false, false);
        assertEquals(0, drawResult.getGoldWon());

        // Si hay ganador, el oro ganado es la apuesta (30)
        CombatResult winResult = new CombatResult(
                challenge, challenger, 1, date, log, false, false);
        assertEquals(30, winResult.getGoldWon());
    }

    @Test
    void getWinnerUser() {
        CombatResult result = new CombatResult(
                challenge, challenged, 1, date, log, false, false);
        assertEquals(challenged, result.getWinnerUser());
    }

    @Test
    void getLoserUser() {
        // En caso de empate no hay perdedor
        CombatResult drawResult = new CombatResult(
                challenge, null, 1, date, log, false, false);
        assertNull(drawResult.getLoserUser());

        // Si gana el retador (challenger), el perdedor es el retado (challenged)
        CombatResult challengerWins = new CombatResult(
                challenge, challenger, 1, date, log, false, false);
        assertEquals(challenged, challengerWins.getLoserUser());

        // Si gana el retado (challenged), el perdedor es el retador (challenger)
        CombatResult challengedWins = new CombatResult(
                challenge, challenged, 1, date, log, false, false);
        assertEquals(challenger, challengedWins.getLoserUser());
    }

    @Test
    void challengerHasSurvivingMinions() {
        CombatResult result = new CombatResult(
                challenge, null, 1, date, log, true, false);
        assertTrue(result.challengerHasSurvivingMinions());
    }

    @Test
    void challengedHasSurvivingMinions() {
        CombatResult result = new CombatResult(
                challenge, null, 1, date, log, false, true);
        assertTrue(result.challengedHasSurvivingMinions());
    }

    @Test
    void testGoldTransferOnWin() {
        // Comprobamos el oro inicial
        assertEquals(100, challenger.getCharacter().checkGold());
        assertEquals(100, challenged.getCharacter().checkGold());

        // El retador gana la apuesta (30)
        new CombatResult(challenge, challenger, 1, date, log, false, false);

        // El retador debe tener 100 + 30 = 130
        assertEquals(130, challenger.getCharacter().checkGold());
        // El retado debe tener 100 - 30 = 70
        assertEquals(70, challenged.getCharacter().checkGold());
    }

    @Test
    void testNoGoldTransferOnDraw() {
        assertEquals(100, challenger.getCharacter().checkGold());
        assertEquals(100, challenged.getCharacter().checkGold());

        // Ocurre un empate
        new CombatResult(challenge, null, 1, date, log, false, false);

        // Nadie pierde ni gana oro
        assertEquals(100, challenger.getCharacter().checkGold());
        assertEquals(100, challenged.getCharacter().checkGold());
    }
}
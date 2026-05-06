package test.main.java.urjc.metprog.models.challenges;

import main.java.urjc.metprog.dao.UserDaoImpl;
import main.java.urjc.metprog.models.challenges.Challenge;
import main.java.urjc.metprog.models.challenges.Combat;
import main.java.urjc.metprog.models.challenges.CombatResult;
import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.Inventory;
import main.java.urjc.metprog.models.equipment.Weapon;
import main.java.urjc.metprog.models.minions.Minion;
import main.java.urjc.metprog.models.user.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CombatTest {

    // ══════════════════════════════════════════════════════════════════════
    // Stubs manuales para evitar NullPointerExceptions
    // ══════════════════════════════════════════════════════════════════════

    static class StubCharacter extends Character {
        private int life = 5;
        private final int power = 10;
        private final String name;

        // Creamos un inventario falso que devuelve listas vacías para que no explote
        private final Inventory stubInventory = new Inventory() {
            @Override
            public List<Weapon> getActiveWeapons() {
                return Collections.emptyList();
            }
            // getActiveArmor() puede devolver null sin problema porque Combat lo comprueba.
        };

        StubCharacter(String name) {
            this.name = name;
        }

        @Override public int getLife() { return life; }
        @Override public void decreaseLife(int amount) { life = Math.max(0, life - amount); }
        @Override public int getPower() { return power; }
        @Override public String getName() { return name; }

        @Override public int getSpecialAttack() { return 0; }
        @Override public int getSpecialDefense() { return 0; }
        @Override public int checkGold() { return 0; }
        @Override public void increaseGold(int amount) {}
        @Override public void decreaseGold(int amount) {}

        // --- SOLUCIÓN AL NULL POINTER EXCEPTION ---

        @Override
        public Inventory getInventory() {
            return stubInventory;
        }

        @Override
        public List<Minion> getMinions() {
            return Collections.emptyList();
        }

        @Override
        public List getStrengths() {
            return Collections.emptyList();
        }

        @Override
        public List getWeaknesses() {
            return Collections.emptyList();
        }
    }

    static class StubUser extends User {
        private final String nick;
        private final Character character;
        private int victories = 0;

        StubUser(String nick, Character character) {
            this.nick = nick;
            this.character = character;
        }

        @Override public String getNick() { return nick; }
        @Override public String getName() { return nick; }
        @Override public Character getCharacter() { return character; }
        @Override public int getVictories() { return victories; }
        @Override public void setVictories(int victories) { this.victories = victories; }
    }

    static class StubChallenge extends Challenge {
        private boolean isFinished = false;

        StubChallenge(User challenger, User challenged) {
            super(challenger, challenged, 0);
        }

        @Override public void finish() { this.isFinished = true; }
        public boolean isFinished() { return isFinished; }
    }

    static class StubUserDao extends UserDaoImpl {
        public boolean wasUpdated = false;

        @Override
        public void updateUser(User user) {
            this.wasUpdated = true;
        }
    }

    // ── Fixtures ───────────────────────────────────────────────────────────

    private StubUser challenger;
    private StubUser challenged;
    private StubChallenge challenge;
    private StubUserDao userDao;
    private Combat combat;

    @BeforeEach
    void setUp() {
        StubCharacter challengerChar = new StubCharacter("Hero");
        StubCharacter challengedChar = new StubCharacter("Villain");

        challenger = new StubUser("Jugador1", challengerChar);
        challenged = new StubUser("Jugador2", challengedChar);

        challenge = new StubChallenge(challenger, challenged);
        // Evitamos NullPointerExceptions en activeModifiers dándole uno inicializado o validándolo
        challenge.validate(Collections.emptyMap());
        challenge.accept();

        userDao = new StubUserDao();
        combat = new Combat(challenge, userDao);
    }

    // ══════════════════════════════════════════════════════════════════════
    // Tests
    // ══════════════════════════════════════════════════════════════════════

    @Test
    void run() {
        CombatResult result = combat.run();

        assertNotNull(result);
        assertTrue(challenge.isFinished());

        boolean someoneDied = challenger.getCharacter().getLife() == 0 || challenged.getCharacter().getLife() == 0;
        assertTrue(someoneDied);

        if (combat.getWinner() != null) {
            assertTrue(userDao.wasUpdated);
            assertEquals(1, combat.getWinner().getVictories());
        }
    }

    @Test
    void getRoundLog() {
        assertTrue(combat.getRoundLog().isEmpty());

        combat.run();

        assertFalse(combat.getRoundLog().isEmpty());
        assertTrue(combat.getRoundLog().get(0).contains("Round 1"));
    }

    @Test
    void getRoundCount() {
        assertEquals(0, combat.getRoundCount());

        combat.run();

        assertTrue(combat.getRoundCount() > 0);
    }

    @Test
    void getWinner() {
        assertNull(combat.getWinner());

        combat.run();

        if (challenger.getCharacter().getLife() == 0 && challenged.getCharacter().getLife() == 0) {
            // Empate
            assertNull(combat.getWinner());
        } else {
            // Hay un ganador
            assertNotNull(combat.getWinner());
            assertTrue(combat.getWinner() == challenger || combat.getWinner() == challenged);
        }
    }

    @Test
    void getCombatDate() {
        LocalDateTime beforeOrEqual = LocalDateTime.now().minusSeconds(1);
        LocalDateTime date = combat.getCombatDate();
        LocalDateTime afterOrEqual = LocalDateTime.now().plusSeconds(1);

        assertNotNull(date);
        assertTrue(date.isAfter(beforeOrEqual));
        assertTrue(date.isBefore(afterOrEqual));
    }
}
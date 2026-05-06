package test.main.java.urjc.metprog.controllers;

import main.java.urjc.metprog.controllers.ChallengeManager;
import main.java.urjc.metprog.models.challenges.Challenge;
import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeManagerTest {

    static class StubCharacter extends Character {
        @Override public int checkGold() { return 1000; }
        @Override public void increaseGold(int amount) {}
        @Override public void decreaseGold(int amount) {}
        @Override public int getLife() { return 10; }
        @Override public void decreaseLife(int amount) {}
        @Override public int getPower() { return 10; }
        @Override public String getName() { return "Stub"; }
        @Override public int getSpecialAttack() { return 0; }
        @Override public int getSpecialDefense() { return 0; }
    }

    static class StubUser extends User {
        private final String nick;
        private final Character character;

        StubUser(String nick) {
            this.nick = nick;
            this.character = new StubCharacter();
        }

        @Override public String getNick() { return nick; }
        @Override public Character getCharacter() { return character; }
    }

    private ChallengeManager manager;
    private StubUser user1;
    private StubUser user2;
    private StubUser user3;

    @BeforeEach
    void setUp() {
        manager = ChallengeManager.getInstance();

        List<Challenge> existingChallenges = manager.findAll();
        for (Challenge c : existingChallenges) {
            manager.delete(c);
        }

        user1 = new StubUser("Alice");
        user2 = new StubUser("Bob");
        user3 = new StubUser("Charlie");
    }

    @Test
    void getInstance() {
        ChallengeManager instance1 = ChallengeManager.getInstance();
        ChallengeManager instance2 = ChallengeManager.getInstance();

        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }

    @Test
    void save() {
        Challenge challenge = new Challenge(user1, user2, 10);
        manager.save(challenge);

        assertEquals(1, manager.findAll().size());
        assertTrue(manager.findAll().contains(challenge));
    }

    @Test
    void saveThrowsExceptionOnNull() {
        assertThrows(IllegalArgumentException.class, () -> manager.save(null));
    }

    @Test
    void delete() {
        Challenge challenge = new Challenge(user1, user2, 10);
        manager.save(challenge);
        assertEquals(1, manager.findAll().size());

        manager.delete(challenge);
        assertEquals(0, manager.findAll().size());
    }

    @Test
    void findAll() {
        Challenge c1 = new Challenge(user1, user2, 10);
        Challenge c2 = new Challenge(user2, user3, 20);

        manager.save(c1);
        manager.save(c2);

        List<Challenge> all = manager.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(c1));
        assertTrue(all.contains(c2));
    }

    @Test
    void findPendingValidation() {
        Challenge pending = new Challenge(user1, user2, 10);
        Challenge published = new Challenge(user2, user3, 20);
        published.validate(null);

        manager.save(pending);
        manager.save(published);

        List<Challenge> result = manager.findPendingValidation();
        assertEquals(1, result.size());
        assertTrue(result.contains(pending));
    }

    @Test
    void findPublishedChallengeForUser() {
        Challenge c1 = new Challenge(user1, user2, 10);
        c1.validate(null); // Estado: PUBLISHED, Challenged: Bob (user2)

        Challenge c2 = new Challenge(user3, user2, 10);
        // Estado: PENDING_VALIDATION, Challenged: Bob (user2)

        manager.save(c1);
        manager.save(c2);

        Optional<Challenge> resultUser2 = manager.findPublishedChallengeForUser(user2);
        assertTrue(resultUser2.isPresent());
        assertEquals(c1, resultUser2.get());

        Optional<Challenge> resultUser1 = manager.findPublishedChallengeForUser(user1);
        assertFalse(resultUser1.isPresent());
    }

    @Test
    void findByChallengerNick() {
        Challenge c1 = new Challenge(user1, user2, 10);
        Challenge c2 = new Challenge(user1, user3, 10);
        Challenge c3 = new Challenge(user2, user1, 10);

        manager.save(c1);
        manager.save(c2);
        manager.save(c3);

        List<Challenge> result = manager.findByChallengerNick("Alice");
        assertEquals(2, result.size());
        assertTrue(result.contains(c1));
        assertTrue(result.contains(c2));
    }

    @Test
    void findByChallengedNick() {
        Challenge c1 = new Challenge(user1, user2, 10);
        Challenge c2 = new Challenge(user3, user2, 10);
        Challenge c3 = new Challenge(user2, user1, 10);

        manager.save(c1);
        manager.save(c2);
        manager.save(c3);

        List<Challenge> result = manager.findByChallengedNick("Bob");
        assertEquals(2, result.size());
        assertTrue(result.contains(c1));
        assertTrue(result.contains(c2));
    }

    @Test
    void hasActiveChallenge() {
        Challenge challenge = new Challenge(user1, user2, 10);
        manager.save(challenge);

        assertTrue(manager.hasActiveChallenge(user1));
        assertTrue(manager.hasActiveChallenge(user2));
        assertFalse(manager.hasActiveChallenge(user3));

        challenge.validate(null);
        assertTrue(manager.hasActiveChallenge(user1));

        challenge.accept();
        assertFalse(manager.hasActiveChallenge(user1));
    }
}
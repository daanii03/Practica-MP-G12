package test.main.java.urjc.metprog.controllers;

import main.java.urjc.metprog.controllers.ChallengeManager;
import main.java.urjc.metprog.controllers.UserManager;
import main.java.urjc.metprog.dao.UserDao;
import main.java.urjc.metprog.models.challenges.Challenge;
import main.java.urjc.metprog.models.user.User;
import main.java.urjc.metprog.models.user.UserBuilder;
import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.Vampire;
import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.characters.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {

    private UserManager manager;
    private FakeUserDao fakeDao;


    class FakeUserDao implements UserDao {
        Map<String, User> database = new HashMap<>();

        @Override
        public User getUser(String nick) {
            return database.get(nick);
        }

        @Override
        public void insertUser(User user) {
            database.put(user.getNick(), user);
        }

        @Override
        public void updateUser(User user) {
            database.put(user.getNick(), user);
        }

        @Override
        public void deleteUser(String nick) {
            database.remove(nick);
        }

        @Override
        public User getUserByRegistrationNumber(String registrationNumber) {
            return database.values().stream()
                    .filter(u -> registrationNumber.equals(u.getRegistrationNumber()))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public List<User> getGlobalRanking() {
            return new ArrayList<>(database.values());
        }
    }

    @BeforeEach
    void setUp() {
        fakeDao = new FakeUserDao();
        manager = new UserManager(fakeDao);
    }

    @Test
    void signUp_Valid_CreatesPlayerSuccessfully() {
        User user = manager.signUp("Juan Pérez", "juanito", "password123");

        assertNotNull(user, "User should not be null");
        assertEquals("juanito", user.getNick(), "Nick should match");
        assertEquals("player", user.getRole(), "Default role should be 'player'");
        assertNotNull(user.getRegistrationNumber(), "Players must have a generated registration number");
        assertFalse(user.isBlocked(), "A new user should not be blocked");

        assertEquals(user, fakeDao.getUser("juanito"), "User should be saved in the DB");
    }

    @Test
    void signUp_ShortPassword_ThrowsException() {
        assertThrows(RuntimeException.class, () -> {
            manager.signUp("Juan", "juanito", "1234567"); // 7 caracteres minimos
        }, "Should throw an error for a password shorter than 8 characters");
    }

    @Test
    void signUp_NickAlreadyExists_ThrowsException() {
        fakeDao.insertUser(new UserBuilder().setNick("juanito").build());

        assertThrows(RuntimeException.class, () -> {
            manager.signUp("Otro Juan", "juanito", "password123");
        }, "Should throw an error because the nick 'juanito' is already taken");
    }

    @Test
    void signUpOperator_Valid_CreatesOperatorSuccessfully() {
        User operator = manager.signUpOperator("Admin Pro", "admin", "adminPass1");

        assertNotNull(operator, "Operator should not be null");
        assertEquals("operator", operator.getRole(), "Role should be 'operator'");
        assertNull(operator.getRegistrationNumber(), "Operators must NOT have a registration number");
        assertEquals(operator, fakeDao.getUser("admin"), "Operator should be saved in the DB");
    }

    @Test
    void logIn_ValidCredentials_ReturnsUser() {
        User realUser = new UserBuilder().setNick("pepe").setPassword("secreta123").setBlocked(false).build();
        fakeDao.insertUser(realUser);

        User loggedUser = manager.logIn("pepe", "secreta123");
        assertNotNull(loggedUser, "Login should be successful and return the user");
        assertEquals("pepe", loggedUser.getNick());
    }

    @Test
    void logIn_InvalidPassword_ReturnsNull() {
        User realUser = new UserBuilder().setNick("pepe").setPassword("secreta123").build();
        fakeDao.insertUser(realUser);

        User loggedUser = manager.logIn("pepe", "claveMal");
        assertNull(loggedUser, "Login with bad password should return null");
    }

    @Test
    void logIn_BlockedUser_ThrowsException() {
        User blockedUser = new UserBuilder().setNick("hacker").setPassword("pass1234").setBlocked(true).build();
        fakeDao.insertUser(blockedUser);

        assertThrows(RuntimeException.class, () -> {
            manager.logIn("hacker", "pass1234");
        }, "A blocked user should not be able to log in");
    }

    @Test
    void deleteUser_ValidCredentials_DeletesUser() {
        User user = new UserBuilder().setNick("borrable").setPassword("pass1234").setBlocked(false).build();
        fakeDao.insertUser(user);

        manager.deleteUser("borrable", "pass1234");
        assertNull(fakeDao.getUser("borrable"), "User should have been deleted from the DB");
    }

    @Test
    void deleteUser_InvalidCredentials_ThrowsException() {
        User user = new UserBuilder().setNick("intocable").setPassword("pass1234").build();
        fakeDao.insertUser(user);

        assertThrows(RuntimeException.class, () -> {
            manager.deleteUser("intocable", "claveFalsa");
        }, "Should not allow account deletion if the password is incorrect");
    }

    @Test
    void blockUser_Valid_ChangesStateToBlocked() {
        User user = new UserBuilder().setNick("troll").setBlocked(false).build();
        fakeDao.insertUser(user);

        manager.blockUser("troll");
        assertTrue(fakeDao.getUser("troll").isBlocked(), "User state should change to blocked (true)");
    }

    @Test
    void unblockUser_Valid_ChangesStateToUnblocked() {
        User user = new UserBuilder().setNick("arrepentido").setBlocked(true).build();
        fakeDao.insertUser(user);

        manager.unblockUser("arrepentido");
        assertFalse(fakeDao.getUser("arrepentido").isBlocked(), "User state should change to unblocked (false)");
    }

    @Test
    void rejectInvalidChallengeAndBlock_BlocksChallengerAndDeletesChallenge() {
        Inventory inv = new Inventory();
        inv.setGold(100);

        Character charDummy = new Vampire(new CharacterBuilder()
                .inventory(inv)
                .power(5) //max 5
                .life(5)); //max 5

        User challenger = new UserBuilder().setNick("retador_tramposo").setCharacter(charDummy).build();
        User defender = new UserBuilder().setNick("defensor_inocente").setCharacter(charDummy).build();

        fakeDao.insertUser(challenger);

        Challenge challenge = new Challenge(challenger, defender, 10);
        ChallengeManager.getInstance().save(challenge);

        manager.rejectInvalidChallengeAndBlock(challenge);

        assertTrue(fakeDao.getUser("retador_tramposo").isBlocked(), "Challenger should have been blocked");
        assertFalse(ChallengeManager.getInstance().findPendingValidation().contains(challenge),
                "Challenge should have been deleted from ChallengeManager");
    }
}
package test.main.java.urjc.metprog.dao;

import main.java.urjc.metprog.dao.ChallengeDaoImpl;
import main.java.urjc.metprog.dao.DatabaseManager;
import main.java.urjc.metprog.dao.UserDaoImpl;
import main.java.urjc.metprog.models.challenges.Challenge;
import main.java.urjc.metprog.models.user.User;
import main.java.urjc.metprog.models.user.UserBuilder;
import main.java.urjc.metprog.models.characters.*;
import main.java.urjc.metprog.models.skills.Discipline;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeDaoImplTest {

    private ChallengeDaoImpl challengeDao;
    private UserDaoImpl userDao;
    private int user1Id, user2Id;

    @BeforeEach
    void setUp() {
        DatabaseManager.useTestDatabase();
        DatabaseManager.createTables();
        challengeDao = new ChallengeDaoImpl();
        userDao = new UserDaoImpl();

        // Crear usuarios para los combates
        User u1 = new UserBuilder().setNick("user1").setName("User One").setPassword("pass1").build();
        User u2 = new UserBuilder().setNick("user2").setName("User Two").setPassword("pass2").build();
        userDao.insertUser(u1);
        userDao.insertUser(u2);
        user1Id = userDao.getUserId("user1");
        user2Id = userDao.getUserId("user2");
    }

    @AfterEach
    void tearDown() {
        DatabaseManager.useDefaultDatabase();
    }

    @Test
    void addChallenge_And_GetHistory_ReturnsCorrectData() {
        String date = LocalDateTime.now().toString();
        challengeDao.addChallenge(user1Id, user2Id, 5, date, user1Id, 1, 0, 100);

        List<String[]> history = challengeDao.getChallengeHistoryByUser(user1Id);
        assertEquals(1, history.size());
        assertEquals("user1", history.get(0)[0]);
        assertEquals("user2", history.get(0)[1]);
        assertEquals("5", history.get(0)[2]);
        assertEquals("user1", history.get(0)[4]);
        assertEquals("100", history.get(0)[5]);
    }

    @Test
    void getLastLossDate_ReturnsMostRecentLoss() {
        String oldDate = LocalDateTime.now().minusDays(2).toString();
        String recentDate = LocalDateTime.now().minusHours(1).toString();

        // Combate 1: user1 pierde (user2 gana) hace 2 días
        challengeDao.addChallenge(user1Id, user2Id, 3, oldDate, user2Id, 0, 1, 50);
        // Combate 2: user1 pierde (user2 gana) hace 1 hora
        challengeDao.addChallenge(user1Id, user2Id, 4, recentDate, user2Id, 0, 1, 50);

        LocalDateTime lastLoss = challengeDao.getLastLossDate(user1Id);
        assertNotNull(lastLoss);
        // El parseo puede perder algo de precisión de nanosegundos dependiendo de la versión de SQLite, 
        // pero comparamos hasta los segundos
        assertTrue(lastLoss.isAfter(LocalDateTime.now().minusHours(2)));
    }

    @Test
    void savePendingChallenge_And_GetPending_Works() {
        User u1 = userDao.getUser("user1");
        User u2 = userDao.getUser("user2");
        
        Inventory inv = new Inventory();
        inv.setGold(500);
        CharacterBuilder cb = new CharacterBuilder().name("C1").life(1).power(1).specialSkill(new Discipline("D", 1, 1, 1)).inventory(inv);
        u1.setCharacter(new Vampire(cb));

        Challenge challenge = new Challenge(u1, u2, 500);

        challengeDao.savePendingChallenge(challenge);
        List<String[]> pending = challengeDao.getPendingChallengesFromDb();

        assertEquals(1, pending.size());
        assertEquals("user1", pending.get(0)[0]);
        assertEquals("user2", pending.get(0)[1]);
        assertEquals("500", pending.get(0)[2]);
    }
}

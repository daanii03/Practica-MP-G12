package test.main.java.urjc.metprog.dao;

import main.java.urjc.metprog.dao.CharacterDaoImpl;
import main.java.urjc.metprog.dao.DatabaseManager;
import main.java.urjc.metprog.dao.UserDaoImpl;
import main.java.urjc.metprog.models.characters.*;
import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.equipment.Armor;
import main.java.urjc.metprog.models.equipment.Weapon;
import main.java.urjc.metprog.models.minions.GhoulMinion;
import main.java.urjc.metprog.models.modifiers.Strength;
import main.java.urjc.metprog.models.modifiers.Weakness;
import main.java.urjc.metprog.models.skills.Discipline;
import main.java.urjc.metprog.models.user.User;
import main.java.urjc.metprog.models.user.UserBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class CharacterDaoImplTest {

    private CharacterDaoImpl characterDao;
    private UserDaoImpl userDao;

    @BeforeEach
    void setUp() {
        DatabaseManager.useTestDatabase();
        DatabaseManager.createTables();
        characterDao = new CharacterDaoImpl();
        userDao = new UserDaoImpl();
        
        // Crear un usuario dueño para los tests
        userDao.insertUser(new UserBuilder().setNick("owner").setName("Test Owner").setPassword("12345678").build());
    }

    @AfterEach
    void tearDown() {
        DatabaseManager.useDefaultDatabase();
    }

    @Test
    void insertAndGetCharacter_ComplexVampire_ReturnsFullStructure() {
        Inventory inv = new Inventory();
        inv.setGold(500);
        inv.addWeapon(new Weapon("Sun Sword", 3, 1, true));
        inv.addArmor(new Armor("Moon Shield", 0, 2));

        CharacterBuilder builder = new CharacterBuilder()
                .name("Alucard")
                .life(5)
                .power(4)
                .specialSkill(new Discipline("Blood Lust", 2, 3, 1))
                .inventory(inv);
        
        Vampire vampire = new Vampire(builder);
        vampire.setAge(100);
        vampire.setBloodPoints(5);
        vampire.addMinion(new GhoulMinion("Renfield", 2, 5));
        vampire.addStrength(new Strength("Nocturnal", 2));
        vampire.addWeakness(new Weakness("Silver", 3));

        characterDao.insertCharacter(vampire, "owner");
        Character recovered = characterDao.getCharacterByOwner("owner");

        assertNotNull(recovered);
        assertTrue(recovered instanceof Vampire);
        assertEquals("Alucard", recovered.getName());
        assertEquals(500, recovered.checkGold());
        assertEquals(1, recovered.getMinions().size());
        assertEquals("Renfield", recovered.getMinions().get(0).getName());
        assertEquals(1, recovered.getInventory().getWeapons().size());
        assertEquals(1, recovered.getInventory().getArmors().size());
        assertEquals(1, recovered.getStrengths().size());
        assertEquals(1, recovered.getWeaknesses().size());
    }

    @Test
    void deleteCharacter_RemovesCharacterAndSecondaryData() {
        CharacterBuilder builder = new CharacterBuilder().name("Disposable").life(1).power(1).specialSkill(new Discipline("D", 1, 1, 1)).inventory(new Inventory());
        Vampire v = new Vampire(builder);
        v.addMinion(new GhoulMinion("M", 1, 1));
        
        characterDao.insertCharacter(v, "owner");
        int id = v.getId();
        assertNotNull(characterDao.getCharacter(id));

        characterDao.deleteCharacter(id);
        assertNull(characterDao.getCharacter(id));
        
        // El usuario debería seguir existiendo pero con character_id null
        User user = userDao.getUser("owner");
        assertNotNull(user);
    }
}

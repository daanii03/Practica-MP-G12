package test.main.java.urjc.metprog.models.factorys;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.Vampire;
import main.java.urjc.metprog.models.factorys.VampireFactory;
import main.java.urjc.metprog.models.skills.Discipline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VampireFactoryTest {

    private VampireFactory vampireFactory;

    @BeforeEach
    public void setUp() {
        vampireFactory = new VampireFactory();
    }

    @Test
    public void testCreateVampire() {
        Character vampire = vampireFactory.createCharacter(3, "Dracula");

        assertNotNull(vampire);
        assertTrue(vampire instanceof Vampire);
        assertEquals(3, vampire.getId());
        assertEquals("Dracula", vampire.getName());
        assertEquals(5, vampire.getLife());
        assertEquals(5, vampire.getPower());
        assertNotNull(vampire.getInventory());
        assertNotNull(vampire.getSpecialSkill());
        assertTrue(vampire.getSpecialSkill() instanceof Discipline);
    }
}
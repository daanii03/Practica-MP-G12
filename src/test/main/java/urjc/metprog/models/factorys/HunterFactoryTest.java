package test.main.java.urjc.metprog.models.factorys;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.Hunter;
import main.java.urjc.metprog.models.factorys.HunterFactory;
import main.java.urjc.metprog.models.skills.Talent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HunterFactoryTest {

    private HunterFactory hunterFactory;

    @BeforeEach
    public void setUp() {
        hunterFactory = new HunterFactory();
    }

    @Test
    public void testCreateHunter() {
        Character hunter = hunterFactory.createCharacter(2, "Van Helsing");

        assertNotNull(hunter);
        assertTrue(hunter instanceof Hunter);
        assertEquals(2, hunter.getId());
        assertEquals("Van Helsing", hunter.getName());
        assertEquals(5, hunter.getLife());
        assertEquals(5, hunter.getPower());
        assertNotNull(hunter.getInventory());
        assertNotNull(hunter.getSpecialSkill());
        assertTrue(hunter.getSpecialSkill() instanceof Talent);
    }
}
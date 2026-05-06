package test.main.java.urjc.metprog.models.factorys;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.Werewolf;
import main.java.urjc.metprog.models.factorys.WerewolfFactory;
import main.java.urjc.metprog.models.skills.Gift;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WerewolfFactoryTest {

    private WerewolfFactory werewolfFactory;

    @BeforeEach
    public void setUp() {
        werewolfFactory = new WerewolfFactory();
    }

    @Test
    public void testCreateWerewolf() {
        Character werewolf = werewolfFactory.createCharacter(1, "Lucian");

        assertNotNull(werewolf);
        assertTrue(werewolf instanceof Werewolf);
        assertEquals(1, werewolf.getId());
        assertEquals("Lucian", werewolf.getName());
        assertEquals(5, werewolf.getLife());
        assertEquals(5, werewolf.getPower());
        assertNotNull(werewolf.getInventory());
        assertNotNull(werewolf.getSpecialSkill());
        assertTrue(werewolf.getSpecialSkill() instanceof Gift);
    }
}
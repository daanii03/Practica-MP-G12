package test.main.java.urjc.metprog.models.skills;

import main.java.urjc.metprog.models.skills.Talent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TalentTest {

    @Test
    public void testValidCreation() {
        Talent talent = new Talent("Disparo Rápido", 3, 1);
        assertEquals("Disparo Rápido", talent.getName());
        assertEquals(3, talent.getAttackValue());
        assertEquals(1, talent.getDefenseValue());
    }
}
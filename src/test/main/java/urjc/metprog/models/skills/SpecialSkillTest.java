package test.main.java.urjc.metprog.models.skills;

import main.java.urjc.metprog.models.skills.SpecialSkill;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpecialSkillTest {

    @Test
    public void testValidCreation() {
        SpecialSkill skill = new SpecialSkill("TestSkill", 2, 2) {};
        assertEquals("TestSkill", skill.getName());
        assertEquals(2, skill.getAttackValue());
        assertEquals(2, skill.getDefenseValue());
    }

    @Test
    public void testInvalidAttackValues() {
        assertThrows(IllegalArgumentException.class, () -> new SpecialSkill("TestSkill", 0, 2) {});
        assertThrows(IllegalArgumentException.class, () -> new SpecialSkill("TestSkill", 4, 2) {});
    }

    @Test
    public void testInvalidDefenseValues() {
        assertThrows(IllegalArgumentException.class, () -> new SpecialSkill("TestSkill", 2, 0) {});
        assertThrows(IllegalArgumentException.class, () -> new SpecialSkill("TestSkill", 2, 4) {});
    }

    @Test
    public void testSettersValidValues() {
        SpecialSkill skill = new SpecialSkill("TestSkill", 1, 1) {};
        skill.setName("NewName");
        skill.setAttackValue(3);
        skill.setDefenseValue(3);

        assertEquals("NewName", skill.getName());
        assertEquals(3, skill.getAttackValue());
        assertEquals(3, skill.getDefenseValue());
    }

    @Test
    public void testSettersInvalidValues() {
        SpecialSkill skill = new SpecialSkill("TestSkill", 2, 2) {};
        assertThrows(IllegalArgumentException.class, () -> skill.setAttackValue(0));
        assertThrows(IllegalArgumentException.class, () -> skill.setDefenseValue(4));
    }
}
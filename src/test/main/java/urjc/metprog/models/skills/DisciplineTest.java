package test.main.java.urjc.metprog.models.skills;

import main.java.urjc.metprog.models.skills.Discipline;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DisciplineTest {

    @Test
    public void testValidCreation() {
        Discipline discipline = new Discipline("Vuelo", 2, 1, 3);
        assertEquals("Vuelo", discipline.getName());
        assertEquals(2, discipline.getBloodPointsCost());
        assertEquals(1, discipline.getAttackValue());
        assertEquals(3, discipline.getDefenseValue());
    }

    @Test
    public void testInvalidBloodPointsCostCreation() {
        assertThrows(IllegalArgumentException.class, () -> new Discipline("TestDiscipline", 0, 2, 2));
        assertThrows(IllegalArgumentException.class, () -> new Discipline("TestDiscipline", 4, 2, 2));
    }

    @Test
    public void testSetBloodPointsCost() {
        Discipline discipline = new Discipline("TestDiscipline", 1, 2, 2);
        discipline.setBloodPointsCost(3);
        assertEquals(3, discipline.getBloodPointsCost());
    }

    @Test
    public void testSetInvalidBloodPointsCost() {
        Discipline discipline = new Discipline("TestDiscipline", 1, 2, 2);
        assertThrows(IllegalArgumentException.class, () -> discipline.setBloodPointsCost(0));
        assertThrows(IllegalArgumentException.class, () -> discipline.setBloodPointsCost(4));
    }
}
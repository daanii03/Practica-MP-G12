package test.main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.characters.Vampire;
import main.java.urjc.metprog.models.minions.HumanMinion;
import main.java.urjc.metprog.models.minions.Loyalty;
import main.java.urjc.metprog.models.minions.Minion;
import main.java.urjc.metprog.models.skills.Discipline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VampireTest {

    private Vampire vampire;

    @BeforeEach
    public void setUp() {
        Discipline discipline = new Discipline("TestDiscipline", 3, 2, 2) {
            @Override
            public int getAttackValue() {
                return 7;
            }

            @Override
            public int getDefenseValue() {
                return 5;
            }
        };

        CharacterBuilder builder = new CharacterBuilder()
                .name("Vlad")
                .life(3)
                .power(4)
                .specialSkill(discipline);

        vampire = new Vampire(builder);
    }

    @Test
    public void testDefaultValues() {
        assertEquals(10, vampire.getBloodPoints());
        assertEquals(100, vampire.getAge());
    }

    @Test
    public void testSetValidBloodPoints() {
        vampire.setBloodPoints(5);
        assertEquals(5, vampire.getBloodPoints());
    }

    @Test
    public void testSetInvalidBloodPoints() {
        assertThrows(IllegalArgumentException.class, () -> vampire.setBloodPoints(-1));
        assertThrows(IllegalArgumentException.class, () -> vampire.setBloodPoints(11));
    }

    @Test
    public void testSetAge() {
        vampire.setAge(200);
        assertEquals(200, vampire.getAge());
    }

    @Test
    public void testGetSpecialDefenseEnoughBlood() {
        vampire.setBloodPoints(5);
        assertEquals(5, vampire.getSpecialDefense());
    }

    @Test
    public void testGetSpecialDefenseNotEnoughBlood() {
        vampire.setBloodPoints(2);
        assertEquals(0, vampire.getSpecialDefense());
    }

    @Test
    public void testGetSpecialAttackEnoughBlood() {
        vampire.setBloodPoints(10);
        assertEquals(7, vampire.getSpecialAttack());
        assertEquals(7, vampire.getBloodPoints());
    }

    @Test
    public void testGetSpecialAttackNotEnoughBlood() {
        vampire.setBloodPoints(2);
        assertEquals(0, vampire.getSpecialAttack());
        assertEquals(2, vampire.getBloodPoints());
    }

    @Test
    public void testAddHumanMinionThrowsException() {
        HumanMinion human = new HumanMinion("Humano", 2, Loyalty.NORMAL);
        assertThrows(IllegalArgumentException.class, () -> vampire.addMinion(human));
    }

    @Test
    public void testAddOtherMinion() {
        Minion ghoul = new Minion("Gule", 2) {};
        assertDoesNotThrow(() -> vampire.addMinion(ghoul));
    }
}
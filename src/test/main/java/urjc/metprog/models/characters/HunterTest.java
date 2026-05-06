package test.main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.characters.Hunter;
import main.java.urjc.metprog.models.skills.SpecialSkill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HunterTest {

    private Hunter hunter;

    @BeforeEach
    public void setUp() {
        SpecialSkill skill = new SpecialSkill("TestSkill", 2, 2) {
            @Override
            public int getAttackValue() {
                return 8;
            }

            @Override
            public int getDefenseValue() {
                return 6;
            }
        };

        CharacterBuilder builder = new CharacterBuilder()
                .name("Van Helsing")
                .life(5)
                .power(3)
                .specialSkill(skill);

        hunter = new Hunter(builder);
    }

    @Test
    public void testDefaultValues() {
        assertEquals(3, hunter.getWill());
    }

    @Test
    public void testSetValidWill() {
        hunter.setWill(1);
        assertEquals(1, hunter.getWill());
    }

    @Test
    public void testSetInvalidWill() {
        assertThrows(IllegalArgumentException.class, () -> hunter.setWill(-1));
        assertThrows(IllegalArgumentException.class, () -> hunter.setWill(4));
    }

    @Test
    public void testDecreaseWill() {
        hunter.decreaseWill();
        assertEquals(2, hunter.getWill());
        hunter.decreaseWill();
        hunter.decreaseWill();
        hunter.decreaseWill();
        assertEquals(0, hunter.getWill());
    }

    @Test
    public void testIncreaseWill() {
        hunter.setWill(1);
        hunter.increaseWill();
        assertEquals(2, hunter.getWill());
        hunter.increaseWill();
        hunter.increaseWill();
        assertEquals(3, hunter.getWill());
    }

    @Test
    public void testResetWill() {
        hunter.setWill(0);
        hunter.resetWill();
        assertEquals(3, hunter.getWill());
    }

    @Test
    public void testGetSpecialAttack() {
        assertEquals(8, hunter.getSpecialAttack());
    }

    @Test
    public void testGetSpecialDefense() {
        assertEquals(6, hunter.getSpecialDefense());
    }
}
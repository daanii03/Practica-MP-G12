package test.main.java.urjc.metprog.models.equipment;

import main.java.urjc.metprog.models.equipment.Armor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArmorTest {

    @Test
    public void testValidCreation() {
        Armor armor = new Armor("Escudo", 0, 3);
        assertEquals("Escudo", armor.getName());
        assertEquals(0, armor.getAttackModifier());
        assertEquals(3, armor.getDefenseModifier());
    }

    @Test
    public void testEqualsAndHashCode() {
        Armor a1 = new Armor("Cota", 0, 2);
        Armor a2 = new Armor("Cota", 0, 2);
        Armor a3 = new Armor("Cota", 1, 2);
        Armor a4 = new Armor("Placas", 0, 2);

        assertEquals(a1, a1);
        assertEquals(a1, a2);
        assertNotEquals(a1, a3);
        assertNotEquals(a1, a4);
        assertNotEquals(a1, null);
        assertNotEquals(a1, new Object());

        assertEquals(a1.hashCode(), a2.hashCode());
    }
}
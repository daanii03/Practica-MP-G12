package test.main.java.urjc.metprog.models.equipment;

import main.java.urjc.metprog.models.equipment.Weapon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WeaponTest {

    @Test
    public void testValidCreation() {
        Weapon weapon = new Weapon("Espada", 3, 1, true);
        assertEquals("Espada", weapon.getName());
        assertEquals(3, weapon.getAttackModifier());
        assertEquals(1, weapon.getDefenseModifier());
        assertTrue(weapon.isOneHandWeapon());
    }

    @Test
    public void testSetOneHandWeapon() {
        Weapon weapon = new Weapon("Hacha", 2, 0, false);
        weapon.setOneHandWeapon(true);
        assertTrue(weapon.isOneHandWeapon());
    }

    @Test
    public void testEqualsAndHashCode() {
        Weapon w1 = new Weapon("Daga", 1, 0, true);
        Weapon w2 = new Weapon("Daga", 1, 0, true);
        Weapon w3 = new Weapon("Daga", 2, 0, true);
        Weapon w4 = new Weapon("Espada", 1, 0, true);
        Weapon w5 = new Weapon("Daga", 1, 0, false);

        assertEquals(w1, w1);
        assertEquals(w1, w2);
        assertNotEquals(w1, w3);
        assertNotEquals(w1, w4);
        assertNotEquals(w1, w5);
        assertNotEquals(w1, null);
        assertNotEquals(w1, new Object());

        assertEquals(w1.hashCode(), w2.hashCode());
    }
}
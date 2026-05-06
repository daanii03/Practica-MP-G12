package test.main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.characters.Inventory;
import main.java.urjc.metprog.models.equipment.Armor;
import main.java.urjc.metprog.models.equipment.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    private Inventory inventory;

    @BeforeEach
    public void setUp() {
        inventory = new Inventory();
    }

    @Test
    public void testDefaultGold() {
        assertEquals(100, inventory.getGold());
    }

    @Test
    public void testSetValidGold() {
        inventory.setGold(50);
        assertEquals(50, inventory.getGold());
    }

    @Test
    public void testSetInvalidGold() {
        assertThrows(IllegalArgumentException.class, () -> inventory.setGold(-10));
    }

    @Test
    public void testAddGold() {
        inventory.addGold(50);
        assertEquals(150, inventory.getGold());
    }

    @Test
    public void testDecreaseGold() {
        inventory.decreaseGold(50);
        assertEquals(50, inventory.getGold());
    }

    @Test
    public void testDecreaseGoldBelowZero() {
        inventory.decreaseGold(150);
        assertEquals(0, inventory.getGold());
    }

    @Test
    public void testAddAndRemoveArmor() {
        Armor armor = new Armor("Armadura Test", 0, 2);
        inventory.addArmor(armor);
        assertTrue(inventory.getArmors().contains(armor));

        inventory.setActiveArmor(armor);
        inventory.removeArmor(armor);
        assertFalse(inventory.getArmors().contains(armor));
        assertNull(inventory.getActiveArmor());
    }

    @Test
    public void testSetActiveArmorNotOwned() {
        Armor armor = new Armor("Armadura Test", 0, 2);
        assertThrows(IllegalArgumentException.class, () -> inventory.setActiveArmor(armor));
    }

    @Test
    public void testAddAndRemoveWeapon() {
        Weapon weapon = new Weapon("Arma Test", 2, 0, true);
        inventory.addWeapon(weapon);
        assertTrue(inventory.getWeapons().contains(weapon));

        inventory.removeWeapon(weapon);
        assertFalse(inventory.getWeapons().contains(weapon));
    }

    @Test
    public void testSetActiveWeaponsValid() {
        Weapon w1 = new Weapon("Arma 1", 2, 0, true);
        Weapon w2 = new Weapon("Arma 2", 2, 0, true);

        inventory.addWeapon(w1);
        inventory.addWeapon(w2);

        List<Weapon> active = new ArrayList<>();
        active.add(w1);
        active.add(w2);

        inventory.setActiveWeapons(active);
        assertEquals(2, inventory.getActiveWeapons().size());
        assertTrue(inventory.getActiveWeapons().contains(w1));
        assertTrue(inventory.getActiveWeapons().contains(w2));
    }

    @Test
    public void testSetActiveWeaponsNull() {
        assertThrows(IllegalArgumentException.class, () -> inventory.setActiveWeapons(null));
    }

    @Test
    public void testSetActiveWeaponsTooMany() {
        List<Weapon> active = new ArrayList<>();
        active.add(new Weapon("Arma 1", 1, 0, true));
        active.add(new Weapon("Arma 2", 1, 0, true));
        active.add(new Weapon("Arma 3", 1, 0, true));

        assertThrows(IllegalArgumentException.class, () -> inventory.setActiveWeapons(active));
    }

    @Test
    public void testSetActiveWeaponsNotOwned() {
        Weapon w1 = new Weapon("Arma 1", 1, 0, true);
        List<Weapon> active = new ArrayList<>();
        active.add(w1);

        assertThrows(IllegalArgumentException.class, () -> inventory.setActiveWeapons(active));
    }

    @Test
    public void testSetActiveWeaponsTwoHandedViolation() {
        Weapon w1 = new Weapon("Arma 2H", 3, 0, false);
        Weapon w2 = new Weapon("Arma 1H", 1, 0, true);

        inventory.addWeapon(w1);
        inventory.addWeapon(w2);

        List<Weapon> active = new ArrayList<>();
        active.add(w1);
        active.add(w2);

        assertThrows(IllegalArgumentException.class, () -> inventory.setActiveWeapons(active));
    }
}
package test.main.java.urjc.metprog.models.equipment;

import main.java.urjc.metprog.models.equipment.Weapon;
import main.java.urjc.metprog.models.equipment.WeaponCatalogue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WeaponCatalogueTest {

    @Test
    public void testGenerateWeaponEspadaCelestial() {
        Weapon weapon = WeaponCatalogue.ESPADA_CELESTIAL.generateWeapon();
        assertEquals("Espada Celestial", weapon.getName());
        assertEquals(3, weapon.getAttackModifier());
        assertEquals(1, weapon.getDefenseModifier());
        assertTrue(weapon.isOneHandWeapon());
    }

    @Test
    public void testGenerateWeaponHachaPesada() {
        Weapon weapon = WeaponCatalogue.HACHA_DE_GUERRA_PESADA.generateWeapon();
        assertEquals("Hacha de Guerra Pesada", weapon.getName());
        assertEquals(3, weapon.getAttackModifier());
        assertEquals(0, weapon.getDefenseModifier());
        assertFalse(weapon.isOneHandWeapon());
    }
}
package test.main.java.urjc.metprog.models.equipment;

import main.java.urjc.metprog.models.equipment.Armor;
import main.java.urjc.metprog.models.equipment.ArmorCatalogue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArmorCatalogueTest {

    @Test
    public void testGenerateArmorPlacasEbano() {
        Armor armor = ArmorCatalogue.ARMADURA_DE_PLACAS_EBANO.generateArmor();
        assertEquals("Armadura de Placas de Ébano", armor.getName());
        assertEquals(0, armor.getAttackModifier());
        assertEquals(3, armor.getDefenseModifier());
    }

    @Test
    public void testGenerateArmorMantoSangre() {
        Armor armor = ArmorCatalogue.MANTO_DE_SANGRE.generateArmor();
        assertEquals("Manto de Sangre", armor.getName());
        assertEquals(2, armor.getAttackModifier());
        assertEquals(1, armor.getDefenseModifier());
    }
}
package test.main.java.urjc.metprog.models.equipment;

import main.java.urjc.metprog.models.equipment.Equipment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EquipmentTest {

    @Test
    public void testValidCreation() {
        Equipment equipment = new Equipment("Test Equipment", 2, 2) {};
        assertEquals("Test Equipment", equipment.getName());
        assertEquals(2, equipment.getAttackModifier());
        assertEquals(2, equipment.getDefenseModifier());
    }

    @Test
    public void testInvalidCreationAttackBelowMin() {
        assertThrows(IllegalArgumentException.class, () -> new Equipment("Test", -1, 2) {});
    }

    @Test
    public void testInvalidCreationAttackAboveMax() {
        assertThrows(IllegalArgumentException.class, () -> new Equipment("Test", 4, 2) {});
    }

    @Test
    public void testInvalidCreationDefenseBelowMin() {
        assertThrows(IllegalArgumentException.class, () -> new Equipment("Test", 2, -1) {});
    }

    @Test
    public void testInvalidCreationDefenseAboveMax() {
        assertThrows(IllegalArgumentException.class, () -> new Equipment("Test", 2, 4) {});
    }

    @Test
    public void testSettersValid() {
        Equipment equipment = new Equipment("Test", 1, 1) {};
        equipment.setName("New Test");
        equipment.setAttackModifier(3);
        equipment.setDefenseModifier(3);

        assertEquals("New Test", equipment.getName());
        assertEquals(3, equipment.getAttackModifier());
        assertEquals(3, equipment.getDefenseModifier());
    }

    @Test
    public void testSettersInvalid() {
        Equipment equipment = new Equipment("Test", 1, 1) {};
        assertThrows(IllegalArgumentException.class, () -> equipment.setAttackModifier(4));
        assertThrows(IllegalArgumentException.class, () -> equipment.setDefenseModifier(-1));
    }
}
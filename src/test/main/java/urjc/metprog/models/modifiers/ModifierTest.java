package test.main.java.urjc.metprog.models.modifiers;

import main.java.urjc.metprog.models.modifiers.Modifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ModifierTest {

    @Test
    public void testValidCreation() {
        Modifier modifier = new Modifier("TestModifier", 3) {};
        assertEquals("TestModifier", modifier.getName());
        assertEquals(3, modifier.getValue());
    }

    @Test
    public void testInvalidCreationBelowMin() {
        assertThrows(IllegalArgumentException.class, () -> new Modifier("TestModifier", 0) {});
    }

    @Test
    public void testInvalidCreationAboveMax() {
        assertThrows(IllegalArgumentException.class, () -> new Modifier("TestModifier", 6) {});
    }

    @Test
    public void testSetName() {
        Modifier modifier = new Modifier("OldName", 1) {};
        modifier.setName("NewName");
        assertEquals("NewName", modifier.getName());
    }

    @Test
    public void testSetValidValue() {
        Modifier modifier = new Modifier("TestModifier", 1) {};
        modifier.setValue(5);
        assertEquals(5, modifier.getValue());
    }

    @Test
    public void testSetInvalidValueBelowMin() {
        Modifier modifier = new Modifier("TestModifier", 3) {};
        assertThrows(IllegalArgumentException.class, () -> modifier.setValue(0));
    }

    @Test
    public void testSetInvalidValueAboveMax() {
        Modifier modifier = new Modifier("TestModifier", 3) {};
        assertThrows(IllegalArgumentException.class, () -> modifier.setValue(6));
    }
}
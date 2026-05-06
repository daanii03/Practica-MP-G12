package test.main.java.urjc.metprog.models.minions;

import main.java.urjc.metprog.models.minions.Minion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MinionTest {

    @Test
    public void testValidCreation() {
        Minion minion = new Minion("Esbirro Base", 2) {};
        assertEquals("Esbirro Base", minion.getName());
        assertEquals(2, minion.getHealth());
    }

    @Test
    public void testInvalidCreationHealthBelowMin() {
        assertThrows(IllegalArgumentException.class, () -> new Minion("Débil", 0) {});
    }

    @Test
    public void testInvalidCreationHealthAboveMax() {
        assertThrows(IllegalArgumentException.class, () -> new Minion("Fuerte", 4) {});
    }

    @Test
    public void testSetName() {
        Minion minion = new Minion("Esbirro Base", 2) {};
        minion.setName("Nuevo Nombre");
        assertEquals("Nuevo Nombre", minion.getName());
    }

    @Test
    public void testSetValidHealth() {
        Minion minion = new Minion("Esbirro Base", 1) {};
        minion.setHealth(3);
        assertEquals(3, minion.getHealth());
    }

    @Test
    public void testSetInvalidHealth() {
        Minion minion = new Minion("Esbirro Base", 2) {};
        assertThrows(IllegalArgumentException.class, () -> minion.setHealth(0));
        assertThrows(IllegalArgumentException.class, () -> minion.setHealth(4));
    }
}
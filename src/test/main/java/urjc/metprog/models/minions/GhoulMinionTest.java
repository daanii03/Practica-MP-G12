package test.main.java.urjc.metprog.models.minions;

import main.java.urjc.metprog.models.minions.GhoulMinion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GhoulMinionTest {

    @Test
    public void testValidCreation() {
        GhoulMinion ghoul = new GhoulMinion("Gule Sirviente", 3, 5);
        assertEquals("Gule Sirviente", ghoul.getName());
        assertEquals(3, ghoul.getHealth());
        assertEquals(5, ghoul.getDependence());
    }

    @Test
    public void testInvalidCreationDependenceBelowMin() {
        assertThrows(IllegalArgumentException.class, () -> new GhoulMinion("Gule", 3, 0));
    }

    @Test
    public void testInvalidCreationDependenceAboveMax() {
        assertThrows(IllegalArgumentException.class, () -> new GhoulMinion("Gule", 3, 6));
    }

    @Test
    public void testSetValidDependence() {
        GhoulMinion ghoul = new GhoulMinion("Gule", 3, 2);
        ghoul.setDependence(4);
        assertEquals(4, ghoul.getDependence());
    }

    @Test
    public void testSetInvalidDependence() {
        GhoulMinion ghoul = new GhoulMinion("Gule", 3, 3);
        assertThrows(IllegalArgumentException.class, () -> ghoul.setDependence(0));
        assertThrows(IllegalArgumentException.class, () -> ghoul.setDependence(6));
    }
}
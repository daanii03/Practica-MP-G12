package test.main.java.urjc.metprog.models.minions;

import main.java.urjc.metprog.models.minions.DemonMinion;
import main.java.urjc.metprog.models.minions.Minion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DemonMinionTest {

    @Test
    public void testValidCreation() {
        DemonMinion demon = new DemonMinion("Azazel", 3, "Pacto de Sangre");
        assertEquals("Azazel", demon.getName());
        assertEquals(3, demon.getHealth());
        assertEquals("Pacto de Sangre", demon.getPactDescription());
        assertTrue(demon.getMinions().isEmpty());
    }

    @Test
    public void testSetPactDescription() {
        DemonMinion demon = new DemonMinion("Azazel", 3, "Pacto Antiguo");
        demon.setPactDescription("Pacto Renovar");
        assertEquals("Pacto Renovar", demon.getPactDescription());
    }

    @Test
    public void testAddMinion() {
        DemonMinion demon = new DemonMinion("Jefe", 3, "Pacto");
        Minion subMinion = new Minion("Subordinado", 1) {};

        demon.addMinion(subMinion);
        assertEquals(1, demon.getMinions().size());
        assertTrue(demon.getMinions().contains(subMinion));
    }
}
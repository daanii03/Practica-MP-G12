package test.main.java.urjc.metprog.models.minions;

import main.java.urjc.metprog.models.minions.HumanMinion;
import main.java.urjc.metprog.models.minions.Loyalty;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HumanMinionTest {

    @Test
    public void testValidCreation() {
        HumanMinion human = new HumanMinion("Campesino", 2, Loyalty.NORMAL);
        assertEquals("Campesino", human.getName());
        assertEquals(2, human.getHealth());
        assertEquals(Loyalty.NORMAL, human.getLoyalty());
    }

    @Test
    public void testSetLoyalty() {
        HumanMinion human = new HumanMinion("Campesino", 2, Loyalty.NORMAL);
        human.setLoyalty(Loyalty.ALTA);
        assertEquals(Loyalty.ALTA, human.getLoyalty());
    }
}
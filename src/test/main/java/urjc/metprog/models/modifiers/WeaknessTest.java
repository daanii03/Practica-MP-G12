package test.main.java.urjc.metprog.models.modifiers;

import main.java.urjc.metprog.models.modifiers.Weakness;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WeaknessTest {

    @Test
    public void testValidCreation() {
        Weakness weakness = new Weakness("Alergia al Ajo", 2);
        assertEquals("Alergia al Ajo", weakness.getName());
        assertEquals(2, weakness.getValue());
    }

    @Test
    public void testInvalidCreation() {
        assertThrows(IllegalArgumentException.class, () -> new Weakness("Debilidad Nula", 0));
    }
}
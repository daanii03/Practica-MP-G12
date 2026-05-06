package test.main.java.urjc.metprog.models.modifiers;

import main.java.urjc.metprog.models.modifiers.Strength;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StrengthTest {

    @Test
    public void testValidCreation() {
        Strength strength = new Strength("Fuerza Sobrehumana", 5);
        assertEquals("Fuerza Sobrehumana", strength.getName());
        assertEquals(5, strength.getValue());
    }

    @Test
    public void testInvalidCreation() {
        assertThrows(IllegalArgumentException.class, () -> new Strength("Fuerza Rota", 10));
    }
}
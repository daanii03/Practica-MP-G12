package test.main.java.urjc.metprog.models.modifiers;

import main.java.urjc.metprog.models.modifiers.Strength;
import main.java.urjc.metprog.models.modifiers.StrengthCatalogue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StrengthCatalogueTest {

    @Test
    public void testGenerateStrengthVampire() {
        Strength strength = StrengthCatalogue.DRENAJE_VITAL.generateStrength();
        assertEquals("Drenaje Vital", strength.getName());
        assertEquals(4, strength.getValue());
    }

    @Test
    public void testGenerateStrengthHunter() {
        Strength strength = StrengthCatalogue.ARSENAL_SAGRADO.generateStrength();
        assertEquals("Arsenal Sagrado", strength.getName());
        assertEquals(5, strength.getValue());
    }

    @Test
    public void testGenerateStrengthWerewolf() {
        Strength strength = StrengthCatalogue.FURIA_MANADA.generateStrength();
        assertEquals("Furia de Manada", strength.getName());
        assertEquals(3, strength.getValue());
    }
}
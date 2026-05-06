package test.main.java.urjc.metprog.models.modifiers;

import main.java.urjc.metprog.models.modifiers.Weakness;
import main.java.urjc.metprog.models.modifiers.WeaknessCatalogue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WeaknessCatalogueTest {

    @Test
    public void testGenerateWeaknessVampire() {
        Weakness weakness = WeaknessCatalogue.ANEMIA_SOLAR.generateWeakness();
        assertEquals("Anemia Solar", weakness.getName());
        assertEquals(5, weakness.getValue());
    }

    @Test
    public void testGenerateWeaknessHunter() {
        Weakness weakness = WeaknessCatalogue.CONSTITUCION_HUMANA.generateWeakness();
        assertEquals("Constitución Humana", weakness.getName());
        assertEquals(2, weakness.getValue());
    }

    @Test
    public void testGenerateWeaknessWerewolf() {
        Weakness weakness = WeaknessCatalogue.INESTABILIDAD_LUNAR.generateWeakness();
        assertEquals("Inestabilidad Lunar", weakness.getName());
        assertEquals(3, weakness.getValue());
    }
}
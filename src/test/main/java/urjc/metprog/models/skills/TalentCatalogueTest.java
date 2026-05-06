package test.main.java.urjc.metprog.models.skills;

import main.java.urjc.metprog.models.skills.Talent;
import main.java.urjc.metprog.models.skills.TalentCatalogue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TalentCatalogueTest {

    @Test
    public void testGenerateTalentDisparoCertero() {
        Talent talent = TalentCatalogue.DISPARO_CERTERO.generateTalent();
        assertEquals("Disparo Certero", talent.getName());
        assertEquals(2, talent.getAttackValue());
        assertEquals(1, talent.getDefenseValue());
    }

    @Test
    public void testGenerateTalentTrampaDePlata() {
        Talent talent = TalentCatalogue.TRAMPA_DE_PLATA.generateTalent();
        assertEquals("Trampa de Plata", talent.getName());
        assertEquals(1, talent.getAttackValue());
        assertEquals(3, talent.getDefenseValue());
    }

    @Test
    public void testGenerateTalentEstacaBendita() {
        Talent talent = TalentCatalogue.ESTACA_BENDITA.generateTalent();
        assertEquals("Estaca Bendita", talent.getName());
        assertEquals(3, talent.getAttackValue());
        assertEquals(2, talent.getDefenseValue());
    }
}
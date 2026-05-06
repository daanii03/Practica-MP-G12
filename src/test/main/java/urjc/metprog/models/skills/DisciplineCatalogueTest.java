package test.main.java.urjc.metprog.models.skills;

import main.java.urjc.metprog.models.skills.Discipline;
import main.java.urjc.metprog.models.skills.DisciplineCatalogue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DisciplineCatalogueTest {

    @Test
    public void testGenerateDisciplineMordiscoLetal() {
        Discipline discipline = DisciplineCatalogue.MORDISCO_LETAL.generateDiscipline();
        assertEquals("Mordisco Letal", discipline.getName());
        assertEquals(1, discipline.getBloodPointsCost());
        assertEquals(2, discipline.getAttackValue());
        assertEquals(1, discipline.getDefenseValue());
    }

    @Test
    public void testGenerateDisciplineSombrasOcultas() {
        Discipline discipline = DisciplineCatalogue.SOMBRAS_OCULTAS.generateDiscipline();
        assertEquals("Sombras ocultas", discipline.getName());
        assertEquals(2, discipline.getBloodPointsCost());
        assertEquals(3, discipline.getAttackValue());
        assertEquals(2, discipline.getDefenseValue());
    }

    @Test
    public void testGenerateDisciplineSangreHervida() {
        Discipline discipline = DisciplineCatalogue.SANGRE_HERVIDA.generateDiscipline();
        assertEquals("Sangre hervida", discipline.getName());
        assertEquals(3, discipline.getBloodPointsCost());
        assertEquals(3, discipline.getAttackValue());
        assertEquals(1, discipline.getDefenseValue());
    }
}
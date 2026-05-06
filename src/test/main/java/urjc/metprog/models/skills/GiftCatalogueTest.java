package test.main.java.urjc.metprog.models.skills;

import main.java.urjc.metprog.models.skills.Gift;
import main.java.urjc.metprog.models.skills.GiftCatalogue;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GiftCatalogueTest {

    @Test
    public void testGenerateGiftGarrasDesgarradoras() {
        Gift gift = GiftCatalogue.GARRAS_DESGARRADORAS.generateGift();
        assertEquals("Garras Desgarradoras", gift.getName());
        assertEquals(2, gift.getRageCost());
        assertEquals(3, gift.getAttackValue());
        assertEquals(1, gift.getDefenseValue());
    }

    @Test
    public void testGenerateGiftPielDeLuna() {
        Gift gift = GiftCatalogue.PIEL_DE_LUNA.generateGift();
        assertEquals("Piel de Luna", gift.getName());
        assertEquals(1, gift.getRageCost());
        assertEquals(1, gift.getAttackValue());
        assertEquals(3, gift.getDefenseValue());
    }

    @Test
    public void testGenerateGiftFuriaCiega() {
        Gift gift = GiftCatalogue.FURIA_CIEGA.generateGift();
        assertEquals("Furia Ciega", gift.getName());
        assertEquals(3, gift.getRageCost());
        assertEquals(3, gift.getAttackValue());
        assertEquals(1, gift.getDefenseValue());
    }
}
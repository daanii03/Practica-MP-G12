package test.main.java.urjc.metprog.models.skills;

import main.java.urjc.metprog.models.skills.Gift;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GiftTest {

    @Test
    public void testValidCreation() {
        Gift gift = new Gift("Aullido", 1, 2, 2);
        assertEquals("Aullido", gift.getName());
        assertEquals(1, gift.getRageCost());
        assertEquals(2, gift.getAttackValue());
        assertEquals(2, gift.getDefenseValue());
    }

    @Test
    public void testInvalidRageCostCreation() {
        assertThrows(IllegalArgumentException.class, () -> new Gift("TestGift", -1, 2, 2));
        assertThrows(IllegalArgumentException.class, () -> new Gift("TestGift", 4, 2, 2));
    }

    @Test
    public void testSetRageCost() {
        Gift gift = new Gift("TestGift", 0, 2, 2);
        gift.setRageCost(3);
        assertEquals(3, gift.getRageCost());
    }

    @Test
    public void testSetInvalidRageCost() {
        Gift gift = new Gift("TestGift", 0, 2, 2);
        assertThrows(IllegalArgumentException.class, () -> gift.setRageCost(-1));
        assertThrows(IllegalArgumentException.class, () -> gift.setRageCost(4));
    }
}
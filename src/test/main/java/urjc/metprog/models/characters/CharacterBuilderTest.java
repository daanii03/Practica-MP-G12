package test.main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.characters.Inventory;
import main.java.urjc.metprog.models.minions.Minion;
import main.java.urjc.metprog.models.modifiers.Strength;
import main.java.urjc.metprog.models.modifiers.Weakness;
import main.java.urjc.metprog.models.skills.SpecialSkill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterBuilderTest {

    private CharacterBuilder builder;

    @BeforeEach
    public void setUp() {
        builder = new CharacterBuilder();
    }

    @Test
    public void testBuilderPattern() {
        SpecialSkill skill = new SpecialSkill("TestSkill", 2, 2) {};
        Inventory inventory = new Inventory();
        Minion minion = new Minion("Esbirro", 2) {};
        Weakness weakness = new Weakness("Debilidad", 2) {};
        Strength strength = new Strength("Fortaleza", 2) {};

        builder.name("Dracula")
                .life(5)
                .power(4)
                .id(1)
                .specialSkill(skill)
                .inventory(inventory)
                .addMinion(minion)
                .addWeakness(weakness)
                .addStrength(strength);

        assertEquals("Dracula", builder.getName());
        assertEquals(5, builder.getLife());
        assertEquals(4, builder.getPower());
        assertEquals(1, builder.getId());
        assertEquals(skill, builder.getSpecialSkill());
        assertEquals(inventory, builder.getInventory());
        assertTrue(builder.getMinions().contains(minion));
        assertTrue(builder.getWeaknesses().contains(weakness));
        assertTrue(builder.getStrengths().contains(strength));
    }
}
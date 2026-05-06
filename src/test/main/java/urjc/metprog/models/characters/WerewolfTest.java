package test.main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.characters.Werewolf;
import main.java.urjc.metprog.models.skills.Gift;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WerewolfTest {

    private Werewolf werewolf;

    @BeforeEach
    public void setUp() {
        Gift gift = new Gift("TestGift", 1, 2, 2) {
            @Override
            public int getAttackValue() {
                return 5;
            }

            @Override
            public int getDefenseValue() {
                return 4;
            }
        };

        CharacterBuilder builder = new CharacterBuilder()
                .name("Hombre Lobo")
                .life(4)
                .power(4)
                .specialSkill(gift);

        werewolf = new Werewolf(builder);
    }

    @Test
    public void testDefaultValues() {
        assertEquals(0, werewolf.getRage());
    }

    @Test
    public void testIncreaseRage() {
        werewolf.increaseRage();
        assertEquals(1, werewolf.getRage());
        werewolf.increaseRage();
        werewolf.increaseRage();
        werewolf.increaseRage();
        assertEquals(3, werewolf.getRage());
    }

    @Test
    public void testResetRage() {
        werewolf.increaseRage();
        werewolf.resetRage();
        assertEquals(0, werewolf.getRage());
    }

    @Test
    public void testGetSpecialAttackEnoughRage() {
        werewolf.increaseRage();
        werewolf.increaseRage();
        assertEquals(5, werewolf.getSpecialAttack());
    }

    @Test
    public void testGetSpecialAttackNotEnoughRage() {
        assertEquals(0, werewolf.getSpecialAttack());
    }

    @Test
    public void testGetSpecialDefenseEnoughRage() {
        werewolf.increaseRage();
        assertEquals(4, werewolf.getSpecialDefense());
    }

    @Test
    public void testGetSpecialDefenseNotEnoughRage() {
        assertEquals(0, werewolf.getSpecialDefense());
    }
}
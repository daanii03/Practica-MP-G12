package test.main.java.urjc.metprog.models.factorys;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.factorys.CharacterFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterFactoryTest {

    @Test
    public void testCreateCharacter() {
        CharacterFactory factory = new CharacterFactory() {
            @Override
            public Character createCharacter(int id, String name) {
                CharacterBuilder builder = new CharacterBuilder()
                        .id(id)
                        .name(name)
                        .power(3)
                        .life(3);

                return new Character(builder) {
                    @Override
                    public int getSpecialAttack() {
                        return 0;
                    }

                    @Override
                    public int getSpecialDefense() {
                        return 0;
                    }
                };
            }
        };

        Character character = factory.createCharacter(99, "TestCharacter");

        assertNotNull(character);
        assertEquals(99, character.getId());
        assertEquals("TestCharacter", character.getName());
        assertEquals(3, character.getPower());
        assertEquals(3, character.getLife());
    }
}
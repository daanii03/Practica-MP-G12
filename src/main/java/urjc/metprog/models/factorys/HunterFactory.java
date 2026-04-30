package main.java.urjc.metprog.models.factorys;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.characters.Hunter;

public class HunterFactory extends CharacterFactory {

    @Override
    public Character createCharacter(int id, String name) {

        CharacterBuilder characterBuilder = new CharacterBuilder();


        characterBuilder.id(id).life(5).power(5).specialSkill(random)

        return new Hunter(characterBuilder);
    }
}

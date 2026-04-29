package main.java.urjc.metprog.models.factorys;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.characters.Discipline;
import main.java.urjc.metprog.models.characters.Vampire;

public class vampireFactory extends characterFactory{


    @Override
    public Character createCharacter(int id, String name) {

        CharacterBuilder characterBuilder = new CharacterBuilder();
        characterBuilder.id(id).name(name).specialSkill(new Discipline()).life(5)
                .power(5);


        return new Vampire(characterBuilder);
    }
}

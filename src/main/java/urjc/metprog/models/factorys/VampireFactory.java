package main.java.urjc.metprog.models.factorys;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.skills.Discipline;
import main.java.urjc.metprog.models.characters.Vampire;
import main.java.urjc.metprog.models.skills.DisciplineCatalogue;

import java.util.Random;

public class VampireFactory extends CharacterFactory {

    @Override
    public Character createCharacter(int id, String name) {

        CharacterBuilder characterBuilder = new CharacterBuilder();

        DisciplineCatalogue[] options = DisciplineCatalogue.values();

        int randomIndex = new Random().nextInt(options.length);

        Discipline randomDiscipline = options[randomIndex].generateDiscipline();

        characterBuilder.id(id).name(name).specialSkill(randomDiscipline).life(5)
                .power(5).inventory(null).activeArmor(null);


        return new Vampire(characterBuilder);
    }
}

package main.java.urjc.metprog.models.factorys;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.characters.Hunter;
import main.java.urjc.metprog.models.characters.Inventory;
import main.java.urjc.metprog.models.skills.Talent;
import main.java.urjc.metprog.models.skills.TalentCatalogue;

import java.util.Random;

public class HunterFactory extends CharacterFactory {

    @Override
    public Character createCharacter(int id, String name) {

        CharacterBuilder characterBuilder = new CharacterBuilder();

        TalentCatalogue[] talentCatalogue = TalentCatalogue.values();

        int randomIndex = new Random().nextInt(talentCatalogue.length);

        Talent randomTalent = talentCatalogue[randomIndex].generateTalent();

        characterBuilder.id(id)
                .name(name)
                .life(5)
                .power(5)
                .specialSkill(randomTalent)
                .inventory(new Inventory());

        return new Hunter(characterBuilder);
    }
}

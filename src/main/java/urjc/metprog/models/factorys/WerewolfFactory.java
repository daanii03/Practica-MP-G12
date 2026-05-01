package main.java.urjc.metprog.models.factorys;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.characters.Inventory;
import main.java.urjc.metprog.models.characters.Werewolf;
import main.java.urjc.metprog.models.skills.Gift;
import main.java.urjc.metprog.models.skills.GiftCatalogue;

import java.util.Random;

public class WerewolfFactory extends CharacterFactory{

    @Override
    public Character createCharacter(int id, String name) {

        GiftCatalogue[] giftCatalogue = GiftCatalogue.values();

        int randomIndex = new Random().nextInt(giftCatalogue.length);

        Gift gift = giftCatalogue[randomIndex].generateGift();

        CharacterBuilder characterBuilder = new CharacterBuilder();

        characterBuilder.id(id)
                .name(name)
                .specialSkill(gift)
                .life(5)
                .power(5)
                .inventory(new Inventory());

        return new Werewolf(characterBuilder);
    }
}

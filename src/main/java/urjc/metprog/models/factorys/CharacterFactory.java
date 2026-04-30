package main.java.urjc.metprog.models.factorys;

import main.java.urjc.metprog.models.characters.Character;

public abstract class CharacterFactory {

    public abstract Character createCharacter(int id, String name);

}

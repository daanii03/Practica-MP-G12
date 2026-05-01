package main.java.urjc.metprog.dao;

import main.java.urjc.metprog.models.characters.Character;

public interface CharacterDao {
    void insertCharacter(Character character, String ownerNick);

    Character getCharacter(int id);

    void deleteCharacter(int id);

    Character getCharacterByOwner(String ownerNick);
}
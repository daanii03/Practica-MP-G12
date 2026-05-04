package main.java.urjc.metprog.dao;

import main.java.urjc.metprog.models.characters.Character;

public interface CharacterDao {
    void insertCharacter(Character character, String ownerNick);

    Character getCharacter(int id);

    void deleteCharacter(int id);

    Character getCharacterByOwner(String ownerNick);

    /**
     * Obtiene todos los personajes de la base de datos junto con el nick de su dueño.
     * @return Mapa de ownerNick -> Character.
     */
    java.util.Map<String, Character> getAllCharactersWithOwners();
}
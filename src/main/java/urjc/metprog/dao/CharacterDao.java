package main.java.urjc.metprog.dao;

import main.java.urjc.metprog.models.characters.Character;

public interface CharacterDao {
    void insertCharacter(Character character);
    Character SearchOwner(int idUser);
    void updateCharacter(Character personaje);
    void deleteCharacter(int id);
}

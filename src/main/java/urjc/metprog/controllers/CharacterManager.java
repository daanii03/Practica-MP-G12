package main.java.urjc.metprog.controllers;
import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.Minion;
import main.java.urjc.metprog.dao.CharacterDao;
//Añadir import de las fabric y del characterDAO


public class CharacterManager {
    //Dependecias
    //Añadir las dependencias faltantes
    private final CharacterDao characterDao;


    public CharacterManager(CharacterDao characterDao) {
        this.characterDao = characterDao;
    }

    public void borrarPersonaje(int id) {
        characterDao.deleteCharacter(id);
    }
}

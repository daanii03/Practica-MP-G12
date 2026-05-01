package main.java.urjc.metprog.controllers;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.dao.CharacterDao;
import main.java.urjc.metprog.models.factorys.HunterFactory;
import main.java.urjc.metprog.models.factorys.VampireFactory;
import main.java.urjc.metprog.models.factorys.WerewolfFactory;

import java.util.Random;

public class CharacterManager {

    private final CharacterDao characterDao;

    public CharacterManager(CharacterDao characterDao) {
        this.characterDao = characterDao;
    }

    /**
     * Crea un personaje delegando en la fábrica correspondiente y lo guarda en la BD.
     */
    public Character createCharacter(String ownerNick, String name, String race) {
        Character newCharacter = null;

        // Generamos un ID aleatorio positivo para la base de datos
        int generatedId = Math.abs(new Random().nextInt());

        switch (race.toLowerCase()) {
            case "vampiro":
            case "vampire":
                newCharacter = new VampireFactory().createCharacter(generatedId, name);
                break;
            case "licantropo":
            case "werewolf":
                newCharacter = new WerewolfFactory().createCharacter(generatedId, name);
                break;
            case "cazador":
            case "hunter":
                newCharacter = new HunterFactory().createCharacter(generatedId, name);
                break;
            default:
                throw new IllegalArgumentException("Raza de personaje no reconocida: " + race);
        }

        characterDao.insertCharacter(newCharacter, ownerNick);

        return newCharacter;
    }

    /**
     * Elimina permanentemente un personaje de la base de datos.
     */
    public void deleteCharacter(int id) {
        characterDao.deleteCharacter(id);
    }
    /**
     * Devuelve el personaje asociado a un usuario concreto.
     * Si el usuario no tiene personaje creado, devuelve null.
     */
    public Character getCharacterByUser(String ownerNick) {
        return characterDao.getCharacterByOwner(ownerNick);
    }
}
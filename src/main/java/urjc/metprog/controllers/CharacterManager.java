package main.java.urjc.metprog.controllers;

import main.java.urjc.metprog.dao.CharacterDao;
import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.equipment.Armor;
import main.java.urjc.metprog.models.equipment.Weapon;
import main.java.urjc.metprog.models.factorys.HunterFactory;
import main.java.urjc.metprog.models.factorys.VampireFactory;
import main.java.urjc.metprog.models.factorys.WerewolfFactory;
import main.java.urjc.metprog.models.minions.Minion;
import main.java.urjc.metprog.models.modifiers.Strength;
import main.java.urjc.metprog.models.modifiers.Weakness;
import main.java.urjc.metprog.models.equipment.ArmorCatalogue;
import main.java.urjc.metprog.models.equipment.WeaponCatalogue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Gestor principal de personajes.
 * Proporciona la lógica de negocio para que los jugadores gestionen sus personajes
 * y para que los operadores puedan editar o añadir elementos a cualquier personaje.
 */
public class CharacterManager {

    private final CharacterDao characterDao;

    /**
     * Constructor del gestor de personajes.
     *
     * @param characterDao Interfaz DAO para acceder a los datos persistentes.
     */
    public CharacterManager(CharacterDao characterDao) {
        this.characterDao = characterDao;
    }


    /**
     * Crea un personaje delegando en la fábrica correspondiente y lo guarda en la BD.
     *
     * @param ownerNick Nick del usuario dueño del personaje.
     * @param name      Nombre del personaje.
     * @param race      Raza del personaje (Vampiro, Licantropo, Cazador).
     * @return El personaje recién creado.
     * @throws IllegalArgumentException si la raza no es válida.
     */
    /**
     * Crea un personaje delegando en la fábrica correspondiente y lo guarda en la BD.
     */
    public Character createCharacter(String ownerNick, String name, String race) {
        Character newCharacter = null;
        int generatedId = Math.abs(new Random().nextInt());

        switch (race.toLowerCase()) {
            case "vampire":
                newCharacter = new VampireFactory().createCharacter(generatedId, name);
                break;
            case "werewolf":
                newCharacter = new WerewolfFactory().createCharacter(generatedId, name);
                break;
            case "hunter":
                newCharacter = new HunterFactory().createCharacter(generatedId, name);
                break;
            default:
                throw new IllegalArgumentException("Raza de personaje no reconocida: " + race);
        }

        // --- SOLUCIÓN: AÑADIR EQUIPO INICIAL ALEATORIO AQUÍ ---
        addInitialEquipment(newCharacter);
        // ------------------------------------------------------

        characterDao.insertCharacter(newCharacter, ownerNick);
        return newCharacter;
    }

    /**
     * Método auxiliar para dar 2 armas y 2 armaduras aleatorias al crear el personaje.
     */
    private void addInitialEquipment(Character character) {
        Random rand = new Random();
        ArmorCatalogue[] armorValues = ArmorCatalogue.values();
        WeaponCatalogue[] weaponValues = WeaponCatalogue.values();

        // --- PARA LAS ARMADURAS ---
        List<Integer> usedArmorIdx = new ArrayList<>();
        while (usedArmorIdx.size() < 3) {
            int idx = rand.nextInt(armorValues.length);
            if (!usedArmorIdx.contains(idx)) {
                character.getInventory().addArmor(armorValues[idx].generateArmor());
                usedArmorIdx.add(idx);
            }
            // Si tu catálogo tiene menos de 3 armaduras, este bucle sería infinito.
            // Asegúrate de tener al menos 3 en el Enum.
        }

        // --- PARA LAS ARMAS ---
        List<Integer> usedWeaponIdx = new ArrayList<>();
        while (usedWeaponIdx.size() < 3) {
            int idx = rand.nextInt(weaponValues.length);
            if (!usedWeaponIdx.contains(idx)) {
                character.getInventory().addWeapon(weaponValues[idx].generateWeapon());
                usedWeaponIdx.add(idx);
            }
        }
    }

    /**
     * Elimina permanentemente un personaje de la base de datos (Dar de baja).
     *
     * @param id Identificador único del personaje.
     */
    public void deleteCharacter(int id) {
        characterDao.deleteCharacter(id);
    }

    /**
     * Devuelve el personaje asociado a un usuario concreto.
     *
     * @param ownerNick Nick del usuario a buscar.
     * @return El personaje, o null si el usuario no tiene ninguno.
     */
    public Character getCharacterByUser(String ownerNick) {
        return characterDao.getCharacterByOwner(ownerNick);
    }

    /**
     * Permite al jugador elegir las armas y armaduras activas de su personaje.
     * Utiliza la técnica de reemplazo (borrar e insertar) para persistir los cambios sin usar UPDATE.
     *
     * @param ownerNick     Nick del usuario.
     * @param activeWeapons Lista de armas que se desean equipar.
     * @param activeArmor   Armadura que se desea equipar.
     */
    public void changeActiveEquipment(String ownerNick, List<Weapon> activeWeapons, Armor activeArmor) {
        Character character = getCharacterByUser(ownerNick);
        if (character == null) {
            throw new IllegalStateException("El usuario no tiene un personaje creado.");
        }

        if (activeWeapons != null && !activeWeapons.isEmpty()) {
            character.getInventory().setActiveWeapons(activeWeapons);
        }
        if (activeArmor != null) {
            character.getInventory().setActiveArmor(activeArmor);
        }

        updateCharacter(ownerNick, character);
    }


    /**
     * Actualiza cualquier característica general de un personaje modificado previamente.
     * Elimina el registro antiguo de la base de datos e inserta la versión actualizada.
     *
     * @param ownerNick        Nick del dueño del personaje.
     * @param updatedCharacter Objeto Character con los datos ya modificados.
     */
    public void updateCharacter(String ownerNick, Character updatedCharacter) {
        if (updatedCharacter == null) {
            throw new IllegalArgumentException("El personaje a actualizar no puede ser nulo.");
        }

        // Borrado e inserción como solución a la falta de un método UPDATE en el DAO
        characterDao.deleteCharacter(updatedCharacter.getId());
        characterDao.insertCharacter(updatedCharacter, ownerNick);
    }

    /**
     * Añade un arma al inventario del personaje de un usuario.
     *
     * @param ownerNick Nick del usuario dueño del personaje.
     * @param weapon    El arma a añadir.
     */
    public void addWeaponToInventory(String ownerNick, Weapon weapon) {
        Character character = getCharacterByUser(ownerNick);
        if (character != null) {
            character.getInventory().addWeapon(weapon);
            updateCharacter(ownerNick, character);
        } else {
            throw new IllegalArgumentException("El usuario especificado no tiene personaje.");
        }
    }

    /**
     * Añade una armadura al inventario del personaje de un usuario.
     *
     * @param ownerNick Nick del usuario dueño del personaje.
     * @param armor     La armadura a añadir.
     */
    public void addArmorToInventory(String ownerNick, Armor armor) {
        Character character = getCharacterByUser(ownerNick);
        if (character != null) {
            character.getInventory().addArmor(armor);
            updateCharacter(ownerNick, character);
        } else {
            throw new IllegalArgumentException("El usuario especificado no tiene personaje.");
        }
    }

    /**
     * Añade un esbirro a la colección del personaje.
     *
     * @param ownerNick Nick del usuario dueño del personaje.
     * @param minion    El esbirro a añadir.
     */
    public void addMinion(String ownerNick, Minion minion) {
        Character character = getCharacterByUser(ownerNick);
        if (character != null) {
            character.addMinion(minion);
            updateCharacter(ownerNick, character);
        } else {
            throw new IllegalArgumentException("El usuario especificado no tiene personaje.");
        }
    }

    /**
     * Añade una fortaleza al personaje.
     *
     * @param ownerNick Nick del usuario dueño del personaje.
     * @param strength  La fortaleza a añadir.
     */
    public void addStrength(String ownerNick, Strength strength) {
        Character character = getCharacterByUser(ownerNick);
        if (character != null) {
            character.addStrength(strength);
            updateCharacter(ownerNick, character);
        } else {
            throw new IllegalArgumentException("El usuario especificado no tiene personaje.");
        }
    }

    /**
     * Añade una debilidad al personaje.
     *
     * @param ownerNick Nick del usuario dueño del personaje.
     * @param weakness  La debilidad a añadir.
     */
    public void addWeakness(String ownerNick, Weakness weakness) {
        Character character = getCharacterByUser(ownerNick);
        if (character != null) {
            character.addWeakness(weakness);
            updateCharacter(ownerNick, character);
        } else {
            throw new IllegalArgumentException("El usuario especificado no tiene personaje.");
        }
    }

    /**
     * Obtiene todos los personajes registrados junto con sus dueños.
     *
     * @return Mapa de ownerNick -> Character.
     */
    public java.util.Map<String, Character> getAllCharacters() {
        return characterDao.getAllCharactersWithOwners();
    }

    /**
     * Obtiene el catálogo global de equipo predefinido.
     */
    public java.util.List<String[]> getCatalog() {
        return characterDao.getEquipmentCatalog();
    }

    /**
     * Elimina un arma del inventario.
     */
    public void removeWeaponFromInventory(String ownerNick, Weapon weapon) {
        Character character = getCharacterByUser(ownerNick);
        if (character != null) {
            character.getInventory().removeWeapon(weapon);
            updateCharacter(ownerNick, character);
        }
    }

    /**
     * Elimina una armadura del inventario.
     */
    public void removeArmorFromInventory(String ownerNick, Armor armor) {
        Character character = getCharacterByUser(ownerNick);
        if (character != null) {
            character.getInventory().removeArmor(armor);
            updateCharacter(ownerNick, character);
        }
    }

    /**
     * Elimina un esbirro del personaje de un usuario por su índice.
     *
     * @param ownerNick Nick del usuario dueño del personaje.
     * @param minionIndex Índice del esbirro a eliminar (0-based).
     */
    public void deleteMinion(String ownerNick, int minionIndex) {
        Character character = getCharacterByUser(ownerNick);
        if (character != null) {
            character.removeMinion(minionIndex);
            updateCharacter(ownerNick, character);
        } else {
            throw new IllegalArgumentException("El usuario especificado no tiene personaje.");
        }
    }
}
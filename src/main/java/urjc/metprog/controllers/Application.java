package main.java.urjc.metprog.controllers;

import java.util.List;
import main.java.urjc.metprog.dao.DatabaseManager;
import main.java.urjc.metprog.dao.CharacterDaoImpl;
import main.java.urjc.metprog.dao.UserDaoImpl;
import main.java.urjc.metprog.models.user.User;
import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.equipment.Armor;
import main.java.urjc.metprog.models.equipment.Weapon;

/**
 * Controlador principal de la aplicación.
 * Orquesta la comunicación entre la interfaz de usuario y los gestores de negocio.
 * Sigue el principio de separación de responsabilidades: procesa datos y lógica,
 * pero no se encarga de la representación visual.
 */
public class Application {
    
    private CharacterManager characterManager;
    private UserManager userManager;
    private User currentUser;

    public Application() {
    }

    /**
     * Inicializa los servicios y la base de datos.
     */
    public void init() {
        DatabaseManager.createTables();
        this.characterManager = new CharacterManager(new CharacterDaoImpl());
        this.userManager = new UserManager(new UserDaoImpl());
    }

    // ==========================================
    // MÉTODOS DE GESTIÓN DE USUARIOS
    // ==========================================

    /**
     * Intenta iniciar sesión en el sistema.
     * @return El usuario que ha iniciado sesión.
     * @throws RuntimeException si las credenciales son incorrectas o el usuario está bloqueado.
     */
    public User loginUser(String nick, String password) {
        User user = userManager.logIn(nick, password);
        if (user == null) {
            throw new RuntimeException("Nick o contraseña incorrectos.");
        }
        this.currentUser = user;
        return user;
    }

    /**
     * Registra un nuevo jugador.
     * @return El usuario recién creado.
     */
    public User registerUser(String name, String nick, String password) {
        return userManager.signUp(name, nick, password);
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    public void logoutUser() {
        if (currentUser != null) {
            userManager.logOut(currentUser);
            currentUser = null;
        }
    }

    /**
     * Elimina la cuenta del usuario.
     */
    public void deleteAccount(String nick, String password) {
        userManager.deleteUser(nick, password);
        if (currentUser != null && currentUser.getNick().equals(nick)) {
            currentUser = null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // ==========================================
    // MÉTODOS DE GESTIÓN DE PERSONAJES
    // ==========================================

    /**
     * Crea un personaje para el usuario actual.
     * @return El personaje creado.
     */
    public Character createCharacter(String name, String characterType) {
        if (currentUser == null) {
            throw new IllegalStateException("Debes iniciar sesión para crear un personaje.");
        }
        
        // Comprobar si ya tiene un personaje registrado
        if (characterManager.getCharacterByUser(currentUser.getNick()) != null) {
            throw new IllegalStateException("Ya tienes un personaje registrado. Solo se permite uno por usuario.");
        }

        // TODO: Gestionar la elección de habilidad especial. Actualmente se asigna una por defecto en la Factory.
        return characterManager.createCharacter(currentUser.getNick(), name, characterType);
    }

    /**
     * Elimina el personaje del usuario actual.
     */
    public void deleteCharacter() {
        if (currentUser == null) {
            throw new IllegalStateException("Debes iniciar sesión para dar de baja un personaje.");
        }
        Character character = characterManager.getCharacterByUser(currentUser.getNick());
        if (character == null) {
            throw new IllegalStateException("El usuario no tiene ningún personaje que eliminar.");
        }
        characterManager.deleteCharacter(character.getId());
    }

    /**
     * Obtiene el personaje del usuario actual.
     */
    public Character getCurrentUserCharacter() {
        if (currentUser == null) return null;
        return characterManager.getCharacterByUser(currentUser.getNick());
    }

    // ==========================================
    // MÉTODOS DE GESTIÓN DE EQUIPAMIENTO
    // ==========================================

    /**
     * Comprueba que el usuario tiene sesión y personaje, y devuelve el personaje.
     * Método auxiliar privado para evitar duplicar esta validación en cada método de equipamiento.
     *
     * @return El personaje del usuario actual.
     * @throws IllegalStateException Si no hay sesión iniciada o el usuario no tiene personaje.
     */
    private Character requireCurrentCharacter() {
        if (currentUser == null) {
            throw new IllegalStateException("Debes iniciar sesión para gestionar el equipamiento.");
        }
        Character character = characterManager.getCharacterByUser(currentUser.getNick());
        if (character == null) {
            throw new IllegalStateException("No tienes un personaje creado. Crea uno primero.");
        }
        return character;
    }

    /**
     * Obtiene todas las armas que el personaje posee en su inventario.
     * Útil para mostrar al usuario la lista de armas disponibles antes de elegir.
     *
     * @return Lista de armas en el inventario del personaje.
     */
    public List<Weapon> getAvailableWeapons() {
        Character character = requireCurrentCharacter();
        return character.getInventory().getWeapons();
    }

    /**
     * Obtiene todas las armaduras que el personaje posee en su inventario.
     * Útil para mostrar al usuario la lista de armaduras disponibles antes de elegir.
     *
     * @return Lista de armaduras en el inventario del personaje.
     */
    public List<Armor> getAvailableArmors() {
        Character character = requireCurrentCharacter();
        return character.getInventory().getArmors();
    }

    /**
     * Obtiene las armas actualmente equipadas por el personaje.
     *
     * @return Lista de armas activas (puede estar vacía).
     */
    public List<Weapon> getActiveWeapons() {
        Character character = requireCurrentCharacter();
        return character.getInventory().getActiveWeapons();
    }

    /**
     * Obtiene la armadura actualmente equipada por el personaje.
     *
     * @return La armadura activa, o null si no tiene ninguna equipada.
     */
    public Armor getActiveArmor() {
        Character character = requireCurrentCharacter();
        return character.getInventory().getActiveArmor();
    }

    /**
     * Cambia las armas activas del personaje.
     * Las reglas de validación (máximo 2 armas, restricción de dos manos, pertenencia al inventario)
     * son aplicadas por la clase Inventory.
     *
     * @param weapons Lista de armas a equipar.
     * @throws IllegalStateException    Si no hay sesión o personaje.
     * @throws IllegalArgumentException Si se incumple alguna regla de equipamiento.
     */
    public void changeActiveWeapons(List<Weapon> weapons) {
        requireCurrentCharacter();
        characterManager.changeActiveEquipment(currentUser.getNick(), weapons, null);
    }

    /**
     * Cambia la armadura activa del personaje.
     * La validación de pertenencia al inventario es aplicada por la clase Inventory.
     *
     * @param armor La armadura a equipar.
     * @throws IllegalStateException    Si no hay sesión o personaje.
     * @throws IllegalArgumentException Si la armadura no pertenece al inventario.
     */
    public void changeActiveArmor(Armor armor) {
        requireCurrentCharacter();
        characterManager.changeActiveEquipment(currentUser.getNick(), null, armor);
    }

    /**
     * Cambia armas y armadura activas en una sola operación.
     * Permite a la interfaz ofrecer un flujo combinado donde el usuario elige todo de una vez.
     * Se puede pasar null en cualquiera de los dos parámetros para cambiar solo uno.
     *
     * @param weapons Lista de armas a equipar (null para no cambiar).
     * @param armor   Armadura a equipar (null para no cambiar).
     * @throws IllegalStateException    Si no hay sesión o personaje.
     * @throws IllegalArgumentException Si se incumple alguna regla de equipamiento.
     */
    public void changeActiveEquipment(List<Weapon> weapons, Armor armor) {
        requireCurrentCharacter();
        characterManager.changeActiveEquipment(currentUser.getNick(), weapons, armor);
    }

    // ==========================================
    // OTROS MÉTODOS GENERALES
    // ==========================================
    
    /**
     * Obtiene el ranking global de victorias.
     */
    public List<User> getGlobalRanking() {
        return userManager.getRanking();
    }

    // Placeholders para futuras funcionalidades
    public void chooseSkill(String skillName) {}
    public void selectMinions(List<String> minionNames) {}
    public void challengePlayer(String opponentNick, int goldBet) {}
    public void acceptChallenge(String challengeId) {}
    public void rejectChallenge(String challengeId) {}
    public void startCombat() {}
}

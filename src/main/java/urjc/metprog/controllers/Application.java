package main.java.urjc.metprog.controllers;

import java.util.List;
import main.java.urjc.metprog.dao.DatabaseManager;
import main.java.urjc.metprog.dao.CharacterDaoImpl;
import main.java.urjc.metprog.dao.UserDaoImpl;
import main.java.urjc.metprog.models.user.User;
import main.java.urjc.metprog.models.characters.Character;

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
    public void addEquipment(String equipmentName) {}
    public void challengePlayer(String opponentNick, int goldBet) {}
    public void acceptChallenge(String challengeId) {}
    public void rejectChallenge(String challengeId) {}
    public void startCombat() {}
}

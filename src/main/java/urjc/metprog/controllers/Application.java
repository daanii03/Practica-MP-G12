package main.java.urjc.metprog.controllers;

import java.util.List;
import main.java.urjc.metprog.dao.DatabaseManager;
import main.java.urjc.metprog.dao.CharacterDaoImpl;
import main.java.urjc.metprog.dao.UserDaoImpl;
import main.java.urjc.metprog.models.user.User;
import main.java.urjc.metprog.models.characters.Character;

public class Application {
    
    private CharacterManager characterManager;
    private UserManager userManager;
    private User currentUser;
    // TODO: Crear y añadir la instancia de ChallengeManager cuando la clase esté programada
    // private ChallengeManager challengeManager;

    public Application() {
        // El constructor ahora se deja limpio intencionadamente.
        // Solo se usa para preparar el objeto en memoria.
    }

    /**
     * Método encargado de inicializar todos los sistemas pesados,
     * como la conexión a la base de datos y la instanciación de Gestores/DAOs.
     */
    public void init() {
        System.out.println("Iniciando servicios y base de datos...");
        
        // 1. Inicializar infraestructura
        DatabaseManager.createTables();
        
        // Instanciar gestores con sus respectivos DAOs (Composición)
        this.characterManager = new CharacterManager(new CharacterDaoImpl());
        this.userManager = new UserManager(new UserDaoImpl());
        // TODO: this.challengeManager = new ChallengeManager(...);
    }

    // ==========================================
    // MÉTODOS DE GESTIÓN DE USUARIOS
    // ==========================================

    public boolean loginUser(String nick, String password) {
        try {
            User user = userManager.logIn(nick, password);
            if (user != null) {
                this.currentUser = user;
                System.out.println("Bienvenido, " + user.getName() + " (" + user.getNick() + ")");
                return true;
            } else {
                System.out.println("Error: Nick o contraseña incorrectos.");
                return false;
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void registerUser(String name, String nick, String password) {
        try {
            User newUser = userManager.signUp(name, nick, password);
            System.out.println("Registro completado. Tu número de registro es: " + newUser.getRegistrationNumber());
        } catch (RuntimeException e) {
            System.out.println("Error en el registro: " + e.getMessage());
        }
    }

    public void logoutUser() {
        if (currentUser != null) {
            userManager.logOut(currentUser);
            currentUser = null;
        } else {
            System.out.println("No hay ninguna sesión activa.");
        }
    }

    public void deleteAccount(String nick, String password) {
        try {
            userManager.deleteUser(nick, password);
            System.out.println("Cuenta eliminada correctamente.");
            if (currentUser != null && currentUser.getNick().equals(nick)) {
                currentUser = null;
            }
        } catch (RuntimeException e) {
            System.out.println("Error al eliminar la cuenta: " + e.getMessage());
        }
    }

    // ==========================================
    // MÉTODOS DE GESTIÓN DE PERSONAJES
    // ==========================================

    public void createCharacter(String name, String characterType) {
        if (currentUser == null) {
            System.out.println("Error: Debes iniciar sesión para crear un personaje.");
            return;
        }
        try {
            // TODO: Gestionar la elección de habilidad especial. Actualmente se asigna una por defecto en la Factory.
            characterManager.createCharacter(currentUser.getNick(), name, characterType);
            System.out.println("Personaje '" + name + "' creado correctamente para el usuario " + currentUser.getNick());
        } catch (Exception e) {
            System.out.println("Error al crear el personaje: " + e.getMessage());
        }
    }

    public void deleteCharacter() {
        if (currentUser == null) {
            System.out.println("Error: Debes iniciar sesión para dar de baja un personaje.");
            return;
        }
        try {
            Character character = characterManager.getCharacterByUser(currentUser.getNick());
            if (character != null) {
                characterManager.deleteCharacter(character.getId());
                System.out.println("Personaje eliminado correctamente.");
            } else {
                System.out.println("El usuario no tiene ningún personaje que eliminar.");
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar el personaje: " + e.getMessage());
        }
    }

    public void chooseSkill(String skillName) {
        // Lógica para que el personaje elija habilidades
    }

    public void selectMinions(List<String> minionNames) {
        // Lógica para que el personaje seleccione sus esbirros
    }

    public void addEquipment(String equipmentName) {
        // Lógica para gestionar el equipo (armas, armaduras) del personaje
    }

    public void showCharacterInfo() {
        if (currentUser == null) {
            System.out.println("Error: Debes iniciar sesión para ver tu personaje.");
            return;
        }
        Character character = characterManager.getCharacterByUser(currentUser.getNick());
        if (character == null) {
            System.out.println("No tienes ningún personaje creado.");
            return;
        }

        System.out.println("\n=== INFORMACIÓN DEL PERSONAJE ===");
        System.out.println("Nombre: " + character.getName());
        System.out.println("Vida: " + character.getLife() + "/5");
        System.out.println("Poder: " + character.getPower() + "/5");
        System.out.println("Oro: " + character.checkGold());
        System.out.println("Habilidad Especial: " + character.getSpecialSkill().getName());
        System.out.println("Esbirros: " + character.getMinions().size());
        System.out.println("Fortalezas: " + character.getStrengths().size());
        System.out.println("Debilidades: " + character.getWeaknesses().size());
    }

    // ==========================================
    // MÉTODOS DE COMBATE Y DESAFÍOS
    // ==========================================

    public void challengePlayer(String opponentNick, int goldBet) {
        // Lógica para retar a otro jugador
    }

    public void acceptChallenge(String challengeId) {
        // Lógica para aceptar un desafío pendiente
    }

    public void rejectChallenge(String challengeId) {
        // Lógica para rechazar un desafío
    }

    public void startCombat() {
        // Lógica para iniciar y resolver un combate (delegado a combatSystem)
    }
    
    // ==========================================
    // OTROS MÉTODOS GENERALES
    // ==========================================
    
    public void showGlobalRanking() {
        // Lógica para mostrar la clasificación global
    }

}

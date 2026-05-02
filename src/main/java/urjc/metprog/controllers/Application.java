package main.java.urjc.metprog.controllers;

import java.util.List;
import main.java.urjc.metprog.dao.DatabaseManager;
import main.java.urjc.metprog.dao.CharacterDaoImpl;
import main.java.urjc.metprog.dao.UserDaoImpl;
import main.java.urjc.metprog.models.user.User;

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
        // Lógica de creación de personaje delegada a characterManager
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

    public void showPlayerProfile() {
        // Lógica para obtener/mostrar el perfil del jugador y personaje
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

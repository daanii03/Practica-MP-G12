package main.java.urjc.metprog.controllers;

import java.util.List;
import main.java.urjc.metprog.dao.DatabaseManager;
import main.java.urjc.metprog.dao.CharacterDaoImpl;

public class Application {
    
    // Aquí irían las instancias de otros controladores o gestores de la aplicación
    // Por ejemplo:
    // private UserManager userManager;
    // private CombatSystem combatSystem;
    private CharacterManager characterManager;

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
        
        // 2. Instanciar lógica de negocio
        // this.userManager = new UserManager();
        // this.combatSystem = new CombatSystem();
        
        // Instanciamos el manager inyectándole la implementación real del DAO
        this.characterManager = new CharacterManager(new CharacterDaoImpl());
    }

    // ==========================================
    // MÉTODOS DE GESTIÓN DE USUARIOS
    // ==========================================

    public boolean loginUser(String username, String password) {
        // Lógica de inicio de sesión delegada a userManager
        return false;
    }

    public void registerUser(String username, String password, String nick) {
        // Lógica de registro delegada a userManager
    }

    public void logoutUser() {
        // Lógica para cerrar sesión
    }

    public void deleteAccount() {
        // Lógica para borrar la cuenta del usuario actualmente logueado
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

package main.java.urjc.metprog.controllers;

import java.util.List;

public class Application {
    
    // Aquí irían las instancias de otros controladores o gestores de la aplicación
    // Por ejemplo:
    // private UserManager userManager;
    // private CombatSystem combatSystem;
    private CharacterManager characterManager;

    public Application() {
        // Inicialización de la lógica y de los distintos controladores
        // this.userManager = new UserManager();
        // this.combatSystem = new CombatSystem();
        
        // TODO: Reemplazar null por la implementación concreta de CharacterDao cuando exista
        this.characterManager = new CharacterManager(null);
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

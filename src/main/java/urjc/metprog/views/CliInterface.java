package main.java.urjc.metprog.views;

import java.util.List;
import java.util.Scanner;
import main.java.urjc.metprog.controllers.Application;
import main.java.urjc.metprog.models.user.User;
import main.java.urjc.metprog.models.characters.Character;

/**
 * Interfaz de línea de comandos (CLI).
 * Única responsable de la interacción con el usuario y la representación de datos.
 */
public class CliInterface {

    private final Application app;
    private final Scanner scanner;

    public CliInterface(Application app) {
        this.app = app;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n========================================");
            System.out.println("       BIENVENIDO A BLOOD & BONES       ");
            System.out.println("========================================");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrar nuevo jugador");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    showLoginMenu();
                    break;
                case "2":
                    showRegisterMenu();
                    break;
                case "0":
                    exit = true;
                    System.out.println("Saliendo del juego... ¡Hasta la próxima!");
                    break;
                default:
                    System.out.println("Opción no válida, intente de nuevo.");
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n--- INICIO DE SESIÓN ---");
        System.out.print("Introduce tu nick: ");
        String nick = scanner.nextLine();
        System.out.print("Introduce tu contraseña: ");
        String password = scanner.nextLine();
        
        try {
            User user = app.loginUser(nick, password);
            System.out.println("Bienvenido, " + user.getName() + " (" + user.getNick() + ")");
            showPlayerMenu();
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showRegisterMenu() {
        System.out.println("\n--- REGISTRO DE NUEVO JUGADOR ---");
        System.out.print("Nombre completo: ");
        String name = scanner.nextLine();
        System.out.print("Nick deseado: ");
        String nick = scanner.nextLine();
        System.out.print("Contraseña (8-12 caracteres): ");
        String password = scanner.nextLine();
        
        try {
            User newUser = app.registerUser(name, nick, password);
            System.out.println("Registro completado con éxito.");
            System.out.println("Tu número de registro es: " + newUser.getRegistrationNumber());
        } catch (RuntimeException e) {
            System.out.println("Error en el registro: " + e.getMessage());
        }
    }

    private void showPlayerMenu() {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Gestionar Personaje");
            System.out.println("2. Buscar Desafíos (Próximamente)");
            System.out.println("3. Ver Ranking Global");
            System.out.println("4. Darse de baja (Eliminar cuenta)");
            System.out.println("0. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    showCharacterSubMenu();
                    break;
                case "3":
                    showGlobalRanking();
                    break;
                case "4":
                    if (showDeleteAccountMenu()) {
                        logout = true;
                    }
                    break;
                case "0":
                    app.logoutUser();
                    System.out.println("Sesión cerrada.");
                    logout = true;
                    break;
                default:
                    System.out.println("Opción no válida o función aún no implementada.");
            }
        }
    }

    private void showCharacterSubMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- GESTIÓN DE PERSONAJE ---");
            System.out.println("1. Ver información del personaje");
            System.out.println("2. Registrar nuevo personaje");
            System.out.println("3. Eliminar personaje");
            System.out.println("0. Volver al menú principal");
            System.out.print("Seleccione una opción: ");

            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    displayCharacterInfo();
                    break;
                case "2":
                    showCharacterCreationMenu();
                    break;
                case "3":
                    showCharacterDeletionMenu();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void displayCharacterInfo() {
        Character character = app.getCurrentUserCharacter();
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

    private void showCharacterCreationMenu() {
        System.out.println("\n--- CREACIÓN DE PERSONAJE ---");
        System.out.print("Nombre del personaje: ");
        String name = scanner.nextLine();
        System.out.println("Elige una raza:");
        System.out.println("1. Vampiro");
        System.out.println("2. Licántropo");
        System.out.println("3. Cazador");
        System.out.print("Opción: ");
        
        String raceOption = scanner.nextLine();
        String race = "";
        switch (raceOption) {
            case "1": race = "vampire"; break;
            case "2": race = "werewolf"; break;
            case "3": race = "hunter"; break;
            default:
                System.out.println("Raza no válida.");
                return;
        }
        
        try {
            Character c = app.createCharacter(name, race);
            System.out.println("Personaje '" + c.getName() + "' creado correctamente.");
        } catch (RuntimeException e) {
            System.out.println("Error al crear el personaje: " + e.getMessage());
        }
    }

    private void showCharacterDeletionMenu() {
        System.out.println("\n--- ELIMINAR PERSONAJE ---");
        System.out.print("¿Estás seguro de que quieres dar de baja tu personaje? (S/N): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("S")) {
            try {
                app.deleteCharacter();
                System.out.println("Personaje eliminado correctamente.");
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void showGlobalRanking() {
        List<User> ranking = app.getGlobalRanking();
        System.out.println("\n=== RANKING GLOBAL DE VICTORIAS ===");
        if (ranking.isEmpty()) {
            System.out.println("No hay usuarios registrados todavía.");
            return;
        }

        int pos = 1;
        for (User user : ranking) {
            System.out.printf("%d. %-15s - Victorias: %d%n", pos++, user.getNick(), user.getVictories());
        }
    }

    private boolean showDeleteAccountMenu() {
        System.out.println("\n--- ELIMINAR CUENTA ---");
        System.out.println("¡ATENCIÓN! Esta acción es irreversible.");
        System.out.print("Confirma tu nick: ");
        String nick = scanner.nextLine();
        System.out.print("Confirma tu contraseña: ");
        String password = scanner.nextLine();
        
        System.out.print("¿Estás seguro de que quieres borrar tu cuenta? (S/N): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("S")) {
            try {
                app.deleteAccount(nick, password);
                System.out.println("Cuenta eliminada correctamente.");
                return true;
            } catch (RuntimeException e) {
                System.out.println("Error al eliminar la cuenta: " + e.getMessage());
            }
        } else {
            System.out.println("Operación cancelada.");
        }
        return false;
    }
}

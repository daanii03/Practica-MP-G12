package main.java.urjc.metprog.views;

import java.util.Scanner;
import main.java.urjc.metprog.controllers.Application;

public class CliInterface {

    private final Application app;
    private final Scanner scanner;

    public CliInterface(Application app) {
        this.app = app;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Punto de entrada principal de la interfaz.
     */
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

    public void showLoginMenu() {
        System.out.println("\n--- INICIO DE SESIÓN ---");
        System.out.print("Introduce tu nick: ");
        String nick = scanner.nextLine();
        System.out.print("Introduce tu contraseña: ");
        String password = scanner.nextLine();
        
        if (app.loginUser(nick, password)) {
            showPlayerMenu();
        }
    }

    public void showRegisterMenu() {
        System.out.println("\n--- REGISTRO DE NUEVO JUGADOR ---");
        System.out.print("Nombre completo: ");
        String name = scanner.nextLine();
        System.out.print("Nick deseado: ");
        String nick = scanner.nextLine();
        System.out.print("Contraseña (8-12 caracteres): ");
        String password = scanner.nextLine();
        
        app.registerUser(name, nick, password);
    }

    public void showPlayerMenu() {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Gestionar Personaje");
            System.out.println("2. Buscar Desafíos (Próximamente)");
            System.out.println("3. Ver Ranking (Próximamente)");
            System.out.println("4. Darse de baja (Eliminar cuenta)");
            System.out.println("0. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    showCharacterSubMenu();
                    break;
                case "4":
                    showDeleteAccountMenu();
                    logout = true; // Al borrar la cuenta, salimos al menú principal
                    break;
                case "0":
                    app.logoutUser();
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
                    app.showCharacterInfo();
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
        
        app.createCharacter(name, race);
    }

    private void showCharacterDeletionMenu() {
        System.out.println("\n--- ELIMINAR PERSONAJE ---");
        System.out.print("¿Estás seguro de que quieres dar de baja tu personaje? (S/N): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("S")) {
            app.deleteCharacter();
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void showDeleteAccountMenu() {
        System.out.println("\n--- ELIMINAR CUENTA ---");
        System.out.println("¡ATENCIÓN! Esta acción es irreversible.");
        System.out.print("Confirma tu nick: ");
        String nick = scanner.nextLine();
        System.out.print("Confirma tu contraseña: ");
        String password = scanner.nextLine();
        
        System.out.print("¿Estás seguro de que quieres borrar tu cuenta? (S/N): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("S")) {
            app.deleteAccount(nick, password);
        } else {
            System.out.println("Operación cancelada.");
        }
    }
}

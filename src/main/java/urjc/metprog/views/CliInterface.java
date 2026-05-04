package main.java.urjc.metprog.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import main.java.urjc.metprog.controllers.Application;
import main.java.urjc.metprog.models.challenges.Challenge;
import main.java.urjc.metprog.models.challenges.CombatResult;
import main.java.urjc.metprog.models.user.User;
import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.equipment.Armor;
import main.java.urjc.metprog.models.equipment.Weapon;

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
            // Advertencia si hay un desafío pendiente de respuesta
            if (app.hasPendingChallenge()) {
                Challenge pending = app.getPendingChallengeForCurrentUser();
                System.out.println("\n  *** ATENCIÓN: Tienes un desafío pendiente de "
                        + pending.getChallenger().getNick() + " ***");
            }

            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Gestionar Personaje");
            System.out.println("2. Desafíos");
            System.out.println("3. Ver Ranking Global");
            System.out.println("4. Darse de baja (Eliminar cuenta)");
            System.out.println("0. Cerrar sesión");
            System.out.print("Seleccione una opción: ");

            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    showCharacterSubMenu();
                    break;
                case "2":
                    showChallengeSubMenu();
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
                    System.out.println("Opción no válida.");
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
            System.out.println("4. Inventario");
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
                case "4":
                    showInventorySubMenu();
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

    // ==========================================
    // SUBMENÚ DE INVENTARIO
    // ==========================================

    private void showInventorySubMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- INVENTARIO ---");
            System.out.println("1. Ver inventario");
            System.out.println("2. Cambiar arma equipada");
            System.out.println("3. Cambiar armadura equipada");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    displayInventory();
                    break;
                case "2":
                    showChangeWeaponMenu();
                    break;
                case "3":
                    showChangeArmorMenu();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void displayInventory() {
        try {
            List<Weapon> weapons = app.getAvailableWeapons();
            List<Armor> armors = app.getAvailableArmors();
            List<Weapon> activeWeapons = app.getActiveWeapons();
            Armor activeArmor = app.getActiveArmor();

            System.out.println("\n=== INVENTARIO ===");

            // Armas equipadas
            System.out.println("\n-- Armas equipadas --");
            if (activeWeapons.isEmpty()) {
                System.out.println("  (Ninguna)");
            } else {
                for (Weapon w : activeWeapons) {
                    System.out.printf("  > %s (ATK: %d | DEF: %d | %s)%n",
                            w.getName(), w.getAttackModifier(), w.getDefenseModifier(),
                            w.isOneHandWeapon() ? "1 mano" : "2 manos");
                }
            }

            // Armadura equipada
            System.out.println("\n-- Armadura equipada --");
            if (activeArmor == null) {
                System.out.println("  (Ninguna)");
            } else {
                System.out.printf("  > %s (ATK: %d | DEF: %d)%n",
                        activeArmor.getName(), activeArmor.getAttackModifier(), activeArmor.getDefenseModifier());
            }

            // Todas las armas
            System.out.println("\n-- Armas en inventario (" + weapons.size() + ") --");
            if (weapons.isEmpty()) {
                System.out.println("  (Vacío)");
            } else {
                for (int i = 0; i < weapons.size(); i++) {
                    Weapon w = weapons.get(i);
                    System.out.printf("  %d. %s (ATK: %d | DEF: %d | %s)%n",
                            i + 1, w.getName(), w.getAttackModifier(), w.getDefenseModifier(),
                            w.isOneHandWeapon() ? "1 mano" : "2 manos");
                }
            }

            // Todas las armaduras
            System.out.println("\n-- Armaduras en inventario (" + armors.size() + ") --");
            if (armors.isEmpty()) {
                System.out.println("  (Vacío)");
            } else {
                for (int i = 0; i < armors.size(); i++) {
                    Armor a = armors.get(i);
                    System.out.printf("  %d. %s (ATK: %d | DEF: %d)%n",
                            i + 1, a.getName(), a.getAttackModifier(), a.getDefenseModifier());
                }
            }
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showChangeWeaponMenu() {
        try {
            List<Weapon> activeWeapons = app.getActiveWeapons();
            List<Weapon> weapons = app.getAvailableWeapons();

            System.out.println("\n--- CAMBIAR ARMA EQUIPADA ---");

            // Mostrar armas actualmente equipadas
            System.out.println("\nArmas equipadas actualmente:");
            if (activeWeapons.isEmpty()) {
                System.out.println("  (Ninguna)");
            } else {
                for (Weapon w : activeWeapons) {
                    System.out.printf("  > %s (ATK: %d | DEF: %d | %s)%n",
                            w.getName(), w.getAttackModifier(), w.getDefenseModifier(),
                            w.isOneHandWeapon() ? "1 mano" : "2 manos");
                }
            }

            // Mostrar armas disponibles
            if (weapons.isEmpty()) {
                System.out.println("\nNo tienes armas en el inventario.");
                return;
            }

            System.out.println("\nArmas disponibles:");
            for (int i = 0; i < weapons.size(); i++) {
                Weapon w = weapons.get(i);
                System.out.printf("  %d. %s (ATK: %d | DEF: %d | %s)%n",
                        i + 1, w.getName(), w.getAttackModifier(), w.getDefenseModifier(),
                        w.isOneHandWeapon() ? "1 mano" : "2 manos");
            }

            System.out.println("\nEscribe los números de las armas a equipar separados por coma (ej: 1,3).");
            System.out.println("Recuerda: máximo 2 armas de una mano, o 1 arma de dos manos.");
            System.out.print("Selección: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Operación cancelada.");
                return;
            }

            String[] parts = input.split(",");
            List<Weapon> selected = new ArrayList<>();
            for (String part : parts) {
                int idx = Integer.parseInt(part.trim()) - 1;
                if (idx < 0 || idx >= weapons.size()) {
                    System.out.println("Índice fuera de rango: " + (idx + 1));
                    return;
                }
                selected.add(weapons.get(idx));
            }

            app.changeActiveWeapons(selected);
            System.out.println("Armas equipadas correctamente.");

        } catch (NumberFormatException e) {
            System.out.println("Error: Introduce números válidos.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showChangeArmorMenu() {
        try {
            Armor activeArmor = app.getActiveArmor();
            List<Armor> armors = app.getAvailableArmors();

            System.out.println("\n--- CAMBIAR ARMADURA EQUIPADA ---");

            // Mostrar armadura actualmente equipada
            System.out.println("\nArmadura equipada actualmente:");
            if (activeArmor == null) {
                System.out.println("  (Ninguna)");
            } else {
                System.out.printf("  > %s (ATK: %d | DEF: %d)%n",
                        activeArmor.getName(), activeArmor.getAttackModifier(), activeArmor.getDefenseModifier());
            }

            // Mostrar armaduras disponibles
            if (armors.isEmpty()) {
                System.out.println("\nNo tienes armaduras en el inventario.");
                return;
            }

            System.out.println("\nArmaduras disponibles:");
            for (int i = 0; i < armors.size(); i++) {
                Armor a = armors.get(i);
                System.out.printf("  %d. %s (ATK: %d | DEF: %d)%n",
                        i + 1, a.getName(), a.getAttackModifier(), a.getDefenseModifier());
            }

            System.out.print("\nElige el número de la armadura a equipar: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Operación cancelada.");
                return;
            }

            int idx = Integer.parseInt(input) - 1;
            if (idx < 0 || idx >= armors.size()) {
                System.out.println("Índice fuera de rango.");
                return;
            }

            app.changeActiveArmor(armors.get(idx));
            System.out.println("Armadura equipada correctamente.");

        } catch (NumberFormatException e) {
            System.out.println("Error: Introduce un número válido.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
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

    // ==========================================
    // SUBMENÚ DE DESAFÍOS
    // ==========================================

    private void showChallengeSubMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- DESAFÍOS ---");
            System.out.println("1. Desafiar a un jugador");
            System.out.println("2. Responder a desafío pendiente");
            System.out.println("3. Ver mi historial de combates");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    showChallengeSendMenu();
                    break;
                case "2":
                    showChallengeRespondMenu();
                    break;
                case "3":
                    showChallengeHistory();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    /**
     * Flujo para que el usuario actual desafíe a otro jugador.
     */
    private void showChallengeSendMenu() {
        System.out.println("\n--- DESAFIAR A UN JUGADOR ---");
        System.out.print("Nick del jugador a desafiar: ");
        String nick = scanner.nextLine().trim();

        System.out.print("Cantidad de oro a apostar: ");
        String goldStr = scanner.nextLine().trim();

        try {
            int gold = Integer.parseInt(goldStr);
            Challenge challenge = app.challengePlayer(nick, gold);
            System.out.println("\nDesafío enviado correctamente a '" + nick + "'.");
            System.out.println("Estado: PENDIENTE DE VALIDACIÓN por un operador.");
            System.out.println("Oro apostado: " + challenge.getGoldBet());
        } catch (NumberFormatException e) {
            System.out.println("Error: Introduce una cantidad de oro válida.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Flujo para que el usuario acepte o rechace un desafío publicado dirigido a él.
     */
    private void showChallengeRespondMenu() {
        Challenge challenge = app.getPendingChallengeForCurrentUser();
        if (challenge == null) {
            System.out.println("\nNo tienes ningún desafío pendiente de respuesta.");
            return;
        }

        System.out.println("\n--- DESAFÍO PENDIENTE ---");
        System.out.printf("De:          %s%n", challenge.getChallenger().getNick());
        System.out.printf("Oro apostado: %d%n", challenge.getGoldBet());
        System.out.printf("Fecha:       %s%n", challenge.getCreatedAt());
        System.out.println("\nSi rechazas, pagas el 10%% del oro apostado (" 
                + (int) Math.ceil(challenge.getGoldBet() * 0.1) + " de oro).");
        System.out.print("\n¿Aceptas el desafío? (S = Aceptar / N = Rechazar): ");
        String answer = scanner.nextLine().trim();

        if (answer.equalsIgnoreCase("S")) {
            try {
                CombatResult result = app.acceptChallenge();
                displayCombatResult(result);
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else if (answer.equalsIgnoreCase("N")) {
            try {
                app.rejectChallenge();
                int penalty = (int) Math.ceil(challenge.getGoldBet() * 0.1);
                System.out.println("Desafío rechazado. Se han descontado " + penalty + " de oro de tu personaje.");
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Opción no válida. Volviendo al menú.");
        }
    }

    /**
     * Muestra el resultado del combate ronda a ronda y el resumen final.
     */
    private void displayCombatResult(CombatResult result) {
        System.out.println("\n========================================");
        System.out.println("           RESULTADO DEL COMBATE        ");
        System.out.println("========================================");

        // Log de rondas
        System.out.println("\n--- Desarrollo del combate ---");
        for (String log : result.getRoundLog()) {
            System.out.println("  " + log);
        }

        // Resultado final
        System.out.println("\n--- Resultado final ---");
        System.out.println("Rondas jugadas: " + result.getRounds());

        if (result.isDraw()) {
            System.out.println("¡EMPATE! No se transfiere oro.");
        } else {
            System.out.println("GANADOR: " + result.getWinnerUser().getNick());
            System.out.println("PERDEDOR: " + result.getLoserUser().getNick());
            System.out.println("Oro transferido: " + result.getGoldWon());
        }
        System.out.println("========================================");
    }

    /**
     * Muestra el historial de combates del usuario actual desde la base de datos.
     */
    private void showChallengeHistory() {
        try {
            List<String[]> history = app.getChallengeHistory();

            System.out.println("\n=== HISTORIAL DE COMBATES ===");
            if (history.isEmpty()) {
                System.out.println("Aún no has participado en ningún combate.");
                return;
            }

            String currentNick = app.getCurrentUser().getNick();
            int wins = 0, losses = 0, draws = 0;

            System.out.printf("%-4s %-15s %-15s %-6s %-12s %-22s %-6s%n",
                    "#", "Retador", "Desafiado", "Rondas", "Resultado", "Fecha", "Oro");
            System.out.println("-".repeat(85));

            int i = 1;
            for (String[] row : history) {
                String challenger = row[0];
                String challenged = row[1];
                String rounds     = row[2];
                String date       = row[3] != null ? row[3].substring(0, 16).replace("T", " ") : "--";
                String winnerNick = row[4]; // null = empate
                String gold       = row[5];

                String resultado;
                if (winnerNick == null) {
                    resultado = "Empate";
                    draws++;
                } else if (winnerNick.equals(currentNick)) {
                    resultado = "Victoria";
                    wins++;
                } else {
                    resultado = "Derrota";
                    losses++;
                }

                System.out.printf("%-4d %-15s %-15s %-6s %-12s %-22s %-6s%n",
                        i++, challenger, challenged, rounds, resultado, date, gold + " oro");
            }

            System.out.println("-".repeat(85));
            System.out.printf("Victorias: %d | Derrotas: %d | Empates: %d%n", wins, losses, draws);

        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

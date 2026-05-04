package main.java.urjc.metprog.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import main.java.urjc.metprog.controllers.Application;
import main.java.urjc.metprog.models.challenges.Challenge;
import main.java.urjc.metprog.models.challenges.CombatResult;
import main.java.urjc.metprog.models.user.User;
import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.equipment.Armor;
import main.java.urjc.metprog.models.equipment.Weapon;
import main.java.urjc.metprog.models.minions.Minion;
import main.java.urjc.metprog.models.minions.HumanMinion;
import main.java.urjc.metprog.models.minions.GhoulMinion;
import main.java.urjc.metprog.models.minions.DemonMinion;
import main.java.urjc.metprog.models.minions.Loyalty;

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
            System.out.println("3. Registrar nuevo administrador");
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
                case "3":
                    showRegisterOperatorMenu();
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
            System.out.println("\nBienvenido, " + user.getName() + " (" + user.getNick() + ")");
            System.out.println("Rol: " + user.getRole().toUpperCase());

            if (user.getRole().equalsIgnoreCase("operator")) {
                showOperatorMenu();
            } else {
                showPlayerMenu();
            }
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

    private void showRegisterOperatorMenu() {
        System.out.println("\n--- REGISTRO DE NUEVO ADMINISTRADOR ---");
        System.out.print("Nombre completo: ");
        String name = scanner.nextLine();
        System.out.print("Nick deseado: ");
        String nick = scanner.nextLine();
        System.out.print("Contraseña (8-12 caracteres): ");
        String password = scanner.nextLine();
        
        try {
            app.registerOperator(name, nick, password);
            System.out.println("Registro de administrador completado con éxito.");
            System.out.println("Ahora puedes iniciar sesión con tus credenciales.");
        } catch (RuntimeException e) {
            System.out.println("Error en el registro: " + e.getMessage());
        }
    }

    private void showOperatorMenu() {
        boolean logout = false;
        while (!logout) {
            System.out.println("\n--- MENÚ DE ADMINISTRADOR ---");
            System.out.println("1. Editar personaje de usuario");
            System.out.println("2. Añadir equipo/esbirros/fortalezas/debilidades");
            System.out.println("3. Validar desafíos pendientes");
            System.out.println("4. Bloquear usuario");
            System.out.println("5. Desbloquear usuario");
            System.out.println("6. Darse de baja (Eliminar cuenta)");
            System.out.println("0. Cerrar sesión");
            System.out.print("Seleccione una opción: ");

            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    showAdminEditCharacterMenu();
                    break;
                case "2":
                    showAdminManageEquipmentMenu();
                    break;
                case "3":
                    showAdminValidateChallengesMenu();
                    break;
                case "4":
                    System.out.print("Introduce el nick del usuario a BLOQUEAR: ");
                    String blockNick = scanner.nextLine();
                    try {
                        app.blockUser(blockNick);
                    } catch (RuntimeException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "5":
                    System.out.print("Introduce el nick del usuario a DESBLOQUEAR: ");
                    String unblockNick = scanner.nextLine();
                    try {
                        app.unblockUser(unblockNick);
                    } catch (RuntimeException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "6":
                    if (showDeleteAccountMenu()) {
                        logout = true;
                    }
                    break;
                case "0":
                    app.logoutUser();
                    System.out.println("Sesión de administrador cerrada.");
                    logout = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
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
            System.out.println("5. Gestionar esbirros");
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
                case "5":
                    showPlayerMinionSubMenu();
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

    // ==========================================
    // MÉTODOS DE ADMINISTRACIÓN (OPERADOR)
    // ==========================================

    private void showAdminEditCharacterMenu() {
        java.util.Map<String, Character> characters = app.getAllCharacters();
        if (characters.isEmpty()) {
            System.out.println("\nNo hay personajes registrados en el sistema.");
            return;
        }

        System.out.println("\n--- LISTA DE PERSONAJES ---");
        List<String> owners = new ArrayList<>(characters.keySet());
        for (int i = 0; i < owners.size(); i++) {
            String owner = owners.get(i);
            Character c = characters.get(owner);
            System.out.println((i + 1) + ". " + c.getName() + " (Dueño: " + owner + ", Raza: " + c.getClass().getSimpleName() + ")");
        }
        System.out.println("0. Cancelar");
        System.out.print("Seleccione el personaje a editar: ");

        String choice = scanner.nextLine();
        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < owners.size()) {
                String selectedOwner = owners.get(index);
                Character selectedChar = characters.get(selectedOwner);
                showEditCharacterFields(selectedOwner, selectedChar);
            }
        } catch (NumberFormatException e) {
            System.out.println("Opción no válida.");
        }
    }

    private void showEditCharacterFields(String ownerNick, Character character) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- EDITANDO PERSONAJE: " + character.getName() + " ---");
            System.out.println("1. Editar Nombre (Actual: " + character.getName() + ")");
            System.out.println("2. Editar Oro (Actual: " + character.checkGold() + ")");
            System.out.println("3. Editar Vida (Actual: " + character.getLife() + ")");
            System.out.println("4. Editar Poder (Actual: " + character.getPower() + ")");
            
            // Atributos de raza
            if (character instanceof main.java.urjc.metprog.models.characters.Vampire) {
                main.java.urjc.metprog.models.characters.Vampire v = (main.java.urjc.metprog.models.characters.Vampire) character;
                System.out.println("5. Editar Edad (Actual: " + v.getAge() + ")");
                System.out.println("6. Editar Puntos de Sangre (Actual: " + v.getBloodPoints() + ")");
            } else if (character instanceof main.java.urjc.metprog.models.characters.Werewolf) {
                main.java.urjc.metprog.models.characters.Werewolf w = (main.java.urjc.metprog.models.characters.Werewolf) character;
                System.out.println("5. Editar Rabia (Actual: " + w.getRage() + ")");
            } else if (character instanceof main.java.urjc.metprog.models.characters.Hunter) {
                main.java.urjc.metprog.models.characters.Hunter h = (main.java.urjc.metprog.models.characters.Hunter) character;
                System.out.println("5. Editar Voluntad (Actual: " + h.getWill() + ")");
            }

            System.out.println("0. Finalizar edición y guardar");
            System.out.print("Seleccione qué desea editar: ");

            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    System.out.print("Nuevo nombre: ");
                    character.setName(scanner.nextLine());
                    break;
                case "2":
                    System.out.print("Nuevo oro: ");
                    try {
                        character.getInventory().setGold(Integer.parseInt(scanner.nextLine()));
                    } catch (NumberFormatException e) { System.out.println("Valor no válido."); }
                    break;
                case "3":
                    System.out.print("Nueva vida (0-5): ");
                    try {
                        character.setLife(Integer.parseInt(scanner.nextLine()));
                    } catch (NumberFormatException e) { System.out.println("Valor no válido."); }
                    break;
                case "4":
                    System.out.print("Nuevo poder (1-5): ");
                    try {
                        character.setPower(Integer.parseInt(scanner.nextLine()));
                    } catch (NumberFormatException e) { System.out.println("Valor no válido."); }
                    break;
                case "5":
                    try {
                        if (character instanceof main.java.urjc.metprog.models.characters.Vampire) {
                            System.out.print("Nueva edad: ");
                            ((main.java.urjc.metprog.models.characters.Vampire) character).setAge(Integer.parseInt(scanner.nextLine()));
                        } else if (character instanceof main.java.urjc.metprog.models.characters.Werewolf) {
                            System.out.print("Nueva rabia: ");
                            int targetRage = Integer.parseInt(scanner.nextLine());
                            System.out.println("Nota: La rabia solo puede aumentar en este modelo.");
                        } else if (character instanceof main.java.urjc.metprog.models.characters.Hunter) {
                            System.out.print("Nueva voluntad: ");
                            ((main.java.urjc.metprog.models.characters.Hunter) character).setWill(Integer.parseInt(scanner.nextLine()));
                        }
                    } catch (NumberFormatException e) { System.out.println("Valor no válido."); }
                    break;
                case "6":
                    if (character instanceof main.java.urjc.metprog.models.characters.Vampire) {
                        System.out.print("Nuevos puntos de sangre: ");
                        try {
                            ((main.java.urjc.metprog.models.characters.Vampire) character).setBloodPoints(Integer.parseInt(scanner.nextLine()));
                        } catch (NumberFormatException e) { System.out.println("Valor no válido."); }
                    }
                    break;
                case "0":
                    app.updateCharacter(ownerNick, character);
                    System.out.println("Personaje actualizado correctamente.");
                    back = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void showAdminManageEquipmentMenu() {
        java.util.Map<String, Character> characters = app.getAllCharacters();
        if (characters.isEmpty()) {
            System.out.println("\nNo hay personajes registrados.");
            return;
        }

        System.out.println("\n--- GESTIÓN DE EQUIPO ---");
        List<String> owners = new ArrayList<>(characters.keySet());
        for (int i = 0; i < owners.size(); i++) {
            String owner = owners.get(i);
            Character c = characters.get(owner);
            System.out.println((i + 1) + ". " + c.getName() + " (" + owner + ")");
        }
        System.out.print("Seleccione un personaje (0 para cancelar): ");

        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < owners.size()) {
                String owner = owners.get(index);
                Character c = characters.get(owner);
                
                System.out.println("\n1. Añadir equipo desde catálogo");
                System.out.println("2. Eliminar equipo existente");
                System.out.println("3. Añadir esbirro");
                System.out.println("4. Eliminar esbirro");
                System.out.print("Seleccione opción: ");
                String opt = scanner.nextLine();
                
                if (opt.equals("1")) {
                    showAdminAddEquipment(owner, c);
                } else if (opt.equals("2")) {
                    showAdminRemoveEquipment(owner, c);
                } else if (opt.equals("3")) {
                    showAddMinionMenu(owner);
                } else if (opt.equals("4")) {
                    showDeleteMinionMenu(owner);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showAdminAddEquipment(String owner, Character character) {
        List<String[]> catalog = app.getCatalog();
        System.out.println("\n--- CATÁLOGO GLOBAL ---");
        for (int i = 0; i < catalog.size(); i++) {
            String[] item = catalog.get(i);
            System.out.printf("%d. [%s] %s (Atk: %s, Def: %s)%n", 
                i + 1, item[2].toUpperCase(), item[1], item[3], item[4]);
        }
        System.out.print("Seleccione objeto a añadir (0 para cancelar): ");

        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < catalog.size()) {
                String[] item = catalog.get(index);
                String name = item[1];
                String type = item[2];
                int atk = Integer.parseInt(item[3]);
                int def = Integer.parseInt(item[4]);
                boolean isOneHand = item[5].equals("1");

                if (type.equals("weapon")) {
                    app.addWeaponToInventory(owner, new Weapon(name, atk, def, isOneHand));
                } else {
                    app.addArmorToInventory(owner, new Armor(name, atk, def));
                }
                System.out.println("Objeto añadido con éxito.");
            }
        } catch (Exception e) {
            System.out.println("Error al añadir objeto: " + e.getMessage());
        }
    }

    private void showAdminRemoveEquipment(String owner, Character character) {
        List<Weapon> weapons = character.getInventory().getWeapons();
        List<Armor> armors = character.getInventory().getArmors();

        System.out.println("\n--- EQUIPO ACTUAL DE " + character.getName() + " ---");
        int count = 1;
        for (Weapon w : weapons) {
            System.out.println(count++ + ". [ARMA] " + w.getName());
        }
        for (Armor a : armors) {
            System.out.println(count++ + ". [ARMADURA] " + a.getName());
        }
        System.out.print("Seleccione objeto a eliminar (0 para cancelar): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice <= 0) return;

            if (choice <= weapons.size()) {
                app.removeWeaponFromInventory(owner, weapons.get(choice - 1));
            } else if (choice <= weapons.size() + armors.size()) {
                app.removeArmorFromInventory(owner, armors.get(choice - weapons.size() - 1));
            }
            System.out.println("Objeto eliminado con éxito.");
        } catch (Exception e) {
            System.out.println("Error al eliminar objeto: " + e.getMessage());
        }
    }

    private void showAdminValidateChallengesMenu() {
        List<Challenge> pending = app.getPendingChallengesForValidation();
        if (pending.isEmpty()) {
            System.out.println("\nNo hay desafíos pendientes de validación.");
            return;
        }

        System.out.println("\n--- DESAFÍOS PENDIENTES DE VALIDACIÓN ---");
        for (int i = 0; i < pending.size(); i++) {
            Challenge c = pending.get(i);
            System.out.printf("%d. %s desafía a %s (Oro: %d)%n", 
                i + 1, c.getChallenger().getNick(), c.getChallenged().getNick(), c.getGoldBet());
        }
        System.out.print("Seleccione un desafío (0 para cancelar): ");

        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < pending.size()) {
                Challenge selected = pending.get(index);
                
                System.out.println("\n1. Validar desafío (Publicar)");
                System.out.println("2. Rechazar desafío y bloquear usuario (Infractor)");
                System.out.print("Seleccione opción: ");
                String opt = scanner.nextLine();
                
                if (opt.equals("1")) {
                    Map<String, Integer> modifiers = new HashMap<>();
                    boolean adding = true;
                    while (adding) {
                        System.out.print("\n¿Añadir modificador de escenario? (S/N): ");
                        if (scanner.nextLine().equalsIgnoreCase("s")) {
                            System.out.print("Nombre del modificador (ej. Eclipse, Lluvia): ");
                            String name = scanner.nextLine();
                            System.out.print("Valor del modificador (positivo para fortaleza, negativo para debilidad, ej. -1, 2): ");
                            try {
                                int val = Integer.parseInt(scanner.nextLine());
                                modifiers.put(name, val);
                            } catch (NumberFormatException e) { System.out.println("Valor no válido."); }
                        } else {
                            adding = false;
                        }
                    }
                    app.validateChallenge(selected, modifiers);
                    System.out.println("Desafío validado y publicado con éxito.");
                } else if (opt.equals("2")) {
                    app.rejectInvalidChallengeAndBlock(selected);
                    System.out.println("Desafío rechazado y usuario bloqueado.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ==========================================
    // SUBMENÚ DE ESBIRROS (JUGADOR)
    // ==========================================

    private void showPlayerMinionSubMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- GESTIÓN DE ESBIRROS ---");
            System.out.println("1. Ver esbirros");
            System.out.println("2. Añadir esbirro");
            System.out.println("3. Eliminar esbirro");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    displayMinions();
                    break;
                case "2":
                    showAddMinionMenu(null);
                    break;
                case "3":
                    showDeleteMinionMenu(null);
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void displayMinions() {
        try {
            List<Minion> minions = app.getCurrentUserMinions();
            System.out.println("\n=== ESBIRROS ===");
            if (minions.isEmpty()) {
                System.out.println("  (No tienes esbirros)");
                return;
            }
            printMinionList(minions);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Muestra la lista de esbirros formateada.
     */
    private void printMinionList(List<Minion> minions) {
        for (int i = 0; i < minions.size(); i++) {
            Minion m = minions.get(i);
            StringBuilder info = new StringBuilder();
            info.append(String.format("  %d. %s (Vida: %d", i + 1, m.getName(), m.getHealth()));

            if (m instanceof HumanMinion) {
                HumanMinion hm = (HumanMinion) m;
                info.append(" | Tipo: Humano | Lealtad: ").append(hm.getLoyalty());
            } else if (m instanceof GhoulMinion) {
                GhoulMinion gm = (GhoulMinion) m;
                info.append(" | Tipo: Ghoul | Dependencia: ").append(gm.getDependence());
            } else if (m instanceof DemonMinion) {
                DemonMinion dm = (DemonMinion) m;
                info.append(" | Tipo: Demonio | Pacto: ").append(dm.getPactDescription());
                info.append(" | Subordinados: ").append(dm.getMinions().size());
            }
            info.append(")");
            System.out.println(info.toString());
        }
    }

    /**
     * Menú para añadir un esbirro. Si ownerNick es null, se añade al usuario actual.
     * Si ownerNick tiene valor, se añade al personaje de ese usuario (modo admin).
     */
    private void showAddMinionMenu(String ownerNick) {
        System.out.println("\n--- AÑADIR ESBIRRO ---");
        System.out.println("Tipo de esbirro:");
        System.out.println("1. Humano");
        System.out.println("2. Ghoul");
        System.out.println("3. Demonio");
        System.out.println("0. Cancelar");
        System.out.print("Seleccione tipo: ");

        String typeOpt = scanner.nextLine();
        if (typeOpt.equals("0")) {
            System.out.println("Operación cancelada.");
            return;
        }

        // Datos comunes
        System.out.print("Nombre del esbirro: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            return;
        }

        System.out.print("Puntos de vida (1-3): ");
        int health;
        try {
            health = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Valor de vida no válido.");
            return;
        }

        Minion minion = null;
        try {
            switch (typeOpt) {
                case "1": // Humano
                    System.out.println("Nivel de lealtad:");
                    System.out.println("1. ALTA");
                    System.out.println("2. NORMAL");
                    System.out.println("3. BAJA");
                    System.out.print("Seleccione lealtad: ");
                    String loyaltyOpt = scanner.nextLine();
                    Loyalty loyalty;
                    switch (loyaltyOpt) {
                        case "1": loyalty = Loyalty.ALTA; break;
                        case "2": loyalty = Loyalty.NORMAL; break;
                        case "3": loyalty = Loyalty.BAJA; break;
                        default:
                            System.out.println("Opción de lealtad no válida.");
                            return;
                    }
                    minion = new HumanMinion(name, health, loyalty);
                    break;

                case "2": // Ghoul
                    System.out.print("Nivel de dependencia (1-5): ");
                    int dependence;
                    try {
                        dependence = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Valor de dependencia no válido.");
                        return;
                    }
                    minion = new GhoulMinion(name, health, dependence);
                    break;

                case "3": // Demonio
                    System.out.print("Descripción del pacto: ");
                    String pact = scanner.nextLine().trim();
                    if (pact.isEmpty()) {
                        System.out.println("La descripción del pacto no puede estar vacía.");
                        return;
                    }
                    minion = new DemonMinion(name, health, pact);
                    break;

                default:
                    System.out.println("Tipo de esbirro no válido.");
                    return;
            }

            if (ownerNick == null) {
                app.addMinionToCurrentUser(minion);
            } else {
                app.addMinionToCharacter(ownerNick, minion);
            }
            System.out.println("Esbirro '" + minion.getName() + "' añadido correctamente.");

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error al crear el esbirro: " + e.getMessage());
        }
    }

    /**
     * Menú para eliminar un esbirro. Si ownerNick es null, se elimina del usuario actual.
     * Si ownerNick tiene valor, se elimina del personaje de ese usuario (modo admin).
     */
    private void showDeleteMinionMenu(String ownerNick) {
        try {
            List<Minion> minions;
            if (ownerNick == null) {
                minions = app.getCurrentUserMinions();
            } else {
                Character c = app.getAllCharacters().get(ownerNick);
                if (c == null) {
                    System.out.println("El usuario no tiene personaje.");
                    return;
                }
                minions = c.getMinions();
            }

            if (minions.isEmpty()) {
                System.out.println("\nNo hay esbirros que eliminar.");
                return;
            }

            System.out.println("\n--- ELIMINAR ESBIRRO ---");
            printMinionList(minions);
            System.out.print("Seleccione el esbirro a eliminar (0 para cancelar): ");

            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice <= 0) return;

            int index = choice - 1;
            if (ownerNick == null) {
                app.deleteMinionFromCurrentUser(index);
            } else {
                app.deleteMinionFromCharacter(ownerNick, index);
            }
            System.out.println("Esbirro eliminado correctamente.");

        } catch (NumberFormatException e) {
            System.out.println("Error: Introduce un número válido.");
        } catch (IndexOutOfBoundsException | IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

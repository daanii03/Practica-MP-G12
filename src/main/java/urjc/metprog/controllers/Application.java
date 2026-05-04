package main.java.urjc.metprog.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import main.java.urjc.metprog.dao.ChallengeDaoImpl;
import main.java.urjc.metprog.dao.DatabaseManager;
import main.java.urjc.metprog.dao.CharacterDaoImpl;
import main.java.urjc.metprog.dao.UserDaoImpl;
import main.java.urjc.metprog.models.challenges.Challenge;
import main.java.urjc.metprog.models.challenges.Combat;
import main.java.urjc.metprog.models.challenges.CombatResult;
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
    private ChallengeDaoImpl challengeDao;
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
        this.challengeDao = new ChallengeDaoImpl();

        reloadActiveChallengesFromDb();
    }

    //NO BORRAR LOS SYSTEM.PRINT
    private void reloadActiveChallengesFromDb() {
        List<String[]> rows = challengeDao.getPendingChallengesFromDb();
        System.out.println("DEBUG - Desafíos cargados de BD: " + rows.size());
        for (String[] row : rows) {
            System.out.println("DEBUG - Desafío: " + row[0] + " vs " + row[1] + " estado: " + row[3]);

            User challenger = userManager.getUserByNick(row[0]);
            User challenged = userManager.getUserByNick(row[1]);
            System.out.println("DEBUG - challenger encontrado: " + (challenger != null));
            System.out.println("DEBUG - challenged encontrado: " + (challenged != null));

            if (challenger == null || challenged == null) continue;

            Character challengerChar = characterManager.getCharacterByUser(row[0]);
            Character challengedChar = characterManager.getCharacterByUser(row[1]);
            System.out.println("DEBUG - challengerChar encontrado: " + (challengerChar != null));
            System.out.println("DEBUG - challengedChar encontrado: " + (challengedChar != null));

            if (challengerChar == null || challengedChar == null) continue;

            challenger.setCharacter(challengerChar);
            challenged.setCharacter(challengedChar);

            Challenge challenge = new Challenge(challenger, challenged, Integer.parseInt(row[2]));
            if (row[3].equals("PUBLISHED")) {
                challenge.validate(new HashMap<>());
            }
            ChallengeManager.getInstance().save(challenge);
            System.out.println("DEBUG - Desafío añadido al ChallengeManager");
        }
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

    public User registerUser(String name, String nick, String password) {
        return userManager.signUp(name, nick, password);
    }

    /**
     * Registra un nuevo operador (administrador).
     * @return El usuario recién creado.
     */
    public User registerOperator(String name, String nick, String password) {
        return userManager.signUpOperator(name, nick, password);
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

    /**
     * Bloquea a un usuario (acción de administrador).
     */
    public void blockUser(String nick) {
        userManager.blockUser(nick);
    }

    /**
     * Desbloquea a un usuario (acción de administrador).
     */
    public void unblockUser(String nick) {
        userManager.unblockUser(nick);
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
    // MÉTODOS DE ADMINISTRACIÓN (OPERADOR)
    // ==========================================

    /**
     * Obtiene todos los personajes de la base de datos con sus dueños.
     * Solo accesible por operadores (aunque el control de rol debe estar en la vista).
     */
    public java.util.Map<String, Character> getAllCharacters() {
        return characterManager.getAllCharacters();
    }

    /**
     * Actualiza los datos de un personaje en la base de datos.
     * 
     * @param ownerNick Nick del dueño del personaje.
     * @param character Objeto character con los nuevos datos.
     */
    public void updateCharacter(String ownerNick, Character character) {
        characterManager.updateCharacter(ownerNick, character);
    }

    /**
     * Obtiene el catálogo de objetos predefinidos.
     */
    public java.util.List<String[]> getCatalog() {
        return characterManager.getCatalog();
    }

    /**
     * Añade un arma al inventario de un usuario.
     */
    public void addWeaponToInventory(String ownerNick, Weapon weapon) {
        characterManager.addWeaponToInventory(ownerNick, weapon);
    }

    /**
     * Añade una armadura al inventario de un usuario.
     */
    public void addArmorToInventory(String ownerNick, Armor armor) {
        characterManager.addArmorToInventory(ownerNick, armor);
    }

    /**
     * Elimina un arma del inventario de un usuario.
     */
    public void removeWeaponFromInventory(String ownerNick, Weapon weapon) {
        characterManager.removeWeaponFromInventory(ownerNick, weapon);
    }

    /**
     * Elimina una armadura del inventario de un usuario.
     */
    public void removeArmorFromInventory(String ownerNick, Armor armor) {
        characterManager.removeArmorFromInventory(ownerNick, armor);
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

    // ==========================================
    // MÉTODOS DE DESAFÍOS
    // ==========================================

    /**
     * El usuario actual desafía al jugador con el nick indicado.
     * Crea el desafío en estado PENDING_VALIDATION y lo registra en el ChallengeManager.
     *
     * Validaciones previas:
     * - El usuario actual debe tener personaje.
     * - El desafiado debe existir y tener personaje.
     * - El desafiado NO puede haber perdido un combate en las últimas 24 horas.
     * - El desafiador debe tener suficiente oro.
     * - Ninguno puede tener ya un desafío activo (PENDING o PUBLISHED).
     *
     * @param challengedNick Nick del jugador a desafiar.
     * @param goldBet        Cantidad de oro apostada.
     * @return El desafío creado.
     */
    public Challenge challengePlayer(String challengedNick, int goldBet) {
        if (currentUser == null) {
            throw new IllegalStateException("Debes iniciar sesión para desafiar a otro jugador.");
        }

        // Obtener personaje del retador y asignarlo al objeto User
        Character challengerChar = characterManager.getCharacterByUser(currentUser.getNick());
        if (challengerChar == null) {
            throw new IllegalStateException("Debes tener un personaje creado para poder desafiar.");
        }
        currentUser.setCharacter(challengerChar);

        // Verificar que el retador no tenga ya un desafío activo
        if (ChallengeManager.getInstance().hasActiveChallenge(currentUser)) {
            throw new IllegalStateException("Ya tienes un desafío activo pendiente de resolución.");
        }

        // Obtener el usuario desafiado
        User challenged = userManager.getUserByNick(challengedNick);
        if (challenged == null) {
            throw new IllegalArgumentException("No existe ningún jugador con el nick '" + challengedNick + "'.");
        }
        if (challenged.getNick().equals(currentUser.getNick())) {
            throw new IllegalArgumentException("No puedes desafiarte a ti mismo.");
        }

        // Cargar el personaje del desafiado (necesario para la regla de las 24h y para el combate)
        Character challengedChar = characterManager.getCharacterByUser(challengedNick);
        if (challengedChar == null) {
            throw new IllegalStateException("El jugador '" + challengedNick + "' no tiene personaje y no puede ser desafiado.");
        }
        challenged.setCharacter(challengedChar);

        // Comprobar que el desafiado no perdió un combate en las últimas 24 horas
        // (obtenemos su userId desde la BD y consultamos el historial)
        LocalDateTime lastLoss = challengeDao.getLastLossDate(getUserIdByNick(challengedNick));
        if (lastLoss != null && lastLoss.isAfter(LocalDateTime.now().minusHours(24))) {
            throw new IllegalStateException(
                "'" + challengedNick + "' perdió un combate hace menos de 24 horas y no puede ser desafiado todavía.");
        }

        // Verificar que el desafiado no tenga ya un desafío activo
        if (ChallengeManager.getInstance().hasActiveChallenge(challenged)) {
            throw new IllegalStateException("El jugador '" + challengedNick + "' ya tiene un desafío activo.");
        }

        // Al final de challengePlayer(), antes del return:
        Challenge challenge = new Challenge(currentUser, challenged, goldBet);
        challengeDao.savePendingChallenge(challenge);
        System.out.println("DEBUG - Desafío guardado en BD: " + challenge.getChallenger().getNick()); // <-- añade esto
        challengeDao.savePendingChallenge(challenge); // <-- AÑADE ESTO
        return challenge;
    }

    /**
     * Comprueba si el usuario actual tiene un desafío publicado (PUBLISHED) esperando su respuesta.
     * La CLI puede usar esto para bloquear el resto de opciones de menú.
     *
     * @return true si hay un desafío publicado dirigido al usuario actual.
     */
    public boolean hasPendingChallenge() {
        if (currentUser == null) return false;
        return ChallengeManager.getInstance()
                .findPublishedChallengeForUser(currentUser)
                .isPresent();
    }

    /**
     * Obtiene el desafío publicado que está esperando la respuesta del usuario actual.
     * Devuelve null si no hay ninguno.
     *
     * @return El desafío pendiente, o null.
     */
    public Challenge getPendingChallengeForCurrentUser() {
        if (currentUser == null) return null;
        return ChallengeManager.getInstance()
                .findPublishedChallengeForUser(currentUser)
                .orElse(null);
    }

    /**
     * Devuelve todos los desafíos pendientes de validación por el operador.
     * Solo debería ser llamado desde la interfaz del administrador.
     *
     * @return Lista de desafíos en estado PENDING_VALIDATION.
     */
    public List<Challenge> getPendingChallengesForValidation() {
        return ChallengeManager.getInstance().findPendingValidation();
    }

    /**
     * (Acción de operador) Valida un desafío pendiente y lo publica para que el desafiado pueda responder.
     * El operador puede aplicar modificadores de combate activos (fortalezas/debilidades del escenario).
     *
     * @param challenge       El desafío a validar.
     * @param activeModifiers Modificadores de escenario que el operador decide activar (puede ser null).
     */
    public void validateChallenge(Challenge challenge, Map<String, Integer> activeModifiers) {
        userManager.validateChallenge(challenge, activeModifiers);
        challengeDao.updatePendingChallengeStatus(
                challenge.getChallenger().getNick(), "PUBLISHED"); // <-- AÑADE
    }

    /**
     * (Acción de operador) Rechaza un desafío que rompe las reglas y bloquea al usuario desafiante.
     */
    public void rejectInvalidChallengeAndBlock(Challenge challenge) {
        userManager.rejectInvalidChallengeAndBlock(challenge);
        challengeDao.updatePendingChallengeStatus(
                challenge.getChallenger().getNick(), "REJECTED"); // <-- AÑADE
    }

    /**
     * El usuario actual acepta el desafío que tiene pendiente.
     * Ejecuta el combate de forma automática y persiste el resultado.
     *
     * @return El resultado del combate.
     */
    public CombatResult acceptChallenge() {
        Challenge challenge = getPendingChallengeForCurrentUser();
        if (challenge == null) {
            throw new IllegalStateException("No tienes ningún desafío pendiente de aceptar.");
        }

        // Asegurarse de que los personajes están cargados en los objetos User
        challenge.getChallenger().setCharacter(
                characterManager.getCharacterByUser(challenge.getChallenger().getNick()));
        challenge.getChallenged().setCharacter(
                characterManager.getCharacterByUser(challenge.getChallenged().getNick()));

        challenge.accept();

        // Ejecutar el combate
        UserDaoImpl userDaoImpl = new UserDaoImpl();
        Combat combat = new Combat(challenge, userDaoImpl);
        CombatResult result = combat.run();

        // Persistir el resultado en la base de datos
        User winner = result.getWinnerUser();
        Integer winnerId = winner != null ? getUserIdByNick(winner.getNick()) : null;
        challengeDao.addChallenge(
                getUserIdByNick(challenge.getChallenger().getNick()),
                getUserIdByNick(challenge.getChallenged().getNick()),
                result.getRounds(),
                result.getDate().toString(),
                winnerId,
                result.challengerHasSurvivingMinions() ? 1 : 0,
                result.challengedHasSurvivingMinions() ? 1 : 0,
                result.getGoldWon()
        );

        // Actualizar los personajes en la BD
        characterManager.updateCharacter(
                challenge.getChallenger().getNick(), challenge.getChallenger().getCharacter());
        characterManager.updateCharacter(
                challenge.getChallenged().getNick(), challenge.getChallenged().getCharacter());

        // Marcar el desafío pendiente como FINISHED en la BD
        challengeDao.updatePendingChallengeStatus(
                challenge.getChallenger().getNick(), "FINISHED");

        return result;
    }

    public void rejectChallenge() {
        Challenge challenge = getPendingChallengeForCurrentUser();
        if (challenge == null) {
            throw new IllegalStateException("No tienes ningún desafío pendiente de rechazar.");
        }

        // Asegurarse de que los personajes están cargados para aplicar la penalización de oro
        challenge.getChallenger().setCharacter(
                characterManager.getCharacterByUser(challenge.getChallenger().getNick()));
        challenge.getChallenged().setCharacter(
                characterManager.getCharacterByUser(challenge.getChallenged().getNick()));

        challenge.reject(); // Aplica el 10% de penalización en oro

        // Persistir los cambios de oro en ambos personajes
        characterManager.updateCharacter(
                challenge.getChallenger().getNick(), challenge.getChallenger().getCharacter());
        characterManager.updateCharacter(
                challenge.getChallenged().getNick(), challenge.getChallenged().getCharacter());

        // Marcar el desafío pendiente como REJECTED en la BD
        challengeDao.updatePendingChallengeStatus(
                challenge.getChallenger().getNick(), "REJECTED");
    }

    /**
     * Obtiene el historial de combates del usuario actual.
     * Cada elemento contiene: [challenger_nick, challenged_nick, rounds, combat_date, winner_nick, gold_won].
     * winner_nick es null si fue empate.
     *
     * @return Lista con los datos de cada combate.
     */
    public List<String[]> getChallengeHistory() {
        if (currentUser == null) {
            throw new IllegalStateException("Debes iniciar sesión para ver tu historial.");
        }
        int userId = getUserIdByNick(currentUser.getNick());
        return challengeDao.getChallengeHistoryByUser(userId);
    }

    // ==========================================
    // MÉTODO AUXILIAR PRIVADO
    // ==========================================


    /**
     * Obtiene el ID de base de datos de un usuario dado su nick.
     * Necesario para operaciones con ChallengeDao que trabajan con IDs numéricos.
     *
     * @param nick Nick del usuario.
     * @return El ID del usuario, o -1 si no se encuentra.
     */
    private int getUserIdByNick(String nick) {
        return new UserDaoImpl().getUserId(nick);
    }
}

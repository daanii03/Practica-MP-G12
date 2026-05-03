package main.java.urjc.metprog.controllers;

import main.java.urjc.metprog.dao.UserDao;
import main.java.urjc.metprog.models.user.User;
import main.java.urjc.metprog.models.user.UserBuilder;
import java.util.Random;

/**
 * Gestor de usuarios. Controla el registro, login, bajas y bloqueos de
 * jugadores y operadores.
 */
public class UserManager {

    private final UserDao userDao;

    public UserManager(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Registra un jugador estándar en el sistema.
     */
    public User signUp(String name, String nick, String password) {
        if (password.length() < 8 || password.length() > 12) {
            throw new RuntimeException("La contraseña debe tener entre 8 y 12 caracteres.");
        }
        if (userDao.getUser(nick) != null) {
            throw new RuntimeException("Ese nick ya está registrado.");
        }

        String numRegistration = createRecordNumber();
        User newUser = new UserBuilder()
                .setName(name)
                .setNick(nick)
                .setPassword(password)
                .setRegistrationNumber(numRegistration)
                .setRole("player") // Asignamos rol de jugador
                .setBlocked(false) // Por defecto no está bloqueado
                .build();

        userDao.insertUser(newUser);
        return newUser;
    }

    /**
     * Registra un operador en el sistema. Los operadores no tienen número de
     * registro.
     */
    public User signUpOperator(String name, String nick, String password) {
        if (password.length() < 8 || password.length() > 12) {
            throw new RuntimeException("La contraseña debe tener entre 8 y 12 caracteres.");
        }
        if (userDao.getUser(nick) != null) {
            throw new RuntimeException("Ese nick ya está registrado.");
        }

        User newOperator = new UserBuilder()
                .setName(name)
                .setNick(nick)
                .setPassword(password)
                .setRegistrationNumber(null) // El operador NO tiene número de registro
                .setRole("operator") // Asignamos rol de operador
                .setBlocked(false)
                .build();

        userDao.insertUser(newOperator);
        return newOperator;
    }

    /**
     * Inicia sesión comprobando credenciales y si el usuario está bloqueado.
     */
    public User logIn(String nick, String password) {
        User user = userDao.getUser(nick);

        if (user != null && user.getPassword().equals(password)) {
            if (user.isBlocked()) {
                throw new RuntimeException("Acceso denegado: Este usuario ha sido bloqueado por un operador.");
            }
            return user; // Login exitoso
        }
        return null; // Usuario no encontrado o contraseña errónea
    }

    /**
     * Cerrar sesion
     */
    public void logOut(User userLog) {
        System.out.println("Sesión cerrada para el usuario: " + userLog.getNick());
    }

    /**
     * Da de baja al usuario eliminándolo de la BBDD.
     */
    public void deleteUser(String nick, String password) {
        User user = logIn(nick, password); // logIn ya comprueba la contraseña
        if (user != null) {
            userDao.deleteUser(nick);
        } else {
            throw new RuntimeException("Credenciales incorrectas para dar de baja.");
        }
    }

    // ==========================================
    // FUNCIONES EXCLUSIVAS DEL OPERADOR
    // ==========================================

    /**
     * Bloquea a un usuario infractor.
     * 
     * @param nick Nick del usuario a bloquear.
     */
    public void blockUser(String nick) {
        User user = userDao.getUser(nick);
        if (user != null) {
            user.setBlocked(true);
            userDao.updateUser(user); // Asumimos que el DAO tiene un método UPDATE
            System.out.println("Usuario " + nick + " bloqueado correctamente.");
        } else {
            throw new RuntimeException("No se encontró al usuario con nick: " + nick);
        }
    }

    /**
     * Desbloquea a un usuario.
     * 
     * @param nick Nick del usuario a desbloquear.
     */
    public void unblockUser(String nick) {
        User user = userDao.getUser(nick);
        if (user != null) {
            user.setBlocked(false);
            userDao.updateUser(user);
            System.out.println("Usuario " + nick + " desbloqueado correctamente.");
        } else {
            throw new RuntimeException("No se encontró al usuario con nick: " + nick);
        }
    }

    /**
     * Genera un número de registro único (Formato LNNLL) comprobando
     * que no exista previamente en la base de datos.
     */

    private String createRecordNumber() {
        String newNumber;
        do {
            newNumber = createRandomFormat();

        } while (userDao.getUserByRegistrationNumber(newNumber) != null);


        return newNumber;
    }

    /**
     * Función auxiliar que crea el string con el formato
     * Letra-Número-Número-Letra-Letra.
     */
    private String createRandomFormat() {

        Random rand = new Random();
        char l1 = (char) ('A' + rand.nextInt(26));
        int n1 = rand.nextInt(10);
        int n2 = rand.nextInt(10);
        char l2 = (char) ('A' + rand.nextInt(26));
        char l3 = (char) ('A' + rand.nextInt(26));

        return "" + l1 + n1 + n2 + l2 + l3;

    }

    /**
     * Devuelve el ranking global de usuarios ordenado por victorias.
     */
    public java.util.List<User> getRanking() {
        return userDao.getGlobalRanking();
    }
}

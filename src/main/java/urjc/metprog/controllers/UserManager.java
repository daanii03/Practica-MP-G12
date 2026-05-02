package main.java.urjc.metprog.controllers;

import main.java.urjc.metprog.dao.UserDao;
import main.java.urjc.metprog.models.user.User;
import main.java.urjc.metprog.models.user.UserBuilder;
import java.util.Random;

public class UserManager {

    private final UserDao userDao;

    public UserManager(UserDao userDao) {
        this.userDao = userDao;
    }

    public User registrarUsuario(String nombre, String nick, String password) {

        // Validamos la contraseña
        if (password.length() < 8 || password.length() > 12) {
            throw new IllegalArgumentException("La contraseña debe tener entre 8 y 12 caracteres.");
        }

        //Creamos el numero de registro del nuevo usuario
        String numRegistro = generarNumeroRegistro();

        User nuevoUsuario = new UserBuilder()
                .setName(nombre)
                .setNick(nick)
                .setPassword(password)
                .setRegistrationNumber(numRegistro)
                .build();

        userDao.insertUser(nuevoUsuario);
        return nuevoUsuario;
    }

    public User inicioSesion(String nick, String password) {
        User user = userDao.getUser(nick);
        if (user != null && user.getPassword().equals(password)) {
            return user; // Login exitoso
        }
        return null; // Usuario no encontrado
    }

    public void darBajaUsuario(String nick, String password) {
        User user = inicioSesion(nick, password);
        if (user != null) {
            userDao.deleteUser(nick);
        } else {
            throw new IllegalArgumentException("Credenciales incorrectas para dar de baja.");
        }
    }


    private String generarNumeroRegistro() {
        // Formato LNNLL (Letra, Número, Número, Letra, Letra)
        Random rand = new Random();
        char l1 = (char) ('A' + rand.nextInt(26));
        int n1 = rand.nextInt(10);
        int n2 = rand.nextInt(10);
        char l2 = (char) ('A' + rand.nextInt(26));
        char l3 = (char) ('A' + rand.nextInt(26));
        String numRegistro = "" + l1 + n1 + n2 + l2 + l3;
        return numRegistro;
    }
}
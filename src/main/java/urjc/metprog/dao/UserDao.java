package main.java.urjc.metprog.dao;

import main.java.urjc.metprog.models.user.User;

public interface UserDao {
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(String nick);
    User getUser(String nick);
    /**
     * Busca un usuario en la base de datos por su número de registro.
     * @param numReg Número de registro a buscar.
     * @return El objeto User si lo encuentra, o null si está libre.
     */
    User getUserByRegistrationNumber(String numReg);
    java.util.List<User> getGlobalRanking();
}


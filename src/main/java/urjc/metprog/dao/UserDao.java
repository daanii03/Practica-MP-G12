package main.java.urjc.metprog.dao;

import main.java.urjc.metprog.models.user.User;

public interface UserDao {
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(int id);
}

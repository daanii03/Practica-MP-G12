package main.java.urjc.metprog.controllers;

import main.java.urjc.metprog.models.user.User;
import main.java.urjc.metprog.models.user.UserBuilder;

public class UserManager {


    public void newUser(){
        UserBuilder userBuilder = new UserBuilder();
        //pongo todos los set
        userBuilder.setName("a").setNick("a");
        User user = new User(userBuilder); //por confirma que esta bien
    }

}

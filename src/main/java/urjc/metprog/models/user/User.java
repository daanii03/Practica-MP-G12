package main.java.urjc.metprog.models.user;

import main.java.urjc.metprog.models.characters.Character;

public class User {
    private String name;
    private String nick;
    private String password;
    private String registrationNumber; // Cambiado a String para el formato LNNLL
    private Character myCharacter;

    public User(UserBuilder userBuilder) {
        this.name = userBuilder.getName();
        this.nick = userBuilder.getNick();
        this.password = userBuilder.getPassword();
        this.registrationNumber = userBuilder.getRegistrationNumber();
        this.myCharacter = userBuilder.getCharacter();
    }

    // Getters necesarios para guardar en la BD y para la lógica del sistema
    public String getName() { return name; }
    public String getNick() { return nick; }
    public String getPassword() { return password; }
    public String getRegistrationNumber() { return registrationNumber; }
    public Character getCharacter() { return myCharacter; }
    public void setCharacter(Character character) { this.myCharacter = character; }
}
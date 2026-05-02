package main.java.urjc.metprog.models.user;

import main.java.urjc.metprog.models.characters.Character;

public class UserBuilder {
    private String name;
    private String nick;
    private String password;
    private String registrationNumber;
    private Character character;
    private int victories;
    private String role;
    private boolean isBlocked;

    // Getters
    public String getName() { return name; }
    public String getNick() { return nick; }
    public String getPassword() { return password; }
    public String getRegistrationNumber() { return registrationNumber; }
    public Character getCharacter() { return character; }
    public int getVictories() { return victories; }


    public String getRole() { return role; }
    public boolean isBlocked() { return isBlocked; }

    // Setters
    public UserBuilder setName(String name) { this.name = name; return this; }
    public UserBuilder setNick(String nick) { this.nick = nick; return this; }
    public UserBuilder setPassword(String password) { this.password = password; return this; }
    public UserBuilder setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
        return this;
    }
    public UserBuilder setCharacter(Character character) { this.character = character; return this; }
    public UserBuilder setVictories(int victories) { this.victories = victories; return this; }

    public UserBuilder setRole(String role) { this.role = role; return this; }
    public UserBuilder setBlocked(boolean isBlocked) { this.isBlocked = isBlocked; return this; }

    public User build(){
        return new User(this);
    }
}

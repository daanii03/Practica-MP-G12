package main.java.urjc.metprog.models.user;

import main.java.urjc.metprog.models.characters.Character;

public class User {
    private String name;
    private String nick;
    private String password;
    private String registrationNumber;
    private Character myCharacter;
    private int victories;

    private String role;
    private boolean isBlocked;

    public User(UserBuilder userBuilder) {
        this.name = userBuilder.getName();
        this.nick = userBuilder.getNick();
        this.password = userBuilder.getPassword();
        this.registrationNumber = userBuilder.getRegistrationNumber();
        this.myCharacter = userBuilder.getCharacter();
        this.victories = userBuilder.getVictories();

        this.role = userBuilder.getRole();
        this.isBlocked = userBuilder.isBlocked();
    }

    //
    public String getName() { return name; }
    public String getNick() { return nick; }
    public String getPassword() { return password; }
    public String getRegistrationNumber() { return registrationNumber; }
    public Character getCharacter() { return myCharacter; }
    public int getVictories() { return victories; }
    public String getRole() { return role; }


    public void setBlocked(boolean blocked) { this.isBlocked = blocked; }
    public void setCharacter(Character character) { this.myCharacter = character; }
    public void setRole(String role) { this.role = role; }
    public void setVictories(int victories) { this.victories = victories; }

    public boolean isBlocked() { return isBlocked; }
    public void addVictory() { this.victories++; }

}
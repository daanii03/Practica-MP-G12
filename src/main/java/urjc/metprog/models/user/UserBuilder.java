package main.java.urjc.metprog.models.user;


import main.java.urjc.metprog.models.characters.Character;

public class UserBuilder {
    private String name;
    private String nick;
    private String password;
    private String RegistrationNumber;
    private Character character;
    private int victories;


    // Getters

    public String getName() {
        return name;
    }

    public String getNick() {
        return nick;
    }

    public String getPassword() {
        return password;
    }

    public String getRegistrationNumber() {
        return RegistrationNumber;
    }

    public Character getCharacter() {
        return character;
    }

    public int getVictories() {
        return victories;
    }


    // Setters


    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder setNick(String nick) {
        this.nick = nick;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setRegistrationNumber(String registrationNumber) {
        RegistrationNumber = registrationNumber;
        return this;
    }

    public UserBuilder setCharacter(Character character) {
        this.character = character;
        return this;
    }

    public UserBuilder setVictories(int victories) {
        this.victories = victories;
        return this;
    }
    public User build(){
        return new User(this);
    }
}


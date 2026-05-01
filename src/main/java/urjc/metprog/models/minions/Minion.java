package main.java.urjc.metprog.models.minions;

public abstract class Minion {
    private String name;
    private int health;

    public Minion(String name, int health){
        this.name = name;
        this.health = validateHealth(health);
    }

    private static int validateHealth(int health) {
        if (health < 1 || health > 3)
            throw new IllegalArgumentException("La vida debe estar entre 1 y 3");
        return health;
    }

    // Getters

    public String getName(){
        return this.name;
    }

    public int getHealth(){
        return this.health;
    }

    // Setters

    public void setName(String name){
        this.name = name;
    }

    public void setHealth(int health) {
        this.health = validateHealth(health);
    }
}

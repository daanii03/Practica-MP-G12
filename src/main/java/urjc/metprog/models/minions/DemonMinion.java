package main.java.urjc.metprog.models.minions;

import java.util.ArrayList;
import java.util.List;

public class DemonMinion extends Minion {

    private String pactDescription;
    private final List<Minion> minions = new ArrayList<>();

    public DemonMinion(String name, int health, String pactDescription) {
        super(name, health);
        this.pactDescription = pactDescription;
    }

    // Getters

    public String getPactDescription(){
        return this.pactDescription;
    }

    public List<Minion> getMinions(){
        return this.minions;
    }

    // Setters

    public void setPactDescription(String pactDescription){
        this.pactDescription = pactDescription;
    }

    // Methods

    public void addMinion(Minion minion){
        this.minions.add(minion);
    }

}

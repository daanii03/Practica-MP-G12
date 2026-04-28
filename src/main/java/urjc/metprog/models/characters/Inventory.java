package main.java.urjc.metprog.models.characters;

public class Inventory {
    private int gold;

    // Getters
    public int getGold() {
        return this.gold;
    }

    // Setters
    public void setGold(int gold){
        this.gold = gold;
    }

    public void addGold(int gold){
        this.gold += gold;
    }

    public void decreaseGold(int gold){
        this.gold -= gold;
    }
}

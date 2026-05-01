package main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.equipment.Armor;
import main.java.urjc.metprog.models.equipment.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private int gold;
    private Armor activeArmor;
    private List<Weapon> activeWeapons = new ArrayList<>();


    // Getters
    public int getGold() {
        return this.gold;
    }

    public Armor getActiveArmor() {
        return activeArmor;
    }

    public List<Weapon> getActiveWeapons() {
        return activeWeapons;
    }

    // Setters
    public void setGold(int gold){
        this.gold = gold;
    }

    public void setActiveArmor(Armor activeArmor) {
        this.activeArmor = activeArmor;
    }

    public void setActiveWeapons(List<Weapon> activeWeapons) {
        this.activeWeapons = activeWeapons;
    }

    // Methods

    public void addGold(int gold){
        this.gold += gold;
    }

    public void decreaseGold(int gold){
        this.gold -= gold;
        if (this.gold < 0)
            this.gold = 0;
    }
}

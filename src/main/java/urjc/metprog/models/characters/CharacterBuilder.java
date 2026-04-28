package main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.equipment.Armor;
import main.java.urjc.metprog.models.equipment.Weapon;

import java.util.ArrayList;
import java.util.List;

public class CharacterBuilder {

    private String name;
    private List<Weapon> activeWeapons = new ArrayList<>();
    private Armor activeArmor;
    private int life;
    private int power;
    private int id;
    private SpecialSkill specialSkill;
    private Inventory inventory;
    private List<Minion> minions = new ArrayList<>();

    // Getters

    public String getName() {
        return name;
    }

    public List<Weapon> getActiveWeapons() {
        return activeWeapons;
    }

    public Armor getActiveArmor() {
        return activeArmor;
    }

    public int getLife() {
        return life;
    }

    public int getPower() {
        return power;
    }

    public int getId() {
        return id;
    }

    public SpecialSkill getSpecialSkill() {
        return specialSkill;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<Minion> getMinions() {
        return minions;
    }

    // Setters


    public CharacterBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CharacterBuilder activeWeapons(List<Weapon> activeWeapons) {
        this.activeWeapons = activeWeapons;
        return this;
    }

    public CharacterBuilder activeArmor(Armor activeArmor) {
        this.activeArmor = activeArmor;
        return this;
    }

    public CharacterBuilder life(int life) {
        this.life = life;
        return this;
    }

    public CharacterBuilder power(int power) {
        this.power = power;
        return this;
    }


    public CharacterBuilder specialSkill(SpecialSkill specialSkill) {
        this.specialSkill = specialSkill;
        return this;
    }

    public CharacterBuilder inventory(Inventory inventory) {
        this.inventory = inventory;
        return this;
    }

    public CharacterBuilder minions(List<Minion> minions) {
        this.minions = minions;
        return this;
    }

}

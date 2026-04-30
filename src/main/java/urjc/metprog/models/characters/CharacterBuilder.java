package main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.equipment.Armor;
import main.java.urjc.metprog.models.equipment.Weapon;
import main.java.urjc.metprog.models.skills.SpecialSkill;

import java.util.ArrayList;
import java.util.List;

public class CharacterBuilder {

    // Attributes

    private String name;
    private final List<Weapon> activeWeapons = new ArrayList<>();
    private Armor activeArmor;
    private int life;
    private int power;
    private int id;
    private SpecialSkill specialSkill;
    private Inventory inventory;
    private final List<Minion> minions = new ArrayList<>();

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

    public CharacterBuilder addActiveWeapon(Weapon weapon) {
        this.activeWeapons.add(weapon);
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

    public CharacterBuilder id(int id){
        this.id = id;
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

    public CharacterBuilder addMinion(Minion minion) {
        this.minions.add(minion);
        return this;
    }
}

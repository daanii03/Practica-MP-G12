package main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.minions.Minion;
import main.java.urjc.metprog.models.modifiers.Strength;
import main.java.urjc.metprog.models.modifiers.Weakness;
import main.java.urjc.metprog.models.skills.SpecialSkill;

import java.util.ArrayList;
import java.util.List;

public class CharacterBuilder {

    // Attributes

    private String name;
    private int life;
    private int power;
    private int id;
    private SpecialSkill specialSkill;
    private Inventory inventory;
    private final List<Minion> minions = new ArrayList<>();
    private final List<Weakness> weaknesses = new ArrayList<>();
    private final List<Strength> strengths = new ArrayList<>();

    // Getters

    public String getName() {
        return name;
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

    public List<Weakness> getWeaknesses() {
        return this.weaknesses;
    }

    public List<Strength> getStrengths(){
        return this.strengths;
    }

    // Setters

    public CharacterBuilder name(String name) {
        this.name = name;
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

    public CharacterBuilder addWeakness(Weakness weakness){
        this.weaknesses.add(weakness);
        return this;
    }

    public CharacterBuilder addStrength(Strength strength){
        this.strengths.add(strength);
        return this;
    }


}

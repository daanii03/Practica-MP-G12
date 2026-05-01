package main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.minions.Minion;
import main.java.urjc.metprog.models.modifiers.Strength;
import main.java.urjc.metprog.models.modifiers.Weakness;
import main.java.urjc.metprog.models.skills.SpecialSkill;

import java.util.ArrayList;
import java.util.List;


public abstract class Character {

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

    public Character(CharacterBuilder characterBuilder){
        this.name = characterBuilder.getName();
        this.id = characterBuilder.getId();
        this.life = validateLife(characterBuilder.getLife());
        this.power = validatePower(characterBuilder.getPower());
        this.specialSkill = characterBuilder.getSpecialSkill();
        this.inventory = characterBuilder.getInventory();
        this.weaknesses.addAll(characterBuilder.getWeaknesses());
        this.strengths.addAll(characterBuilder.getStrengths());
        this.minions.addAll(characterBuilder.getMinions());
    }

    private static int validateLife(int life){
        if (life < 0 || life > 5)
            throw new IllegalArgumentException("La vida debe estar entre 0 y 5");
        return life;
    }

    private static int validatePower(int power){
        if (power < 1 || power > 5)
            throw new IllegalArgumentException("El poder debe estar entre 1 y 5");
        return power;
    }

    // Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setLife(int life){
        this.life = validateLife(life);
    }

    public void setPower(int power){
        this.power = validatePower(power);
    }

    public void setMinions(List<Minion> minions){
        this.minions.clear();
        if (minions != null)
            this.minions.addAll(minions);
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setSpecialSkill(SpecialSkill specialSkill) {
        this.specialSkill = specialSkill;
    }


    // Getters

    public String getName(){
        return this.name;
    }
    
    public int getLife(){
        return this.life;
    }

    public int getPower(){
        return this.power;
    }

    public int getId(){
        return this.id;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public SpecialSkill getSpecialSkill() {
        return specialSkill;
    }

    public List<Minion> getMinions(){ return this.minions; }

    public List<Weakness> getWeaknesses(){
        return this.weaknesses;
    }

    public List<Strength> getStrengths(){
        return this.strengths;
    }

    // Methods

    public void increaseLife(int incrementLife){
        this.life += incrementLife;
        if (this.life > 5)
            this.life = 5;
    }

    public void decreaseLife(int decreaseLife){
        this.life -= decreaseLife;
        if (this.life < 0)
            this.life = 0;
    }

    public void increaseGold(int gold){
       this.inventory.addGold(gold);
    }

    public void decreaseGold(int gold){
        this.inventory.decreaseGold(gold);
    }

    public void increasePower(int power){
        this.power += power;
        if (this.power > 5)
            this.power = 5;
    }

    public void decreasePower(int power){
        this.power -= power;
        if (this.power < 1)
            this.power = 1;
    }

    public abstract int getSpecialAttack();
    public abstract int getSpecialDefense();

    public int checkGold(){
        return this.inventory.getGold();
    }

    public void addMinion(Minion minion){
        this.minions.add(minion);
    }

    public void addWeakness(Weakness weakness){
        this.weaknesses.add(weakness);
    }

    public void addStrength(Strength strength){
        this.strengths.add(strength);
    }

}

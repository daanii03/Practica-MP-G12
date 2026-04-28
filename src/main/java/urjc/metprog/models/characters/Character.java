package main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.equipment.Armor;
import main.java.urjc.metprog.models.equipment.Weapon;

import java.util.ArrayList;
import java.util.List;


public abstract class Character {

    // Attributes

    private String name;
    private List<Weapon> activeWeapons = new ArrayList<>();
    private Armor activeArmor;
    private int life;
    private int power;
    private int id;
    private SpecialSkill specialSkill;
    private Inventory inventory;
    private List<Minion> minions = new ArrayList<>();

    public Character(CharacterBuilder characterBuilder){

    }

    // Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setActiveArmor(Armor activeArmor) {
        this.activeArmor = activeArmor;
    }

    public void setActiveWeapons(List<Weapon> activeWeapons) {
        this.activeWeapons.addAll(activeWeapons);
    }

    public void setLife(int life){
        if(life >= 0 && life <= 5){
            this.life = life;
        }
        else{
            throw new IllegalArgumentException("La vida debe estar entre 0 y 5");
        }
    }

    public void setPower(int power){
        if(power >= 0 && power <= 5){
            this.power = power;
        }
        else {
            throw new IllegalArgumentException("El poder debe estar entre 0 y 5");
        }
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

    public List<Weapon> getActiveWeapons(){
        return this.activeWeapons;
    }
    public Armor getActiveArmor(){
        return this.activeArmor;
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

    // Methods

    public void increaseLife(int incrementLife){
        if(this.life + incrementLife <= 5){
            this.life += incrementLife;
        }
        else{
            throw new RuntimeException("La vida no puede tener un valor mayor a 5.");
        }
    }

    public void decreaseLife(int decreaseLife){
        if(this.life - decreaseLife >= 0){
            this.life -= decreaseLife;
        }
        else{
            throw new RuntimeException("La vida no puede tener un valor menor a 0");
        }
    }

    public void increaseGold(int gold){
       this.inventory.addGold(gold);
    }

    public void decreaseGold(int gold){
        this.inventory.decreaseGold(gold);
    }

    public void increasePower(int power){
        if (this.power + power <= 5){
            this.power += power;
        }
        else {
            throw new RuntimeException("El poder no puede tener un valor mayor a 5.");
        }
    }

    public void decreasePower(int power){
        if (this.power - power >= 0){
            this.power -= power;

        }
        else {
            throw new RuntimeException("El poder no puede tener un valor menor a 0.");
        }
    }

    public abstract void useSpecialSkill();

    public int checkGold(){
        return this.inventory.getGold();
    }

    public void addMinion(Minion minion){
        minions.add(minion);
    }

    public void selectSpecialSkill(){

    }
}

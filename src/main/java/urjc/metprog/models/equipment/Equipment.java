package main.java.urjc.metprog.models.equipment;

public abstract class Equipment {

    private String name;
    private int attackModifier;
    private int defenseModifier;

    public Equipment(String name, int attackModifier, int defenseModifier) {
        this.name = name;
        this.attackModifier = validateRange(attackModifier);
        this.defenseModifier = validateRange(defenseModifier);
    }

    private static int validateRange(int value){
        if (value < 0 || value > 3)
            throw new IllegalArgumentException("El rango debe ir entre 0 y 3");
        return value;
    }

    // Getters

    public String getName(){
        return this.name;
    }

    public int getAttackModifier(){
        return this.attackModifier;
    }

    public int getDefenseModifier(){
        return this.defenseModifier;
    }

    // Setters

    public void setName(String name){
        this.name = name;
    }

    public void setAttackModifier(int attackModifier){
        this.attackModifier = validateRange(attackModifier);
    }

    public void setDefenseModifier(int defenseModifier){
        this.defenseModifier = validateRange(defenseModifier);
    }

}

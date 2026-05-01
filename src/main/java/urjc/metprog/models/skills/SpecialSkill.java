package main.java.urjc.metprog.models.skills;

public class SpecialSkill {

    private String name;
    private int attackValue;
    private int defenseValue;

    public SpecialSkill(String name, int attackValue, int defenseValue) {
        this.name = name;
        this.attackValue = validateValue(attackValue);
        this.defenseValue = validateValue(defenseValue);
    }

    private static int validateValue(int value){
        if (value >= 1 && value <= 3){
            return value;
        }
        throw new IllegalArgumentException("El valor debe estar entre 1 y 3.");
    }

    // Getters

    public String getName() {
        return name;
    }

    public int getAttackValue() {
        return attackValue;
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    // Setters


    public void setName(String name) {
        this.name = name;
    }

    public void setAttackValue(int attackValue) {
        this.attackValue = validateValue(attackValue);
    }

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = validateValue(defenseValue);
    }
}

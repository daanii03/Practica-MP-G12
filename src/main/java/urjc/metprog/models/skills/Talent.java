package main.java.urjc.metprog.models.skills;

public class Talent extends SpecialSkill {

    private int willPowerCost;

    public Talent(String name, int willPowerCost, int attack, int defense){
        super(name,attack,defense);
        validateCostWill(willPowerCost);
        this.willPowerCost = willPowerCost;
    }

    // Getters

    public int getWillPowerCost(){
        return this.willPowerCost;
    }

    // Setters

    public void setWillPowerCost(int willPowerCost){
        validateCostWill(willPowerCost);
        this.willPowerCost = willPowerCost;
    }

    private void validateCostWill(int cost){
        if (cost < 0 || cost > 3){
            throw new IllegalArgumentException("El coste de voluntad debe estar entre 0 y 3");
        }
    }

}

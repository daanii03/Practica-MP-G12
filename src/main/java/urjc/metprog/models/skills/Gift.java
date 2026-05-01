package main.java.urjc.metprog.models.skills;

public class Gift extends SpecialSkill{

    private int rageCost;

    public Gift(String name,int rageCost,int attackValue, int defenseValue) {
        super(name, attackValue, defenseValue);
        this.rageCost = validateRageCost(rageCost);
    }

    private static int validateRageCost(int rageCost){
        if (rageCost < 0 || rageCost > 3){
            throw new IllegalArgumentException("La rabia debe estar entre 0 y 3");
        }
        return rageCost;
    }

    // Getters

    public int getRageCost(){
        return this.rageCost;
    }

    // Setters

    public void setRageCost(int rageCost){
        this.rageCost = validateRageCost(rageCost);
    }


}

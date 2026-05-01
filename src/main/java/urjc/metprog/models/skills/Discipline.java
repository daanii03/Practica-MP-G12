package main.java.urjc.metprog.models.skills;

public class Discipline extends SpecialSkill{

    private int bloodPointsCost;

    public Discipline(String name, int bloodPointsCost, int attack, int defense){
        super(name,attack,defense);
        this.bloodPointsCost = validateBloodPointsCost(bloodPointsCost);
    }

    private static int validateBloodPointsCost(int bloodPointsCost) {
        if (bloodPointsCost < 1 || bloodPointsCost > 3)
            throw new IllegalArgumentException("El coste de sangre debe estar entre 1 y 3.");
        return bloodPointsCost;
    }

    // Getters

    public int getBloodPointsCost() {
        return bloodPointsCost;
    }

    // Setters

    public void setBloodPointsCost(int bloodPointsCost){
        this.bloodPointsCost = validateBloodPointsCost(bloodPointsCost);
    }

}

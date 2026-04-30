package main.java.urjc.metprog.models.characters;

public class Discipline extends SpecialSkill{

    private int bloodPointsCost;

    public Discipline(String name, int bloodPointsCost, int attack, int defense){
        this.setName(name);
        this.validateBloodPointsCost(bloodPointsCost);
        this.bloodPointsCost = bloodPointsCost;
        this.setAttackValue(attack);
        this.setDefenseValue(defense);
    }

    private void validateBloodPointsCost(int bloodPointsCost) {
        if (bloodPointsCost < 0 || bloodPointsCost > 10)
            throw new IllegalArgumentException("El coste de sangre debe estar entre 0 y 10.");
    }

    // Getters

    public int getBloodPointsCost() {
        return bloodPointsCost;
    }

    // Setters

    public void setBloodPointsCost(int bloodPointsCost){
        if (this.bloodPointsCost <= 10 && this.bloodPointsCost >= 0)
            this.bloodPointsCost = bloodPointsCost;
    }

}

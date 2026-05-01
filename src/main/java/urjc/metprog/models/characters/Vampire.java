package main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.minions.HumanMinion;
import main.java.urjc.metprog.models.minions.Minion;
import main.java.urjc.metprog.models.skills.Discipline;

public class Vampire extends Character{
    private int age;
    private int bloodPoints;

    public Vampire(CharacterBuilder characterBuilder) {
        super(characterBuilder);
        this.bloodPoints = 10;
        this.age = 100;
    }

    private static int validateBloodPoints(int bloodPoints){
        if (bloodPoints < 0 || bloodPoints > 10)
            throw new IllegalArgumentException("Los puntos de sangre deben estar entre 0 y 10");
        return bloodPoints;
    }

    // Setters

    public void setAge(int age){
        this.age = age;
    }

    public void setBloodPoints(int bloodPoints){
        this.bloodPoints = validateBloodPoints(bloodPoints);
    }

    // Getters

    @Override
    public int getSpecialDefense() {
        Discipline discipline = (Discipline) this.getSpecialSkill();
        if (this.bloodPoints >= discipline.getBloodPointsCost())
            return discipline.getDefenseValue();
        return 0;
    }

    @Override
    public int getSpecialAttack() {
        Discipline discipline = (Discipline) this.getSpecialSkill();
        if (this.bloodPoints >= discipline.getBloodPointsCost()){
            this.bloodPoints -= discipline.getBloodPointsCost();
            return discipline.getAttackValue();
        }
        return 0;
    }

    @Override
    public void addMinion(Minion minion){
        if (minion instanceof HumanMinion)
            throw new IllegalArgumentException("Los vampiros no pueden tener esbirros humanos");
        super.addMinion(minion);
    }

    public int getBloodPoints(){
        return this.bloodPoints;
    }

    public int getAge(){
        return this.age;
    }
}

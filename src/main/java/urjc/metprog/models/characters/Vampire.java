package main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.skills.Discipline;

public class Vampire extends Character{

    private int bloodPoints;

    public Vampire(CharacterBuilder characterBuilder) {
        super(characterBuilder);
        this.bloodPoints = 10;
        int age = 100;
    }

    @Override
    public int getSpecialDefense() {
        Discipline discipline = (Discipline) this.getSpecialSkill();
        if (this.bloodPoints > discipline.getBloodPointsCost())
            return discipline.getDefenseValue();
        return 0;
    }

    @Override
    public int getSpecialAttack() {
        Discipline discipline = (Discipline) this.getSpecialSkill();
        if (this.bloodPoints > discipline.getBloodPointsCost()){
            this.bloodPoints -= discipline.getBloodPointsCost();
            return discipline.getAttackValue();
        }
        return 0;
    }
}

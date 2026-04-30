package main.java.urjc.metprog.models.characters;

import java.util.Random;

public class Vampire extends Character{

    public enum DisciplineCatalogue {
        MORDISCO_LETAL("Mordisco Letal",1,2,1),
        SOMBRAS_OCULTAS("Sombras ocultas",2,3,2),
        SANGRE_HERVIDA("Sangre hervida",3,4,1);

        private final String disciplineName;
        private final int disciplineBloodPoints;
        private final int attack;
        private final int defense;

        DisciplineCatalogue(String disciplineName, int disciplineBloodPoints, int attack, int defense){
            this.disciplineName = disciplineName;
            this.disciplineBloodPoints = disciplineBloodPoints;
            this.attack = attack;
            this.defense = defense;
        }

        public Discipline generateDiscipline(){
            return new Discipline(this.disciplineName,this.disciplineBloodPoints, this.attack, this.defense);
        }
    }

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

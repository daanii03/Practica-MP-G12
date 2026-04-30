package main.java.urjc.metprog.models.skills;

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
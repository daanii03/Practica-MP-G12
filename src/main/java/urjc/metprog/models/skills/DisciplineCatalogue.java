package main.java.urjc.metprog.models.skills;

/**
 * Catálogo predefinido de disciplinas disponibles para los vampiros.
 * <p>
 * Este enumerado centraliza las estadísticas de cada habilidad especial (disciplina),
 * facilitando la creación de objetos {@link Discipline} con valores equilibrados
 * y coherentes.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 */

public enum DisciplineCatalogue {

    /** Disciplina básica de ataque con bajo coste de sangre. */

    MORDISCO_LETAL("Mordisco Letal",1,2,1),

    /** Habilidad equilibrada que mejora significativamente la defensa. */

    SOMBRAS_OCULTAS("Sombras ocultas",2,3,2),

    /** Disciplina de alto poder ofensivo. */

    SANGRE_HERVIDA("Sangre hervida",3,3,1);

    private final String disciplineName;
    private final int disciplineBloodPoints;
    private final int attack;
    private final int defense;

    /**
     * Constructor interno del enumerado para definir las estadísticas de la disciplina.
     *
     * @param disciplineName Nombre legible de la disciplina.
     * @param disciplineBloodPoints Coste de puntos de sangre para su activación.
     * @param attack Valor de modificador de ataque.
     * @param defense Valor de modificador de defensa.
     */

    DisciplineCatalogue(String disciplineName, int disciplineBloodPoints, int attack, int defense){
        this.disciplineName = disciplineName;
        this.disciplineBloodPoints = disciplineBloodPoints;
        this.attack = attack;
        this.defense = defense;
    }

    /**
     * Crea una nueva instancia de {@link Discipline} basada en los valores
     * definidos en la constante del catálogo.
     *
     * @return Un objeto {@link Discipline} listo para ser asignado a un personaje.
     */

    public Discipline generateDiscipline(){
        return new Discipline(this.disciplineName,this.disciplineBloodPoints, this.attack, this.defense);
    }
}
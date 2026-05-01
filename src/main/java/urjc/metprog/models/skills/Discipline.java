package main.java.urjc.metprog.models.skills;

/**
 * Representa una Disciplina, la habilidad especial exclusiva de los vampiros.
 * <p>
 * Las disciplinas requieren el consumo de puntos de sangre para su activación.
 * Hereda de {@link SpecialSkill} los modificadores de combate y añade la gestión
 * del coste de recursos específico de la raza vampírica.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 * @see SpecialSkill
 */

public class Discipline extends SpecialSkill{

    /** Coste de puntos de sangre para activar la disciplina. Rango legal [1, 3]. */

    private int bloodPointsCost;

    /**
     * Construye una nueva Disciplina con sus estadísticas y coste asociados.
     *
     * @param name Nombre de la disciplina.
     * @param bloodPointsCost Coste de activación (debe estar entre 1 y 3).
     * @param attack Modificador de ataque (rango definido en la clase base).
     * @param defense Modificador de defensa (rango definido en la clase base).
     * @throws IllegalArgumentException Si el coste de sangre no cumple el rango [1, 3].
     */

    public Discipline(String name, int bloodPointsCost, int attack, int defense){
        super(name,attack,defense);
        this.bloodPointsCost = validateBloodPointsCost(bloodPointsCost);
    }

    /**
     * Valida que el coste de sangre se encuentre dentro de los límites permitidos.
     *
     * @param bloodPointsCost Valor a validar.
     * @return El valor validado.
     * @throws IllegalArgumentException Si el valor es menor que 1 o mayor que 3.
     */

    private static int validateBloodPointsCost(int bloodPointsCost) {
        if (bloodPointsCost < 1 || bloodPointsCost > 3)
            throw new IllegalArgumentException("El coste de sangre debe estar entre 1 y 3.");
        return bloodPointsCost;
    }

    // Getters

    /** @return El coste de puntos de sangre de esta disciplina. */

    public int getBloodPointsCost() {
        return bloodPointsCost;
    }

    // Setters

    /**
     * Actualiza el coste de sangre de la disciplina.
     *
     * @param bloodPointsCost Nuevo coste de activación.
     * @throws IllegalArgumentException Si el valor no está en el rango [1, 3].
     */

    public void setBloodPointsCost(int bloodPointsCost){
        this.bloodPointsCost = validateBloodPointsCost(bloodPointsCost);
    }

}

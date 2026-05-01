package main.java.urjc.metprog.models.minions;

/**
 * Representa un esbirro de tipo Humano en el sistema.
 * <p>
 * A diferencia de otros tipos de esbirros, los humanos se caracterizan por poseer
 * un nivel de lealtad ({@link Loyalty}), el cual puede influir en su comportamiento
 * o en las interacciones con el personaje que los lidera.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 * @see Minion
 */

public class HumanMinion extends Minion{

    /** El nivel o tipo de lealtad asociado a este esbirro humano. */

    private Loyalty loyalty;

    /**
     * Crea un nuevo esbirro humano con nombre, salud y lealtad.
     *
     * @param name Nombre del humano.
     * @param health Puntos de salud (rango [1, 3] heredado de {@link Minion}).
     * @param loyalty Tipo de lealtad asignada al esbirro.
     * @throws IllegalArgumentException Si la salud no cumple con los rangos de la clase base.
     */

    public HumanMinion(String name, int health, Loyalty loyalty) {
        super(name, health);
        this.loyalty = loyalty;
    }

    // Getters

    /**
     * Obtiene la lealtad actual del esbirro.
     * @return Objeto {@link Loyalty} que representa su fidelidad.
     */

    public Loyalty getLoyalty(){
        return this.loyalty;
    }

    // Setters

    /**
     * Actualiza la lealtad del esbirro humano.
     * @param loyalty Nueva lealtad a asignar.
     */

    public void setLoyalty(Loyalty loyalty){
        this.loyalty = loyalty;
    }
}

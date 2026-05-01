package main.java.urjc.metprog.models.skills;

/**
 * Representa un Don, la habilidad especial exclusiva de los licántropos.
 * <p>
 * Los dones requieren el consumo de puntos de rabia para su ejecución. Esta clase
 * extiende {@link SpecialSkill} incorporando la gestión del coste de rabia,
 * asegurando que se mantenga dentro del rango de intensidad permitido para
 * los hombres lobo.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 * @see SpecialSkill
 */

public class Gift extends SpecialSkill{

    /** Coste de puntos de rabia para activar el don. Rango legal [0, 3]. */

    private int rageCost;

    /**
     * Crea un nuevo Don con sus modificadores y coste de rabia.
     *
     * @param name Nombre identificativo del don.
     * @param rageCost Coste de rabia necesario (rango [0, 3]).
     * @param attackValue Valor de modificador de ataque.
     * @param defenseValue Valor de modificador de defensa.
     * @throws IllegalArgumentException Si el coste de rabia no está en el rango permitido.
     */

    public Gift(String name,int rageCost,int attackValue, int defenseValue) {
        super(name, attackValue, defenseValue);
        this.rageCost = validateRageCost(rageCost);
    }

    /**
     * Valida que el coste de rabia se encuentre en el rango legal [0, 3].
     *
     * @param rageCost Valor de rabia a validar.
     * @return El valor validado.
     * @throws IllegalArgumentException Si el valor es menor que 0 o mayor que 3.
     */

    private static int validateRageCost(int rageCost){
        if (rageCost < 0 || rageCost > 3){
            throw new IllegalArgumentException("La rabia debe estar entre 0 y 3");
        }
        return rageCost;
    }

    // Getters

    /** @return El coste de rabia actual del don. */

    public int getRageCost(){
        return this.rageCost;
    }

    // Setters

    /**
     * Actualiza el coste de rabia del don.
     *
     * @param rageCost Nuevo coste de activación.
     * @throws IllegalArgumentException Si el valor no cumple con el rango [0, 3].
     */

    public void setRageCost(int rageCost){
        this.rageCost = validateRageCost(rageCost);
    }
}

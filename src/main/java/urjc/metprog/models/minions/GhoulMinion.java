package main.java.urjc.metprog.models.minions;

/**
 * Representa un esbirro de tipo Ghoul (Gule) en el sistema.
 * <p>
 * Los Ghouls son esbirros vinculados a un vampiro que presentan un nivel de
 * dependencia específico. Este nivel de dependencia es un atributo crítico
 * que debe mantenerse dentro de un rango estrictamente definido.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 * @see Minion
 */

public class GhoulMinion extends Minion{

    /** Grado de dependencia del Ghoul. Rango legal [1, 5]. */

    private int dependence;

    /**
     * Crea un nuevo esbirro Ghoul con nombre, salud y nivel de dependencia.
     *
     * @param name Nombre del Ghoul.
     * @param health Puntos de salud (rango [1, 3] según {@link Minion}).
     * @param dependence Nivel de dependencia inicial (rango [1, 5]).
     * @throws IllegalArgumentException Si la salud o la dependencia están fuera de sus rangos.
     */

    public GhoulMinion(String name, int health, int dependence) {
        super(name, health);
        this.dependence = validateDependence(dependence);
    }

    /**
     * Valida que el nivel de dependencia se encuentre en el rango legal [1, 5].
     *
     * @param dependence Valor de dependencia a validar.
     * @return El valor validado.
     * @throws IllegalArgumentException Si el valor es menor que 1 o mayor que 5.
     */

    private static int validateDependence(int dependence){
        if (dependence < 1 || dependence > 5)
            throw new IllegalArgumentException("La dependencia debe estar entre 1 y 5");
        return dependence;
    }

    // Getters

    /** @return El nivel de dependencia actual del Ghoul. */

    public int getDependence(){
        return this.dependence;
    }

    // Setters

    /**
     * Actualiza el nivel de dependencia del Ghoul.
     *
     * @param dependence Nuevo valor de dependencia.
     * @throws IllegalArgumentException Si el valor no está en el rango [1, 5].
     */

    public void setDependence(int dependence){
        this.dependence = validateDependence(dependence);
    }

}
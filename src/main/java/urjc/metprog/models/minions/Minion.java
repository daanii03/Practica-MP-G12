package main.java.urjc.metprog.models.minions;

/**
 * Clase abstracta que define la estructura y comportamiento base de un esbirro.
 * <p>
 * Los esbirros son entidades que apoyan a los personajes principales en combate.
 * Esta clase centraliza la gestión del nombre y los puntos de salud, asegurando
 * que estos últimos se mantengan dentro del rango permitido para los súbditos.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 */

public abstract class Minion {

    /** Nombre identificativo del esbirro. */

    private String name;

    /** Puntos de salud del esbirro. Rango legal [1, 3]. */

    private int health;

    /**
     * Constructor para inicializar un esbirro con su nombre y salud.
     *
     * @param name Nombre del esbirro.
     * @param health Puntos de salud iniciales.
     * @throws IllegalArgumentException Si la salud no está en el rango [1, 3].
     */

    public Minion(String name, int health){
        this.name = name;
        this.health = validateHealth(health);
    }

    /**
     * Valida que la salud del esbirro cumpla con las restricciones del sistema.
     *
     * @param health Valor de salud a validar.
     * @return El valor de salud si es correcto.
     * @throws IllegalArgumentException Si el valor es menor que 1 o mayor que 3.
     */

    private static int validateHealth(int health) {
        if (health < 1 || health > 3)
            throw new IllegalArgumentException("La vida debe estar entre 1 y 3");
        return health;
    }

    // Getters

    /** @return El nombre del esbirro. */

    public String getName(){
        return this.name;
    }

    /** @return Los puntos de salud actuales del esbirro. */

    public int getHealth(){
        return this.health;
    }

    // Setters

    /** @param name Nuevo nombre para el esbirro. */

    public void setName(String name){
        this.name = name;
    }

    /**
     * @param health Nuevo valor de salud.
     * @throws IllegalArgumentException Si el valor no está entre 1 y 3.
     */

    public void setHealth(int health) {
        this.health = validateHealth(health);
    }
}

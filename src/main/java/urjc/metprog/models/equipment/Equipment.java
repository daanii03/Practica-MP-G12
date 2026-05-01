package main.java.urjc.metprog.models.equipment;

/**
 * Clase abstracta que define la estructura base para el equipamiento del personaje.
 * <p>
 * Gestiona los modificadores de ataque y defensa comunes a armas y armaduras,
 * asegurando que los valores se mantengan dentro del rango legal permitido.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 */

public abstract class Equipment {

    private String name;
    private int attackModifier;
    private int defenseModifier;

    /**
     * Constructor para inicializar el equipo con nombre y sus modificadores.
     *
     * @param name Nombre del equipo.
     * @param attackModifier Valor de ataque (rango [0, 3]).
     * @param defenseModifier Valor de defensa (rango [0, 3]).
     * @throws IllegalArgumentException Si algún modificador está fuera del rango permitido.
     */

    public Equipment(String name, int attackModifier, int defenseModifier) {
        this.name = name;
        this.attackModifier = validateRange(attackModifier);
        this.defenseModifier = validateRange(defenseModifier);
    }

    /**
     * Valida que el modificador esté dentro del rango legal [0, 3].
     *
     * @param value El valor a validar.
     * @return El valor validado.
     * @throws IllegalArgumentException Si el valor es menor que 0 o mayor que 3.
     */

    private static int validateRange(int value){
        if (value < 0 || value > 3)
            throw new IllegalArgumentException("El rango debe ir entre 0 y 3");
        return value;
    }

    // Getters

    /** @return El nombre del equipo. */

    public String getName(){
        return this.name;
    }

    /** @return El modificador de ataque actual. */

    public int getAttackModifier(){
        return this.attackModifier;
    }

    /** @return El modificador de defensa actual. */

    public int getDefenseModifier(){
        return this.defenseModifier;
    }

    // Setters

    /** @param name Nuevo nombre para el equipo. */

    public void setName(String name){
        this.name = name;
    }

    /**
     * @param attackModifier Nuevo modificador de ataque.
     * @throws IllegalArgumentException Si el valor no está en el rango [0, 3].
     */

    public void setAttackModifier(int attackModifier){
        this.attackModifier = validateRange(attackModifier);
    }

    /**
     * @param defenseModifier Nuevo modificador de defensa.
     * @throws IllegalArgumentException Si el valor no está en el rango [0, 3].
     */

    public void setDefenseModifier(int defenseModifier){
        this.defenseModifier = validateRange(defenseModifier);
    }

}

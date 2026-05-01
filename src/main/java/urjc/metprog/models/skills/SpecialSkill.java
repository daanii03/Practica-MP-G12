package main.java.urjc.metprog.models.skills;

/**
 * Clase abstracta que define la estructura base para cualquier habilidad especial.
 * <p>
 * Una habilidad especial proporciona bonificadores tanto de ataque como de defensa
 * que se aplican durante el combate. Esta clase centraliza la validación de
 * dichos valores para garantizar la consistencia del sistema de juego.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 */

public abstract class SpecialSkill {

    /** Nombre identificativo de la habilidad. */

    private String name;

    /** Valor del bonificador de ataque. Rango legal [1, 3]. */

    private int attackValue;

    /** Valor del bonificador de defensa. Rango legal [1, 3]. */

    private int defenseValue;

    /**
     * Constructor para inicializar una habilidad especial con sus valores base.
     *
     * @param name Nombre de la habilidad.
     * @param attackValue Valor de ataque inicial (debe estar entre 1 y 3).
     * @param defenseValue Valor de defensa inicial (debe estar entre 1 y 3).
     * @throws IllegalArgumentException Si alguno de los valores está fuera del rango [1, 3].
     */

    public SpecialSkill(String name, int attackValue, int defenseValue) {
        this.name = name;
        this.attackValue = validateValue(attackValue);
        this.defenseValue = validateValue(defenseValue);
    }

    /**
     * Valida que el valor del bonificador se encuentre en el rango legal permitido.
     *
     * @param value Valor a validar.
     * @return El valor validado si es correcto.
     * @throws IllegalArgumentException Si el valor es menor que 1 o mayor que 3.
     */

    private static int validateValue(int value){
        if (value >= 1 && value <= 3){
            return value;
        }
        throw new IllegalArgumentException("El valor debe estar entre 1 y 3.");
    }

    // Getters

    /** @return El nombre de la habilidad. */

    public String getName() {
        return name;
    }

    /** @return El valor del bonificador de ataque. */

    public int getAttackValue() {
        return attackValue;
    }

    /** @return El valor del bonificador de defensa. */

    public int getDefenseValue() {
        return defenseValue;
    }

    // Setters

    /** @param name Nuevo nombre para la habilidad. */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Actualiza el valor de ataque de la habilidad.
     *
     * @param attackValue Nuevo valor de ataque.
     * @throws IllegalArgumentException Si el valor no está en el rango [1, 3].
     */

    public void setAttackValue(int attackValue) {
        this.attackValue = validateValue(attackValue);
    }

    /**
     * Actualiza el valor de defensa de la habilidad.
     *
     * @param defenseValue Nuevo valor de defensa.
     * @throws IllegalArgumentException Si el valor no está en el rango [1, 3].
     */

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = validateValue(defenseValue);
    }
}

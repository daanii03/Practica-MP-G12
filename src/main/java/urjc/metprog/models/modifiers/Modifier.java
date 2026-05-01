package main.java.urjc.metprog.models.modifiers;

/**
 * Clase abstracta que define la estructura base para los modificadores del personaje.
 * <p>
 * Los modificadores representan rasgos intrínsecos que alteran las capacidades del
 * personaje, como sus fortalezas o debilidades. Esta clase asegura que el valor
 * de la intensidad del modificador se mantenga dentro del rango permitido.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 */

public abstract class Modifier {

    /** Nombre descriptivo del modificador. */

    private String name;

    /** Valor de intensidad del modificador. Rango legal [1, 5]. */

    private int value;

    /**
     * Constructor para inicializar un modificador con un nombre y un valor de intensidad.
     *
     * @param name Nombre del modificador.
     * @param value Valor de intensidad (debe estar entre 1 y 5).
     * @throws IllegalArgumentException Si el valor no cumple con el rango [1, 5].
     */

    public Modifier(String name, int value){
        this.name = name;
        this.value = validateValue(value);
    }

    /**
     * Valida que el valor del modificador se encuentre en el rango permitido.
     *
     * @param value El valor a validar.
     * @return El valor validado si es correcto.
     * @throws IllegalArgumentException Si el valor es menor que 1 o mayor que 5.
     */

    private static int validateValue(int value){
        if (value < 1 || value > 5)
            throw new IllegalArgumentException("El valor debe estar entre 1 y 5");
        return value;
    }

    // Setters

    /** @param name Nuevo nombre descriptivo del modificador. */

    public void setName(String name){
        this.name = name;
    }

    /**
     * Actualiza el valor de intensidad del modificador.
     *
     * @param value Nuevo valor de intensidad.
     * @throws IllegalArgumentException Si el valor no está en el rango [1, 5].
     */

    public void setValue(int value){
        this.value = validateValue(value);
    }

    // Getters

    /** @return El nombre del modificador. */

    public String getName(){
        return this.name;
    }

    /** @return El valor de intensidad actual. */

    public int getValue(){
        return this.value;
    }

}

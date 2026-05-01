package main.java.urjc.metprog.models.modifiers;

/**
 * Representa una Debilidad o rasgo desfavorable de un personaje.
 * <p>
 * Las debilidades son modificadores negativos que penalizan o limitan las
 * capacidades del personaje en determinadas condiciones. Al igual que las
 * fortalezas, su intensidad está regulada por la clase {@link Modifier}.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 * @see Modifier
 */

public class Weakness extends Modifier{

    /**
     * Crea una nueva debilidad con un nombre y un valor de intensidad.
     *
     * @param name Nombre descriptivo de la debilidad (ej. "Sensibilidad a la luz").
     * @param value Intensidad de la debilidad (rango [1, 5]).
     * @throws IllegalArgumentException Si el valor no cumple con el rango legal de la clase base.
     */

    public Weakness(String name, int value) {
        super(name, value);
    }
}

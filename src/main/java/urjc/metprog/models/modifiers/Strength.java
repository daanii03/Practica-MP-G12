package main.java.urjc.metprog.models.modifiers;

/**
 * Representa una Fortaleza o ventaja intrínseca de un personaje.
 * <p>
 * Las fortalezas son modificadores positivos que mejoran el desempeño del
 * personaje en situaciones específicas. Hereda la validación de rango [1, 5]
 * de la clase {@link Modifier}.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 * @see Modifier
 */

public class Strength extends Modifier {

    /**
     * Crea una nueva fortaleza con un nombre y un valor de intensidad.
     *
     * @param name Nombre descriptivo de la fortaleza (ej. "Reflejos felinos").
     * @param value Intensidad de la fortaleza (rango [1, 5]).
     * @throws IllegalArgumentException Si el valor excede los límites definidos en la clase base.
     */

    public Strength(String name, int value) {
        super(name, value);
    }
}

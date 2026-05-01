package main.java.urjc.metprog.models.minions;

/**
 * Define los niveles de lealtad posibles para un {@link HumanMinion}.
 * <p>
 * La lealtad determina el grado de fidelidad del esbirro humano hacia su señor
 * y puede influir en eventos de traición o bonificaciones durante el juego.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 */

public enum Loyalty {

    /** Indica una fidelidad absoluta. */

    ALTA,

    /** Indica una lealtad estándar. */

    NORMAL,

    /** Indica una lealtad precaria. */

    BAJA
}

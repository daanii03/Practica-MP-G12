package main.java.urjc.metprog.models.equipment;

/**
 * Representa una pieza de armadura que un personaje puede equipar para su protección.
 * <p>
 * Hereda de {@link Equipment} y se utiliza para agrupar objetos cuya función principal
 * es la defensa, aunque pueden incluir modificadores de ataque.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 * @see Equipment
 */

public class Armor extends Equipment{
 /**
  * Crea una nueva armadura con los parámetros especificados.
  *
  * @param name Nombre identificativo de la armadura.
  * @param attackModifier Valor de ataque (0 a 3).
  * @param defenseModifier Valor de defensa (0 a 3).
  * @throws IllegalArgumentException Si los modificadores exceden el rango permitido.
  */
    public Armor(String name, int attackModifier, int defenseModifier) {
        super(name, attackModifier, defenseModifier);
    }
}

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Es exactamente el mismo objeto en memoria
        if (obj == null || getClass() != obj.getClass()) return false; // No son del mismo tipo

        Armor armor = (Armor) obj;

        // Comparamos que tengan el mismo nombre y stats
        // Asumo que tienes los métodos getName(), getAttackModifier() y getDefenseModifier() heredados de Equipment
        return this.getAttackModifier() == armor.getAttackModifier() &&
                this.getDefenseModifier() == armor.getDefenseModifier() &&
                this.getName().equals(armor.getName());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(getName(), getAttackModifier(), getDefenseModifier());
    }
}

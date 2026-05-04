package main.java.urjc.metprog.models.equipment;

/**
 * Representa un arma que un personaje puede equipar para combatir.
 * <p>
 * A diferencia de otros equipos, las armas poseen una propiedad que define si
 * requieren una o dos manos para ser utilizadas, lo cual condiciona la
 * cantidad de armas que un personaje puede llevar activas simultáneamente.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 * @see Equipment
 */

public class Weapon extends Equipment {

    /** Indica si el arma es de una sola mano ({@code true}) o de dos manos ({@code false}). */

    private boolean isOneHandWeapon;

    /**
     * Crea un arma con sus modificadores y define su tipo de agarre.
     *
     * @param name Nombre identificativo del arma.
     * @param attackModifier Valor de ataque (rango [0, 3]).
     * @param defenseModifier Valor de defensa (rango [0, 3]).
     * @param isOneHandWeapon {@code true} si es de una mano, {@code false} si es de dos manos.
     * @throws IllegalArgumentException Si los modificadores exceden el rango permitido en {@link Equipment}.
     */

    public Weapon(String name, int attackModifier, int defenseModifier, boolean isOneHandWeapon){
        super(name,attackModifier,defenseModifier);
        this.isOneHandWeapon = isOneHandWeapon;
    }

    // Getters

    /**
     * Consulta si el arma se puede usar con una sola mano.
     *
     * @return {@code true} si es de una mano; {@code false} si requiere ambas manos.
     */

    public boolean isOneHandWeapon() {
        return isOneHandWeapon;
    }

    // Setters

    /**
     * Cambia el tipo de agarre del arma.
     *
     * @param oneHandWeapon {@code true} para una mano, {@code false} para dos manos.
     */

    public void setOneHandWeapon(boolean oneHandWeapon) {
        isOneHandWeapon = oneHandWeapon;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Es exactamente el mismo objeto en memoria
        if (obj == null || getClass() != obj.getClass()) return false; // No son del mismo tipo

        Weapon weapon = (Weapon) obj;

        // Comparamos que tengan el mismo nombre, stats y tipo de agarre
        // Asumo que tienes los métodos getName(), getAttackModifier() y getDefenseModifier() heredados de Equipment
        return this.isOneHandWeapon == weapon.isOneHandWeapon &&
                this.getAttackModifier() == weapon.getAttackModifier() &&
                this.getDefenseModifier() == weapon.getDefenseModifier() &&
                this.getName().equals(weapon.getName());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(getName(), getAttackModifier(), getDefenseModifier(), isOneHandWeapon);
    }
}

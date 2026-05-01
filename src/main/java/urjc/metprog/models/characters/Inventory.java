package main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.equipment.Armor;
import main.java.urjc.metprog.models.equipment.Weapon;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona el equipamiento y recursos económicos de un personaje.
 * <p>
 * El inventario controla el almacenamiento de armas y armaduras, así como
 * el equipo que el personaje tiene activo en un momento dado. Además,
 * implementa las reglas de validación para el uso de armas (número de manos).
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 */

public class Inventory {
    private int gold;
    private Armor activeArmor;
    private List<Weapon> activeWeapons = new ArrayList<>();
    private List<Weapon> weapons = new ArrayList<>();
    private List<Armor> armors = new ArrayList<>();

    /**
     * Valida que la cantidad de oro no sea negativa.
     *
     * @param gold Cantidad de oro a validar.
     * @return La cantidad de oro si es válida.
     * @throws IllegalArgumentException Si el oro es menor que 0.
     */

    private static int validateGold(int gold){
        if (gold < 0)
            throw new IllegalArgumentException("El oro no puede ser negativo");
        return gold;
    }

    // Getters

    /** @return Cantidad total de oro disponible. */

    public int getGold() {
        return this.gold;
    }

    /** @return La armadura que el personaje lleva equipada actualmente. */

    public Armor getActiveArmor() {
        return activeArmor;
    }

    /** @return Lista de armas (máximo 2) que el personaje está usando. */

    public List<Weapon> getActiveWeapons() {
        return activeWeapons;
    }

    /** @return Lista completa de armas en posesión del personaje. */

    public List<Weapon> getWeapons(){
        return this.weapons;
    }

    /** @return Lista completa de armaduras en posesión del personaje. */

    public List<Armor> getArmors(){
        return this.armors;
    }

    // Setters

    /**
     * @param gold Cantidad de oro inicial o total.
     * @throws IllegalArgumentException Si el valor es negativo.
     */

    public void setGold(int gold){
        this.gold = validateGold(gold);
    }

    /**
     * Establece la armadura activa del personaje.
     *
     * @param activeArmor La armadura a equipar.
     * @throws IllegalArgumentException Si la armadura no está en la lista de armaduras del inventario.
     */

    public void setActiveArmor(Armor activeArmor) {
        if (!this.armors.contains(activeArmor))
            throw new IllegalArgumentException("La armadura no pertenece al inventario");
        this.activeArmor = activeArmor;
    }

    /**
     * Configura la lista de armas activas aplicando las restricciones de combate.
     * <p>
     * Se validan las siguientes reglas:
     * <ul>
     *   <li>La lista no puede ser nula ni tener más de 2 armas.</li>
     *   <li>Todas las armas deben pertenecer previamente al inventario.</li>
     *   <li>Si se equipa un arma de dos manos, no se puede equipar ninguna otra.</li>
     * </ul>
     * </p>
     *
     * @param activeWeapons Lista de armas a equipar.
     * @throws IllegalArgumentException Si se incumple alguna de las reglas de equipamiento.
     */

    public void setActiveWeapons(List<Weapon> activeWeapons) {
        if (activeWeapons == null)
            throw new IllegalArgumentException("La lista de armas no puede ser nula");

        if (activeWeapons.size() > 2)
            throw new IllegalArgumentException("La lista de armas no puede tener más de 2 armas");

        for (Weapon weapon : activeWeapons){
            if (!this.weapons.contains(weapon))
                throw new IllegalArgumentException("Algún arma no pertenece al inventario");
        }

        for (Weapon weapon : activeWeapons){
            if (!weapon.isOneHandWeapon() && activeWeapons.size() > 1)
                throw new IllegalArgumentException("Con un arma de dos no puede haber otra arma");
        }

        this.activeWeapons.clear();
        this.activeWeapons.addAll(activeWeapons);

    }

    // Methods

    /**
     * Añade una cantidad positiva de oro al inventario.
     * @param gold Cantidad de oro a sumar.
     */

    public void addGold(int gold){
        this.gold += validateGold(gold);
    }

    /**
     * Resta una cantidad de oro. Si el resultado fuera negativo, se queda en 0.
     * @param gold Cantidad de oro a restar.
     */

    public void decreaseGold(int gold){
        this.gold -= validateGold(gold);

        if (this.gold < 0)
            this.gold = 0;
    }

    /**
     * Añade una armadura a la colección del inventario.
     * @param armor Armadura a guardar.
     */

    public void addArmor(Armor armor){
        this.armors.add(armor);
    }

    /**
     * Añade un arma a la colección del inventario.
     * @param weapon Arma a guardar.
     */

    public void addWeapon(Weapon weapon){
        this.weapons.add(weapon);
    }
}

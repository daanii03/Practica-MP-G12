package main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.equipment.Armor;
import main.java.urjc.metprog.models.equipment.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private int gold;
    private Armor activeArmor;
    private List<Weapon> activeWeapons = new ArrayList<>();
    private List<Weapon> weapons = new ArrayList<>();
    private List<Armor> armors = new ArrayList<>();


    private static int validateGold(int gold){
        if (gold < 0)
            throw new IllegalArgumentException("El oro no puede ser negativo");
        return gold;
    }

    // Getters
    public int getGold() {
        return this.gold;
    }

    public Armor getActiveArmor() {
        return activeArmor;
    }

    public List<Weapon> getActiveWeapons() {
        return activeWeapons;
    }

    public List<Weapon> getWeapons(){
        return this.weapons;
    }

    public List<Armor> getArmors(){
        return this.armors;
    }

    // Setters
    public void setGold(int gold){
        this.gold = validateGold(gold);
    }

    public void setActiveArmor(Armor activeArmor) {
        if (!this.armors.contains(activeArmor))
            throw new IllegalArgumentException("La armadura no pertenece al inventario");
        this.activeArmor = activeArmor;
    }

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

    public void addGold(int gold){
        this.gold += validateGold(gold);
    }

    public void decreaseGold(int gold){
        this.gold -= validateGold(gold);

        if (this.gold < 0)
            this.gold = 0;
    }

    public void addArmor(Armor armor){
        this.armors.add(armor);
    }

    public void addWeapon(Weapon weapon){
        this.weapons.add(weapon);
    }

}

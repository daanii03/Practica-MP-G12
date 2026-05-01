package main.java.urjc.metprog.models.equipment;

public class Weapon extends Equipment {

    private boolean isOneHandWeapon;

    public Weapon(String name, int attackModifier, int defenseModifier, boolean isOneHandWeapon){
        super(name,attackModifier,defenseModifier);
        this.isOneHandWeapon = isOneHandWeapon;
    }

    // Getters

    public boolean isOneHandWeapon() {
        return isOneHandWeapon;
    }

    // Setters

    public void setOneHandWeapon(boolean oneHandWeapon) {
        isOneHandWeapon = oneHandWeapon;
    }

}

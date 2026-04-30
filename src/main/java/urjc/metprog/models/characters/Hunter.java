package main.java.urjc.metprog.models.characters;

public class Hunter extends Character {

    private int willPowerCost;

    public Hunter(CharacterBuilder characterBuilder) {
        super(characterBuilder);
        this.willPowerCost = 3;
    }

    // Getters

    @Override
    public int getSpecialAttack() {
        return 0;
    }

    @Override
    public int getSpecialDefense() {
        return 0;
    }

    // Setters

    public void setWillPowerCost(int willPowerCost){

    }

}

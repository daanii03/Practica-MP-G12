package main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.skills.Gift;

public class Werewolf extends Character {

    private int rage;

    public Werewolf(CharacterBuilder characterBuilder) {
        super(characterBuilder);
        this.rage = 0;
    }

    private static int validateRage(int cost){
        if (cost < 0 || cost > 3){
            throw new IllegalArgumentException("La rabia debe estar entre 0 y 3");
        }
        return cost;
    }


    // Getters

    public int getRage(){
        return this.rage;
    }

    @Override
    public int getSpecialAttack() {
        Gift gift = (Gift) this.getSpecialSkill();
        return this.rage >= gift.getRageCost() ? gift.getAttackValue() : 0;
    }

    @Override
    public int getSpecialDefense() {
        Gift gift = (Gift) this.getSpecialSkill();
        return this.rage >= gift.getRageCost() ? gift.getDefenseValue() : 0;
    }

    // Setters

    private void setRage(int rage){
        this.rage = validateRage(rage);
    }

    // Methods

    public void increaseRage(){
        if (rage < 3)
            this.rage++;
    }

    public void resetRage(){
        this.rage = 0;
    }


}

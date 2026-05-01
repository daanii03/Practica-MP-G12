package main.java.urjc.metprog.models.characters;

public class Hunter extends Character {

    private int will;

    public Hunter(CharacterBuilder characterBuilder) {
        super(characterBuilder);
        this.will = 3;
    }

    private static int validateWill(int will){
        if (will < 0 || will > 3)
            throw new IllegalArgumentException("El don debe estar entre 0 y 3");
        return will;
    }


    // Getters

    public int getWill(){
        return this.will;
    }

    @Override
    public int getSpecialAttack() {
        return this.getSpecialSkill().getAttackValue();
    }

    @Override
    public int getSpecialDefense() {
        return this.getSpecialSkill().getDefenseValue();
    }

    // Setters

    public void setWill(int will){
        this.will = validateWill(will);
    }

    // Methods

    public void decreaseWill(){
        if (will > 0)
            this.will--;
    }

    public void increaseWill(){
        if (will < 3)
            this.will++;
    }

    public void resetWill(){
        this.will = 3;
    }

}

package main.java.urjc.metprog.models.characters;

public class Hunter extends Character {
    public Hunter(CharacterBuilder characterBuilder) {
        super(characterBuilder);
    }

    @Override
    public int getSpecialAttack() {
        return 0;
    }

    @Override
    public int getSpecialDefense() {
        return 0;
    }
}

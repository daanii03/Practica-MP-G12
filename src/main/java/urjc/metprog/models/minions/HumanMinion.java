package main.java.urjc.metprog.models.minions;

public class HumanMinion extends Minion{

    private Loyalty loyalty;

    public HumanMinion(String name, int health, Loyalty loyalty) {
        super(name, health);
        this.loyalty = loyalty;
    }

    // Getters

    public Loyalty getLoyalty(){
        return this.loyalty;
    }

    // Setters

    public void setLoyalty(Loyalty loyalty){
        this.loyalty = loyalty;
    }

}

package main.java.urjc.metprog.models.minions;

public class GhoulMinion extends Minion{

    private int dependence;


    public GhoulMinion(String name, int health, int dependence) {
        super(name, health);
        this.dependence = validateDependence(dependence);
    }

    private static int validateDependence(int dependence){
        if (dependence < 1 || dependence > 5)
            throw new IllegalArgumentException("La dependencia debe estar entre 1 y 5");
        return dependence;
    }

    // Getters

    public int getDependence(){
        return this.dependence;
    }

    // Setters

    public void setDependence(int dependence){
        this.dependence = validateDependence(dependence);
    }

}
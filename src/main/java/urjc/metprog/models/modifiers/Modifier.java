package main.java.urjc.metprog.models.modifiers;

public abstract class Modifier {

    private String name;
    private int value;

    public Modifier(String name, int value){
        this.name = name;
        this.value = validateValue(value);
    }

    private static int validateValue(int value){
        if (value < 1 || value > 5)
            throw new IllegalArgumentException("El valor debe estar entre 1 y 5");
        return value;
    }

    // Setters

    public void setName(String name){
        this.name = name;
    }

    public void setValue(int value){
        this.value = validateValue(value);
    }

    // Getters

    public String getName(){
        return this.name;
    }

    public int getValue(){
        return this.value;
    }

}

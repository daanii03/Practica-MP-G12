package main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.minions.Minion;
import main.java.urjc.metprog.models.modifiers.Strength;
import main.java.urjc.metprog.models.modifiers.Weakness;
import main.java.urjc.metprog.models.skills.SpecialSkill;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta que define la estructura y comportamiento base de un personaje.
 * <p>
 * Gestiona atributos fundamentales como vida, poder, inventario y colecciones
 * de esbirros, debilidades y fortalezas. Incluye validaciones para asegurar
 * que los valores de vida y poder se mantengan dentro de los rangos permitidos.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 */

public abstract class Character {

    // Attributes

    private String name;
    private int life;
    private int power;
    private int id;
    private SpecialSkill specialSkill;
    private Inventory inventory;
    private final List<Minion> minions = new ArrayList<>();
    private final List<Weakness> weaknesses = new ArrayList<>();
    private final List<Strength> strengths = new ArrayList<>();

    /**
     * Construye un personaje a partir de un objeto {@link CharacterBuilder}.
     *
     * @param characterBuilder El builder que contiene la configuración inicial del personaje.
     */

    public Character(CharacterBuilder characterBuilder){
        this.name = characterBuilder.getName();
        this.id = characterBuilder.getId();
        this.life = validateLife(characterBuilder.getLife());
        this.power = validatePower(characterBuilder.getPower());
        this.specialSkill = characterBuilder.getSpecialSkill();
        this.inventory = characterBuilder.getInventory();
        this.weaknesses.addAll(characterBuilder.getWeaknesses());
        this.strengths.addAll(characterBuilder.getStrengths());
        this.minions.addAll(characterBuilder.getMinions());
    }

    /**
     * Valida que el valor de vida esté en el rango permitido [0, 5].
     *
     * @param life El valor de vida a validar.
     * @return El valor de vida si es válido.
     * @throws IllegalArgumentException Si la vida es menor que 0 o mayor que 5.
     */

    private static int validateLife(int life){
        if (life < 0 || life > 5)
            throw new IllegalArgumentException("La vida debe estar entre 0 y 5");
        return life;
    }

    /**
     * Valida que el valor de poder esté en el rango permitido [1, 5].
     *
     * @param power El valor de poder a validar.
     * @return El valor de poder si es válido.
     * @throws IllegalArgumentException Si el poder es menor que 1 o mayor que 5.
     */

    private static int validatePower(int power){
        if (power < 1 || power > 5)
            throw new IllegalArgumentException("El poder debe estar entre 1 y 5");
        return power;
    }

    // Setters

    /** @param name El nuevo nombre del personaje. */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param life Nuevo valor de vida.
     * @throws IllegalArgumentException Si el valor no está entre 0 y 5.
     */

    public void setLife(int life){
        this.life = validateLife(life);
    }

    /**
     * @param power Nuevo valor de poder.
     * @throws IllegalArgumentException Si el valor no está entre 1 y 5.
     */

    public void setPower(int power){
        this.power = validatePower(power);
    }

    /**
     * Reemplaza la lista actual de esbirros por una nueva.
     *
     * @param minions Nueva lista de esbirros. Si es {@code null}, la lista se vacía.
     */

    public void setMinions(List<Minion> minions){
        this.minions.clear();
        if (minions != null)
            this.minions.addAll(minions);
    }


    /** @param id El nuevo identificador único del personaje. */

    public void setId(int id) {
        this.id = id;
    }

    /** @param inventory El nuevo objeto inventario del personaje. */

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /** @param specialSkill La nueva habilidad especial asignada. */

    public void setSpecialSkill(SpecialSkill specialSkill) {
        this.specialSkill = specialSkill;
    }


    // Getters

    public String getName(){
        return this.name;
    }
    
    public int getLife(){
        return this.life;
    }

    public int getPower(){
        return this.power;
    }

    public int getId(){
        return this.id;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public SpecialSkill getSpecialSkill() {
        return specialSkill;
    }

    public List<Minion> getMinions(){ return this.minions; }

    public List<Weakness> getWeaknesses(){
        return this.weaknesses;
    }

    public List<Strength> getStrengths(){
        return this.strengths;
    }

    // Methods

    /**
     * Aumenta la vida del personaje sin superar el límite máximo de 5.
     * @param incrementLife Puntos de vida a sumar.
     */

    public void increaseLife(int incrementLife){
        this.life += incrementLife;
        if (this.life > 5)
            this.life = 5;
    }

    /**
     * Disminuye la vida del personaje sin bajar del límite mínimo de 0.
     * @param decreaseLife Puntos de vida a restar.
     */

    public void decreaseLife(int decreaseLife){
        this.life -= decreaseLife;
        if (this.life < 0)
            this.life = 0;
    }

    /** @param gold Cantidad de oro a añadir al inventario. */

    public void increaseGold(int gold){
       this.inventory.addGold(gold);
    }

    /** @param gold Cantidad de oro a retirar del inventario. */

    public void decreaseGold(int gold){
        this.inventory.decreaseGold(gold);
    }

    /**
     * Incrementa el poder del personaje sin superar el límite de 5.
     * @param power Puntos de poder a sumar.
     */

    public void increasePower(int power){
        this.power += power;
        if (this.power > 5)
            this.power = 5;
    }

    /**
     * Disminuye el poder del personaje sin bajar del límite mínimo de 1.
     * @param power Puntos de poder a restar.
     */

    public void decreasePower(int power){
        this.power -= power;
        if (this.power < 1)
            this.power = 1;
    }

    /**
     * Calcula el valor de ataque especial del personaje.
     * @return Puntos de ataque especial.
     */

    public abstract int getSpecialAttack();

    /**
     * Calcula el valor de defensa especial del personaje.
     * @return Puntos de defensa especial.
     */

    public abstract int getSpecialDefense();

    /**
     * Consulta la cantidad total de oro en el inventario.
     * @return Cantidad de oro actual.
     */

    public int checkGold(){
        return this.inventory.getGold();
    }

    /** @param minion Esbirro a añadir a la colección. */

    public void addMinion(Minion minion){
        this.minions.add(minion);
    }

    /** @param weakness Debilidad a añadir a la colección. */

    public void addWeakness(Weakness weakness){
        this.weaknesses.add(weakness);
    }

    /** @param strength Fortaleza a añadir a la colección. */

    public void addStrength(Strength strength){
        this.strengths.add(strength);
    }
}

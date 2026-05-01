package main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.minions.Minion;
import main.java.urjc.metprog.models.modifiers.Strength;
import main.java.urjc.metprog.models.modifiers.Weakness;
import main.java.urjc.metprog.models.skills.SpecialSkill;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase constructora siguiendo el patrón de diseño Builder para la creación de objetos {@link Character}.
 * <p>
 * Permite la configuración paso a paso de un personaje y facilita la creación de
 * instancias complejas mediante el encadenamiento de métodos (fluent interface).
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 */

public class CharacterBuilder {

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

    // Getters

    /** @return El nombre configurado en el builder. */

    public String getName() {
        return name;
    }

    /** @return El valor de vida configurado. */

    public int getLife() {
        return life;
    }

    /** @return El valor de poder configurado. */

    public int getPower() {
        return power;
    }

    /** @return El identificador único configurado. */

    public int getId() {
        return id;
    }

    /** @return El objeto de habilidad especial configurado. */

    public SpecialSkill getSpecialSkill() {
        return specialSkill;
    }

    /** @return El inventario configurado. */

    public Inventory getInventory() {
        return inventory;
    }

    /** @return Lista de esbirros añadidos al builder. */

    public List<Minion> getMinions() {
        return minions;
    }

    /** @return Lista de debilidades añadidas al builder. */

    public List<Weakness> getWeaknesses() {
        return this.weaknesses;
    }

    /** @return Lista de fortalezas añadidas al builder. */

    public List<Strength> getStrengths(){
        return this.strengths;
    }

    // Setters

    /**
     * Define el nombre del personaje.
     * @param name Nombre del personaje.
     * @return La instancia actual de {@code CharacterBuilder}.
     */

    public CharacterBuilder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Define la vida inicial del personaje.
     * @param life Valor de vida (normalmente entre 0 y 5).
     * @return La instancia actual de {@code CharacterBuilder}.
     */

    public CharacterBuilder life(int life) {
        this.life = life;
        return this;
    }

    /**
     * Define el poder inicial del personaje.
     * @param power Valor de poder (normalmente entre 1 y 5).
     * @return La instancia actual de {@code CharacterBuilder}.
     */

    public CharacterBuilder power(int power) {
        this.power = power;
        return this;
    }

    /**
     * Define el ID del personaje.
     * @param id Identificador único numérico.
     * @return La instancia actual de {@code CharacterBuilder}.
     */

    public CharacterBuilder id(int id){
        this.id = id;
        return this;
    }

    /**
     * Define la habilidad especial del personaje.
     * @param specialSkill Instancia de {@link SpecialSkill}.
     * @return La instancia actual de {@code CharacterBuilder}.
     */

    public CharacterBuilder specialSkill(SpecialSkill specialSkill) {
        this.specialSkill = specialSkill;
        return this;
    }

    /**
     * Define el inventario inicial del personaje.
     * @param inventory Instancia de {@link Inventory}.
     * @return La instancia actual de {@code CharacterBuilder}.
     */

    public CharacterBuilder inventory(Inventory inventory) {
        this.inventory = inventory;
        return this;
    }

    /**
     * Añade un esbirro a la futura lista del personaje.
     * @param minion Esbirro a añadir.
     * @return La instancia actual de {@code CharacterBuilder}.
     */

    public CharacterBuilder addMinion(Minion minion) {
        this.minions.add(minion);
        return this;
    }

    /**
     * Añade una debilidad al builder.
     * @param weakness Debilidad a añadir.
     * @return La instancia actual de {@code CharacterBuilder}.
     */

    public CharacterBuilder addWeakness(Weakness weakness){
        this.weaknesses.add(weakness);
        return this;
    }

    /**
     * Añade una fortaleza al builder.
     * @param strength Fortaleza a añadir.
     * @return La instancia actual de {@code CharacterBuilder}.
     */

    public CharacterBuilder addStrength(Strength strength){
        this.strengths.add(strength);
        return this;
    }


}

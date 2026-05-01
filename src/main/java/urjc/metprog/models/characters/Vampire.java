package main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.minions.HumanMinion;
import main.java.urjc.metprog.models.minions.Minion;
import main.java.urjc.metprog.models.skills.Discipline;

/**
 * Representa a un Vampiro dentro del sistema de juego.
 * <p>
 * Los vampiros utilizan puntos de sangre para activar sus disciplinas (habilidades especiales)
 * y tienen restricciones específicas sobre qué tipo de esbirros pueden reclutar.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 * @see Character
 */

public class Vampire extends Character{

    /** Edad del vampiro en años. */

    private int age;

    /** Puntos de sangre disponibles para el uso de habilidades. Rango [0, 10]. */

    private int bloodPoints;

    /**
     * Construye un nuevo Vampiro utilizando el builder configurado.
     * <p>
     * Se inicializa por defecto con 10 puntos de sangre y una edad de 100 años.
     * </p>
     *
     * @param characterBuilder El builder con la configuración base del personaje.
     */

    public Vampire(CharacterBuilder characterBuilder) {
        super(characterBuilder);
        this.bloodPoints = 10;
        this.age = 100;
    }

    /**
     * Valida que los puntos de sangre se encuentren en el rango legal.
     *
     * @param bloodPoints Valor de puntos de sangre a validar.
     * @return El valor validado.
     * @throws IllegalArgumentException Si los puntos son menores que 0 o mayores que 10.
     */

    private static int validateBloodPoints(int bloodPoints){
        if (bloodPoints < 0 || bloodPoints > 10)
            throw new IllegalArgumentException("Los puntos de sangre deben estar entre 0 y 10");
        return bloodPoints;
    }

    // Setters

    /** @param age La nueva edad del vampiro. */

    public void setAge(int age){
        this.age = age;
    }

    /**
     * @param bloodPoints Nuevo nivel de puntos de sangre.
     * @throws IllegalArgumentException Si el valor no está entre 0 y 10.
     */

    public void setBloodPoints(int bloodPoints){
        this.bloodPoints = validateBloodPoints(bloodPoints);
    }

    // Getters

    /**
     * {@inheritDoc}
     * <p>
     * La defensa especial se activa solo si el vampiro dispone de puntos de sangre
     * suficientes para cubrir el coste de su {@link Discipline}. Si no tiene suficiente sangre,
     * el valor de defensa será 0.
     * </p>
     *
     * @return Puntos de defensa calculados según disponibilidad de sangre.
     */

    @Override
    public int getSpecialDefense() {
        Discipline discipline = (Discipline) this.getSpecialSkill();
        if (this.bloodPoints >= discipline.getBloodPointsCost())
            return discipline.getDefenseValue();
        return 0;
    }

    /**
     * {@inheritDoc}
     * <p>
     * El ataque especial consume los puntos de sangre requeridos por la {@link Discipline}.
     * Si el vampiro no tiene sangre suficiente, el ataque falla devolviendo 0.
     * </p>
     *
     * @return Puntos de ataque calculados; reduce el depósito de sangre si el ataque es exitoso.
     */

    @Override
    public int getSpecialAttack() {
        Discipline discipline = (Discipline) this.getSpecialSkill();
        if (this.bloodPoints >= discipline.getBloodPointsCost()){
            this.bloodPoints -= discipline.getBloodPointsCost();
            return discipline.getAttackValue();
        }
        return 0;
    }

    /**
     * Añade un esbirro a la colección del vampiro.
     * <p>
     * <b>Restricción:</b> Los vampiros no pueden tener esbirros de tipo {@link HumanMinion}.
     * </p>
     *
     * @param minion El esbirro que se desea añadir.
     * @throws IllegalArgumentException Si el esbirro proporcionado es una instancia de {@code HumanMinion}.
     */

    @Override
    public void addMinion(Minion minion){
        if (minion instanceof HumanMinion)
            throw new IllegalArgumentException("Los vampiros no pueden tener esbirros humanos");
        super.addMinion(minion);
    }

    /** @return Puntos de sangre actuales del vampiro. */

    public int getBloodPoints(){
        return this.bloodPoints;
    }

    /** @return Edad actual del vampiro. */

    public int getAge(){
        return this.age;
    }
}

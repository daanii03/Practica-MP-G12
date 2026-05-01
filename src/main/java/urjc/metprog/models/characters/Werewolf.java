package main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.skills.Gift;

/**
 * Representa a un Licántropo (Hombre Lobo) dentro del sistema de juego.
 * <p>
 * Los licántropos gestionan un recurso acumulable llamado {@code rabia}. Este recurso
 * es necesario para activar sus habilidades especiales, denominadas {@link Gift} (Dones).
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 * @see Character
 */

public class Werewolf extends Character {

    /** Nivel de rabia acumulada. Rango [0, 3]. */

    private int rage;

    /**
     * Construye un nuevo Licántropo utilizando el builder proporcionado.
     * <p>
     * Al inicio de su existencia, un licántropo comienza con 0 puntos de rabia.
     * </p>
     *
     * @param characterBuilder El builder con la configuración base del personaje.
     */

    public Werewolf(CharacterBuilder characterBuilder) {
        super(characterBuilder);
        this.rage = 0;
    }

    /**
     * Valida que el valor de rabia se encuentre dentro del rango permitido.
     *
     * @param cost Valor de rabia a validar.
     * @return El valor validado.
     * @throws IllegalArgumentException Si la rabia es menor que 0 o mayor que 3.
     */

    private static int validateRage(int cost){
        if (cost < 0 || cost > 3){
            throw new IllegalArgumentException("La rabia debe estar entre 0 y 3");
        }
        return cost;
    }


    // Getters

    /** @return El nivel actual de rabia acumulada. */

    public int getRage(){
        return this.rage;
    }

    /**
     * {@inheritDoc}
     * <p>
     * El ataque especial del Licántropo depende de su don activo ({@link Gift}).
     * Solo se devuelve el valor de ataque si la rabia actual es mayor o igual
     * al coste de rabia exigido por el don.
     * </p>
     *
     * @return Puntos de ataque especial; 0 si no dispone de suficiente rabia.
     */

    @Override
    public int getSpecialAttack() {
        Gift gift = (Gift) this.getSpecialSkill();
        return this.rage >= gift.getRageCost() ? gift.getAttackValue() : 0;
    }

    /**
     * {@inheritDoc}
     * <p>
     * La defensa especial se activa únicamente si el nivel de rabia acumulado
     * permite costear el uso del don activo.
     * </p>
     *
     * @return Puntos de defensa especial; 0 si no dispone de suficiente rabia.
     */

    @Override
    public int getSpecialDefense() {
        Gift gift = (Gift) this.getSpecialSkill();
        return this.rage >= gift.getRageCost() ? gift.getDefenseValue() : 0;
    }

    // Setters

    /**
     * Establece el nivel de rabia.
     * @param rage Nuevo valor de rabia.
     * @throws IllegalArgumentException Si el valor no está en el rango [0, 3].
     */

    private void setRage(int rage){
        this.rage = validateRage(rage);
    }

    // Methods

    /**
     * Incrementa la rabia del licántropo en una unidad, hasta un máximo de 3.
     */

    public void increaseRage(){
        if (rage < 3)
            this.rage++;
    }

    /**
     * Reinicia el contador de rabia del licántropo a 0.
     */

    public void resetRage(){
        this.rage = 0;
    }
}

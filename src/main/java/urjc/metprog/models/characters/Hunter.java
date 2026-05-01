package main.java.urjc.metprog.models.characters;

/**
 * Representa a un Cazador, un tipo específico de personaje con gestión de voluntad.
 * <p>
 * El Cazador posee un atributo exclusivo llamado {@code voluntad}, que fluctúa
 * entre 0 y 3, afectando a su desempeño en el juego.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 * @see Character
 */

public class Hunter extends Character {

    /** Valor de voluntad del cazador. Rango [0, 3]. */
    private int will;

    /**
     * Construye un nuevo Cazador utilizando el builder proporcionado.
     * <p>
     * Por defecto, un Cazador comienza con el nivel máximo de voluntad (3).
     * </p>
     *
     * @param characterBuilder El builder con la configuración base del personaje.
     */

    public Hunter(CharacterBuilder characterBuilder) {
        super(characterBuilder);
        this.will = 3;
    }

    /**
     * Valida que el valor de voluntad se encuentre dentro del rango permitido.
     *
     * @param will Valor de voluntad a validar.
     * @return El valor validado.
     * @throws IllegalArgumentException Si la voluntad no está entre 0 y 3.
     */

    private static int validateWill(int will){
        if (will < 0 || will > 3)
            throw new IllegalArgumentException("La voluntad debe estar entre 0 y 3");
        return will;
    }


    // Getters

    /** @return El nivel actual de voluntad del cazador. */

    public int getWill(){
        return this.will;
    }

    /**
     * {@inheritDoc}
     * <p>
     * En el caso del Cazador, el ataque especial se obtiene directamente del valor
     * de ataque de su habilidad especial activa.
     * </p>
     *
     * @return Valor entero del ataque especial.
     */

    @Override
    public int getSpecialAttack() {
        return this.getSpecialSkill().getAttackValue();
    }

    /**
     * {@inheritDoc}
     * <p>
     * La defensa especial del Cazador se calcula basándonos en el valor de defensa
     * de su habilidad especial.
     * </p>
     *
     * @return Valor entero de la defensa especial.
     */

    @Override
    public int getSpecialDefense() {
        return this.getSpecialSkill().getDefenseValue();
    }

    // Setters

    /**
     * @param will Nuevo valor de voluntad.
     * @throws IllegalArgumentException Si el valor está fuera del rango [0, 3].
     */

    public void setWill(int will){
        this.will = validateWill(will);
    }

    // Methods

    /**
     * Reduce la voluntad del cazador en una unidad hasta un mínimo de 0.
     */

    public void decreaseWill(){
        if (will > 0)
            this.will--;
    }

    /**
     * Aumenta la voluntad del cazador en una unidad hasta un máximo de 3.
     */

    public void increaseWill(){
        if (will < 3)
            this.will++;
    }

    /**
     * Restablece la voluntad del cazador a su valor inicial máximo (3).
     */

    public void resetWill(){
        this.will = 3;
    }

}

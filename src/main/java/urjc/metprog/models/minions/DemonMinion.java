package main.java.urjc.metprog.models.minions;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un esbirro de tipo Demonio.
 * <p>
 * Los demonios son una variante avanzada de esbirro que, además de sus atributos
 * básicos, poseen un pacto asociado y la capacidad de actuar como líderes,
 * conteniendo una lista de esbirros subordinados bajo su mando (estructura recursiva).
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 * @see Minion
 */

public class DemonMinion extends Minion {

    /** Descripción detallada del pacto vinculado a este demonio. */

    private String pactDescription;

    /**
     * Colección de esbirros subordinados al demonio.
     * Permite la implementación de una jerarquía de súbditos.
     */

    private final List<Minion> minions = new ArrayList<>();

    /**
     * Construye un nuevo esbirro demonio.
     *
     * @param name Nombre identificativo del demonio.
     * @param health Puntos de vida (debe cumplir el rango [1, 3] definido en {@link Minion}).
     * @param pactDescription Términos del pacto que otorga poder al demonio.
     * @throws IllegalArgumentException Si la salud proporcionada no es válida.
     */

    public DemonMinion(String name, int health, String pactDescription) {
        super(name, health);
        this.pactDescription = pactDescription;
    }

    // Getters

    /** @return Los términos del pacto demoníaco. */

    public String getPactDescription(){
        return this.pactDescription;
    }

    /**
     * Obtiene la lista de esbirros que sirven a este demonio.
     *
     * @return {@link List} con los esbirros subordinados.
     */

    public List<Minion> getMinions(){
        return this.minions;
    }

    // Setters

    /** @param pactDescription Nueva descripción para el pacto. */

    public void setPactDescription(String pactDescription){
        this.pactDescription = pactDescription;
    }

    // Methods

    /**
     * Añade un nuevo esbirro a la jerarquía de este demonio.
     *
     * @param minion Esbirro (cualquier subtipo) que se añade como subordinado.
     */

    public void addMinion(Minion minion){
        this.minions.add(minion);
    }

}

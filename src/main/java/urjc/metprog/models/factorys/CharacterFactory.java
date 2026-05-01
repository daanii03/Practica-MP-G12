package main.java.urjc.metprog.models.factorys;

import main.java.urjc.metprog.models.characters.Character;

/**
 * Clase abstracta que define el contrato para las fábricas de personajes.
 * <p>
 * Implementa el patrón de diseño <b>Factory Method</b>, proporcionando una interfaz
 * para la creación de diferentes tipos de {@link Character} sin especificar
 * sus clases concretas en este nivel.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 */

public abstract class CharacterFactory {

    /**
     * Crea una nueva instancia de un personaje con los parámetros básicos requeridos.
     * <p>
     * Las subclases concretas (como {@code VampireFactory} o {@code WerewolfFactory})
     * deben implementar este método para instanciar el tipo de personaje específico
     * utilizando su correspondiente {@code CharacterBuilder}.
     * </p>
     *
     * @param id Identificador único que se asignará al nuevo personaje.
     * @param name Nombre del personaje a crear.
     * @return Una instancia de una subclase de {@link Character}.
     */

    public abstract Character createCharacter(int id, String name);

}

package main.java.urjc.metprog.models.skills;

/**
 * Representa un Talento, la habilidad especial exclusiva de los cazadores.
 * <p>
 * A diferencia de las disciplinas o los dones, los talentos no requieren el
 * consumo de un recurso específico para su uso, representando el entrenamiento
 * y la pericia técnica del personaje. Hereda los modificadores de combate
 * directamente de {@link SpecialSkill}.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 * @see SpecialSkill
 */

public class Talent extends SpecialSkill {

    /**
     * Crea un nuevo Talento con nombre y modificadores de combate.
     *
     * @param name Nombre del talento (ej. "Disparo Certero").
     * @param attack Valor del modificador de ataque (rango definido en {@link SpecialSkill}).
     * @param defense Valor del modificador de defensa (rango definido en {@link SpecialSkill}).
     */

    public Talent(String name, int attack, int defense){
        super(name,attack,defense);
    }
}

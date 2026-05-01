package main.java.urjc.metprog.models.factorys;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.characters.Inventory;
import main.java.urjc.metprog.models.skills.Discipline;
import main.java.urjc.metprog.models.characters.Vampire;
import main.java.urjc.metprog.models.skills.DisciplineCatalogue;

import java.util.Random;

/**
 * Factoría concreta para la creación de personajes de tipo {@link Vampire}.
 * <p>
 * Esta clase se encarga de instanciar vampiros configurando sus atributos base
 * y asignándoles una disciplina especial obtenida de forma aleatoria del catálogo.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 * @see CharacterFactory
 */

public class VampireFactory extends CharacterFactory {

    /**
     * Crea e inicializa una nueva instancia de {@link Vampire}.
     * <p>
     * El proceso de fabricación incluye:
     * <ul>
     *   <li>Uso de {@link CharacterBuilder} para la construcción del objeto.</li>
     *   <li>Selección aleatoria de una {@link Discipline} desde {@link DisciplineCatalogue}.</li>
     *   <li>Asignación de valores estándar: Vida (5) y Poder (5).</li>
     *   <li>Inicialización de un {@link Inventory} nuevo.</li>
     * </ul>
     * </p>
     *
     * @param id Identificador único para el vampiro.
     * @param name Nombre del vampiro.
     * @return Una nueva instancia de {@link Vampire} lista para el juego.
     */

    @Override
    public Character createCharacter(int id, String name) {

        CharacterBuilder characterBuilder = new CharacterBuilder();

        DisciplineCatalogue[] options = DisciplineCatalogue.values();

        int randomIndex = new Random().nextInt(options.length);

        Discipline randomDiscipline = options[randomIndex].generateDiscipline();

        characterBuilder.id(id).name(name).specialSkill(randomDiscipline).life(5)
                .power(5).inventory(new Inventory());


        return new Vampire(characterBuilder);
    }
}

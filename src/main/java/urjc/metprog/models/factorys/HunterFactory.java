package main.java.urjc.metprog.models.factorys;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.characters.Hunter;
import main.java.urjc.metprog.models.characters.Inventory;
import main.java.urjc.metprog.models.modifiers.Strength;
import main.java.urjc.metprog.models.modifiers.StrengthCatalogue;
import main.java.urjc.metprog.models.modifiers.Weakness;
import main.java.urjc.metprog.models.modifiers.WeaknessCatalogue;
import main.java.urjc.metprog.models.skills.Talent;
import main.java.urjc.metprog.models.skills.TalentCatalogue;

import java.util.Random;

/**
 * Factoría concreta encargada de la creación de personajes de tipo {@link Hunter}.
 * <p>
 * Implementa la lógica específica para inicializar un Cazador, incluyendo la
 * selección aleatoria de un talento desde el catálogo disponible.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 * @see CharacterFactory
 */

public class HunterFactory extends CharacterFactory {

    /**
     * Crea e inicializa una nueva instancia de {@link Hunter}.
     * <p>
     * El proceso de creación sigue los siguientes pasos:
     * <ol>
     *   <li>Instancia un {@link CharacterBuilder}.</li>
     *   <li>Selecciona un talento aleatorio de {@link TalentCatalogue}.</li>
     *   <li>Configura el personaje con valores predeterminados (Vida: 5, Poder: 5).</li>
     *   <li>Asigna un inventario nuevo y vacío.</li>
     * </ol>
     * </p>
     *
     * @param id Identificador único para el cazador.
     * @param name Nombre del cazador.
     * @return Un nuevo objeto {@link Hunter} completamente configurado.
     */

    @Override
    public Character createCharacter(int id, String name) {

        CharacterBuilder characterBuilder = new CharacterBuilder();
        TalentCatalogue[] talentCatalogue = TalentCatalogue.values();

        Random random = new Random();

        int randomIndex = random.nextInt(talentCatalogue.length);

        Talent randomTalent = talentCatalogue[randomIndex].generateTalent();

        Weakness weakness;
        Strength strength;

        WeaknessCatalogue[] weaknessCatalogue = WeaknessCatalogue.values();
        StrengthCatalogue[] strengthCatalogue = StrengthCatalogue.values();


        randomIndex = random.nextInt(0,4);

        if (randomIndex != 3)
            characterBuilder.addWeakness(weaknessCatalogue[randomIndex].generateWeakness());

        randomIndex = random.nextInt(0,4);

        if (randomIndex != 3)
            characterBuilder.addStrength(strengthCatalogue[randomIndex].generateStrength());


        characterBuilder.id(id)
                .name(name)
                .life(5)
                .power(5)
                .specialSkill(randomTalent)
                .inventory(new Inventory());

        return new Hunter(characterBuilder);
    }
}

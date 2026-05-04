package main.java.urjc.metprog.models.factorys;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.characters.Inventory;
import main.java.urjc.metprog.models.characters.Werewolf;
import main.java.urjc.metprog.models.modifiers.Strength;
import main.java.urjc.metprog.models.modifiers.StrengthCatalogue;
import main.java.urjc.metprog.models.modifiers.Weakness;
import main.java.urjc.metprog.models.modifiers.WeaknessCatalogue;
import main.java.urjc.metprog.models.skills.Gift;
import main.java.urjc.metprog.models.skills.GiftCatalogue;

import java.util.Random;

/**
 * Factoría concreta para la generación de personajes de tipo {@link Werewolf}.
 * <p>
 * Esta clase implementa la lógica necesaria para construir un Licántropo,
 * seleccionando de forma aleatoria una habilidad especial de tipo {@link Gift}
 * desde el catálogo de dones disponibles.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 * @see CharacterFactory
 */

public class WerewolfFactory extends CharacterFactory{

    /**
     * Crea e inicializa una nueva instancia de {@link Werewolf}.
     * <p>
     * El proceso de creación sigue los estándares de la jerarquía de factorías:
     * <ol>
     *   <li>Consulta el {@link GiftCatalogue} para obtener un don aleatorio.</li>
     *   <li>Utiliza un {@link CharacterBuilder} para centralizar la configuración.</li>
     *   <li>Establece los valores iniciales de Vida y Poder en 5.</li>
     *   <li>Asocia un {@link Inventory} recién instanciado.</li>
     * </ol>
     * </p>
     *
     * @param id Identificador único para el licántropo.
     * @param name Nombre del personaje.
     * @return Una instancia de {@link Werewolf} completamente inicializada.
     */

    @Override
    public Character createCharacter(int id, String name) {

        Random random = new Random();

        GiftCatalogue[] giftCatalogue = GiftCatalogue.values();



        int randomIndex = random.nextInt(giftCatalogue.length);

        Gift gift = giftCatalogue[randomIndex].generateGift();

        Weakness weakness;
        Strength strength;

        WeaknessCatalogue[] weaknessCatalogue = WeaknessCatalogue.values();
        StrengthCatalogue[] strengthCatalogue = StrengthCatalogue.values();


        randomIndex = random.nextInt(6,weaknessCatalogue.length + 1);

        if (randomIndex == weaknessCatalogue.length + 1)
            weakness = null;
        else
            weakness = weaknessCatalogue[randomIndex].generateWeakness();

        randomIndex = random.nextInt(6,weaknessCatalogue.length +1);

        if (randomIndex == weaknessCatalogue.length)
            strength = null;
        else
            strength = strengthCatalogue[randomIndex].generateStrength();



        CharacterBuilder characterBuilder = new CharacterBuilder();

        characterBuilder.id(id)
                .name(name)
                .specialSkill(gift)
                .life(5)
                .power(5)
                .inventory(new Inventory())
                .addWeakness(weakness)
                .addStrength(strength);

        return new Werewolf(characterBuilder);
    }
}

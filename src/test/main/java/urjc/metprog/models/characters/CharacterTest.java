package test.main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.minions.HumanMinion;
import main.java.urjc.metprog.models.minions.Loyalty;
import main.java.urjc.metprog.models.modifiers.Strength;
import main.java.urjc.metprog.models.modifiers.Weakness;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase abstracta Character.
 *
 * Como Character es abstracta, se usa TestCharacter, una subclase
 * mínima definida al final de este fichero, para poder instanciarla.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CharacterTest {

    private TestCharacter character;

    @BeforeEach
    void setUp() {
        // Creamos un personaje de prueba con vida=5 y poder=3
        CharacterBuilder builder = new CharacterBuilder()
                .name("TestHero")
                .id(1)
                .life(5)
                .power(3);
        character = new TestCharacter(builder);
    }

    // ── Constructor y getters ─────────────────────────────────────────────────

    @Test
    @Order(1)
    @DisplayName("Constructor inicializa los atributos correctamente")
    void testConstructorInitializesFields() {
        assertEquals("TestHero", character.getName());
        assertEquals(5, character.getLife());
        assertEquals(3, character.getPower());
        assertEquals(1, character.getId());
    }

    @Test
    @Order(2)
    @DisplayName("Constructor con vida fuera de rango [0,5] lanza excepción")
    void testConstructorInvalidLife() {
        assertThrows(IllegalArgumentException.class, () ->
                        new TestCharacter(new CharacterBuilder().name("X").life(6).power(3)),
                "Vida > 5 debe lanzar IllegalArgumentException"
        );
        assertThrows(IllegalArgumentException.class, () ->
                        new TestCharacter(new CharacterBuilder().name("X").life(-1).power(3)),
                "Vida < 0 debe lanzar IllegalArgumentException"
        );
    }

    @Test
    @Order(3)
    @DisplayName("Constructor con poder fuera de rango [1,5] lanza excepción")
    void testConstructorInvalidPower() {
        assertThrows(IllegalArgumentException.class, () ->
                        new TestCharacter(new CharacterBuilder().name("X").life(5).power(0)),
                "Poder < 1 debe lanzar IllegalArgumentException"
        );
        assertThrows(IllegalArgumentException.class, () ->
                        new TestCharacter(new CharacterBuilder().name("X").life(5).power(6)),
                "Poder > 5 debe lanzar IllegalArgumentException"
        );
    }

    // ── Vida ──────────────────────────────────────────────────────────────────

    @Test
    @Order(4)
    @DisplayName("increaseLife suma vida correctamente")
    void testIncreaseLife() {
        character.setLife(3);
        character.increaseLife(1);
        assertEquals(4, character.getLife());
    }

    @Test
    @Order(5)
    @DisplayName("increaseLife no supera el máximo de 5")
    void testIncreaseLifeCapsAtMax() {
        character.setLife(4);
        character.increaseLife(10);
        assertEquals(5, character.getLife(), "La vida no puede superar 5");
    }

    @Test
    @Order(6)
    @DisplayName("decreaseLife resta vida correctamente")
    void testDecreaseLife() {
        character.setLife(5);
        character.decreaseLife(2);
        assertEquals(3, character.getLife());
    }

    @Test
    @Order(7)
    @DisplayName("decreaseLife no baja del mínimo de 0")
    void testDecreaseLifeFloorsAtZero() {
        character.setLife(1);
        character.decreaseLife(99);
        assertEquals(0, character.getLife(), "La vida no puede ser negativa");
    }

    @Test
    @Order(8)
    @DisplayName("setLife con valor inválido lanza excepción")
    void testSetLifeInvalid() {
        assertThrows(IllegalArgumentException.class, () -> character.setLife(-1));
        assertThrows(IllegalArgumentException.class, () -> character.setLife(6));
    }

    // ── Poder ─────────────────────────────────────────────────────────────────

    @Test
    @Order(9)
    @DisplayName("increasePower suma poder correctamente")
    void testIncreasePower() {
        character.setPower(3);
        character.increasePower(1);
        assertEquals(4, character.getPower());
    }

    @Test
    @Order(10)
    @DisplayName("increasePower no supera el máximo de 5")
    void testIncreasePowerCapsAtMax() {
        character.setPower(4);
        character.increasePower(10);
        assertEquals(5, character.getPower(), "El poder no puede superar 5");
    }

    @Test
    @Order(11)
    @DisplayName("decreasePower resta poder correctamente")
    void testDecreasePower() {
        character.setPower(3);
        character.decreasePower(1);
        assertEquals(2, character.getPower());
    }

    @Test
    @Order(12)
    @DisplayName("decreasePower no baja del mínimo de 1")
    void testDecreasePowerFloorsAtOne() {
        character.setPower(1);
        character.decreasePower(99);
        assertEquals(1, character.getPower(), "El poder no puede bajar de 1");
    }

    @Test
    @Order(13)
    @DisplayName("setPower con valor inválido lanza excepción")
    void testSetPowerInvalid() {
        assertThrows(IllegalArgumentException.class, () -> character.setPower(0));
        assertThrows(IllegalArgumentException.class, () -> character.setPower(6));
    }

    // ── Esbirros ──────────────────────────────────────────────────────────────

    @Test
    @Order(14)
    @DisplayName("addMinion añade un esbirro a la lista")
    void testAddMinion() {
        assertTrue(character.getMinions().isEmpty(), "Debe empezar sin esbirros");
        character.addMinion(new HumanMinion("Igor", 2, Loyalty.ALTA));
        assertEquals(1, character.getMinions().size());
        assertEquals("Igor", character.getMinions().get(0).getName());
    }

    @Test
    @Order(15)
    @DisplayName("removeMinion elimina el esbirro correcto por índice")
    void testRemoveMinion() {
        character.addMinion(new HumanMinion("Igor", 2, Loyalty.ALTA));
        character.addMinion(new HumanMinion("Renfield", 1, Loyalty.BAJA));
        character.removeMinion(0);
        assertEquals(1, character.getMinions().size());
        assertEquals("Renfield", character.getMinions().get(0).getName());
    }

    @Test
    @Order(16)
    @DisplayName("removeMinion con índice inválido lanza IndexOutOfBoundsException")
    void testRemoveMinionInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () ->
                        character.removeMinion(0),
                "Lista vacía: índice 0 debe lanzar excepción"
        );
        character.addMinion(new HumanMinion("Igor", 2, Loyalty.NORMAL));
        assertThrows(IndexOutOfBoundsException.class, () ->
                        character.removeMinion(5),
                "Índice fuera de rango debe lanzar excepción"
        );
    }

    @Test
    @Order(17)
    @DisplayName("setMinions reemplaza la lista completa de esbirros")
    void testSetMinionsReplacesAll() {
        character.addMinion(new HumanMinion("Viejo", 1, Loyalty.BAJA));
        character.setMinions(java.util.List.of(
                new HumanMinion("Nuevo1", 2, Loyalty.ALTA),
                new HumanMinion("Nuevo2", 1, Loyalty.NORMAL)
        ));
        assertEquals(2, character.getMinions().size());
        assertEquals("Nuevo1", character.getMinions().get(0).getName());
    }

    @Test
    @Order(18)
    @DisplayName("setMinions con null vacía la lista")
    void testSetMinionsNull() {
        character.addMinion(new HumanMinion("Igor", 2, Loyalty.ALTA));
        character.setMinions(null);
        assertTrue(character.getMinions().isEmpty(), "null debe vaciar la lista de esbirros");
    }

    // ── Fortalezas y debilidades ──────────────────────────────────────────────

    @Test
    @Order(19)
    @DisplayName("addStrength añade una fortaleza")
    void testAddStrength() {
        character.addStrength(new Strength("Fuego", 2));
        assertEquals(1, character.getStrengths().size());
    }

    @Test
    @Order(20)
    @DisplayName("addWeakness añade una debilidad")
    void testAddWeakness() {
        character.addWeakness(new Weakness("Agua", 1));
        assertEquals(1, character.getWeaknesses().size());
    }

    // ── Subclase mínima para instanciar Character ─────────────────────────────

    /**
     * Implementación concreta mínima de Character para poder testear
     * la lógica de la clase abstracta sin depender de Vampire/Werewolf/Hunter.
     */
    static class TestCharacter extends Character {
        public TestCharacter(CharacterBuilder builder) {
            super(builder);
        }

        @Override
        public int getSpecialAttack()  { return getPower(); }

        @Override
        public int getSpecialDefense() { return getLife(); }
    }
}
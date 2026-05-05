package test.main.java.urjc.metprog.models.characters;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.minions.HumanMinion;
import main.java.urjc.metprog.models.minions.Loyalty;
import main.java.urjc.metprog.models.modifiers.Strength;
import main.java.urjc.metprog.models.modifiers.Weakness;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test para la clase abstracta Character usando una implementación concreta mínima.
 * Se utiliza @TestMethodOrder para asegurar que los tests básicos se ejecuten primero.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CharacterTest {

    private TestCharacter character;

    @BeforeEach
    void setUp() {
        // Valores base consistentes para la mayoría de los tests
        CharacterBuilder builder = new CharacterBuilder()
                .name("TestHero")
                .id(1)
                .life(5)
                .power(3);
        this.character = new TestCharacter(builder);
    }

    // ── 1. Constructor y Atributos Básicos ────────────────────────────────────

    @Test
    @Order(1)
    @DisplayName("Constructor inicializa los atributos correctamente")
    void testConstructorInitializesFields() {
        assertAll("Verificación de atributos iniciales",
                () -> assertEquals("TestHero", character.getName()),
                () -> assertEquals(5, character.getLife()),
                () -> assertEquals(3, character.getPower()),
                () -> assertEquals(1, character.getId())
        );
    }

    @Test
    @Order(2)
    @DisplayName("Constructor con vida fuera de rango [0,5] lanza excepción")
    void testConstructorInvalidLife() {
        assertThrows(IllegalArgumentException.class, () ->
                new TestCharacter(new CharacterBuilder().name("X").life(6).power(3))
        );
        assertThrows(IllegalArgumentException.class, () ->
                new TestCharacter(new CharacterBuilder().name("X").life(-1).power(3))
        );
    }

    @Test
    @Order(3)
    @DisplayName("Constructor con poder fuera de rango [1,5] lanza excepción")
    void testConstructorInvalidPower() {
        assertThrows(IllegalArgumentException.class, () ->
                new TestCharacter(new CharacterBuilder().name("X").life(5).power(0))
        );
        assertThrows(IllegalArgumentException.class, () ->
                new TestCharacter(new CharacterBuilder().name("X").life(5).power(6))
        );
    }

    // ── 2. Gestión de Vida ────────────────────────────────────────────────────

    @Test
    @Order(4)
    @DisplayName("increaseLife suma vida correctamente y respeta el máximo")
    void testIncreaseLife() {
        character.setLife(3);
        character.increaseLife(1);
        assertEquals(4, character.getLife());

        character.increaseLife(10);
        assertEquals(5, character.getLife(), "La vida no puede superar 5");
    }

    @Test
    @Order(5)
    @DisplayName("decreaseLife resta vida correctamente y respeta el mínimo")
    void testDecreaseLife() {
        character.setLife(5);
        character.decreaseLife(2);
        assertEquals(3, character.getLife());

        character.decreaseLife(10);
        assertEquals(0, character.getLife(), "La vida no puede bajar de 0");
    }

    @Test
    @Order(6)
    @DisplayName("setLife con valor inválido lanza excepción")
    void testSetLifeInvalid() {
        assertThrows(IllegalArgumentException.class, () -> character.setLife(-1));
        assertThrows(IllegalArgumentException.class, () -> character.setLife(6));
    }

    // ── 3. Gestión de Poder ───────────────────────────────────────────────────

    @Test
    @Order(7)
    @DisplayName("increasePower suma poder y respeta el máximo")
    void testIncreasePower() {
        character.setPower(3);
        character.increasePower(1);
        assertEquals(4, character.getPower());

        character.increasePower(10);
        assertEquals(5, character.getPower(), "El poder no puede superar 5");
    }

    @Test
    @Order(8)
    @DisplayName("decreasePower resta poder y respeta el mínimo")
    void testDecreasePower() {
        character.setPower(3);
        character.decreasePower(1);
        assertEquals(2, character.getPower());

        character.decreasePower(10);
        assertEquals(1, character.getPower(), "El poder no puede bajar de 1");
    }

    @Test
    @Order(9)
    @DisplayName("setPower con valor inválido lanza excepción")
    void testSetPowerInvalid() {
        assertThrows(IllegalArgumentException.class, () -> character.setPower(0));
        assertThrows(IllegalArgumentException.class, () -> character.setPower(6));
    }

    // ── 4. Gestión de Esbirros (Minions) ──────────────────────────────────────

    @Test
    @Order(10)
    @DisplayName("addMinion y removeMinion funcionan correctamente")
    void testMinionManagement() {
        HumanMinion m1 = new HumanMinion("Igor", 2, Loyalty.ALTA);
        character.addMinion(m1);
        assertEquals(1, character.getMinions().size());

        character.removeMinion(0);
        assertTrue(character.getMinions().isEmpty());
    }

    @Test
    @Order(11)
    @DisplayName("removeMinion con índice inválido lanza excepción")
    void testRemoveMinionInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> character.removeMinion(0));
    }

    @Test
    @Order(12)
    @DisplayName("setMinions reemplaza la lista o la vacía si es null")
    void testSetMinions() {
        List<main.java.urjc.metprog.models.minions.Minion> newList = List.of(
                new HumanMinion("Nuevo", 1, Loyalty.NORMAL)
        );
        character.setMinions(newList);
        assertEquals(1, character.getMinions().size());

        character.setMinions(null);
        assertTrue(character.getMinions().isEmpty());
    }

    // ── 5. Modificadores ──────────────────────────────────────────────────────

    @Test
    @Order(13)
    @DisplayName("Añadir fortalezas y debilidades")
    void testAddModifiers() {
        character.addStrength(new Strength("Fuego", 2));
        character.addWeakness(new Weakness("Agua", -1));

        assertEquals(1, character.getStrengths().size());
        assertEquals(1, character.getWeaknesses().size());
    }

    // ── Subclase para Testing ─────────────────────────────────────────────────

    static class TestCharacter extends Character {
        TestCharacter(CharacterBuilder builder) {
            super(builder);
        }

        @Override
        public int getSpecialAttack() { return 0; }

        @Override
        public int getSpecialDefense() { return 0; }
    }
}
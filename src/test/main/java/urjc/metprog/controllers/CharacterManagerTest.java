package test.main.java.urjc.metprog.controllers;

import main.java.urjc.metprog.controllers.CharacterManager;
import main.java.urjc.metprog.dao.CharacterDao;
import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.characters.Inventory;
import main.java.urjc.metprog.models.characters.Vampire;
import main.java.urjc.metprog.models.equipment.Armor;
import main.java.urjc.metprog.models.equipment.Weapon;
import main.java.urjc.metprog.models.minions.GhoulMinion;
import main.java.urjc.metprog.models.modifiers.Strength;
import main.java.urjc.metprog.models.modifiers.Weakness;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CharacterManagerTest {

    private CharacterManager manager;
    private FakeCharacterDao fakeDao;
    private Character dummyCharacter;

    /**
     * Doble de pruebas (Fake/Stub) para aislar el controlador de la base de datos real.
     * Nos permite "espiar" qué hace el CharacterManager sin usar SQLite.
     */
    class FakeCharacterDao implements CharacterDao {
        Character insertedCharacter = null;
        String insertedOwner = null;
        int deletedId = -1;
        Character returnedCharacter = null;
        Map<String, Character> returnedMap = new HashMap<>();
        List<String[]> returnedCatalog = new ArrayList<>();

        @Override
        public void insertCharacter(Character character, String ownerNick) {
            this.insertedCharacter = character;
            this.insertedOwner = ownerNick;
        }

        @Override
        public Character getCharacter(int id) { return returnedCharacter; }

        @Override
        public Character getCharacterByOwner(String ownerNick) { return returnedCharacter; }

        @Override
        public void deleteCharacter(int id) { this.deletedId = id; }

        @Override
        public Map<String, Character> getAllCharactersWithOwners() { return returnedMap; }

        @Override
        public List<String[]> getEquipmentCatalog() { return returnedCatalog; }
    }

    @BeforeEach
    void setUp() {
        fakeDao = new FakeCharacterDao();
        manager = new CharacterManager(fakeDao);

        Inventory inv = new Inventory();
        dummyCharacter = new Vampire(new CharacterBuilder()
                .id(99)
                .name("Conde Pátula")
                .life(5)
                .power(5)
                .inventory(inv));
    }

    @Test
    void createCharacter() {
        Character c = manager.createCharacter("dueño1", "Vlad", "vampire");

        assertNotNull(c, "El personaje no debería ser nulo");
        assertEquals("Vlad", c.getName(), "El nombre debería coincidir");
        assertTrue(c instanceof Vampire, "Debería instanciar un Vampiro");

        assertEquals(c, fakeDao.insertedCharacter, "El manager debería haber insertado el personaje en el DAO");
        assertEquals("dueño1", fakeDao.insertedOwner);

        assertThrows(IllegalArgumentException.class, () -> {
            manager.createCharacter("dueño1", "Orco", "orc");
        }, "Debería lanzar error si la raza no existe");
    }

    @Test
    void deleteCharacter() {
        manager.deleteCharacter(99);
        assertEquals(99, fakeDao.deletedId, "Debería llamar a deleteCharacter del DAO con el ID correcto");
    }

    @Test
    void getCharacterByUser() {
        fakeDao.returnedCharacter = dummyCharacter;

        Character c = manager.getCharacterByUser("dueño1");
        assertEquals(dummyCharacter, c, "Debería devolver el personaje extraído del DAO");
    }

    @Test
    void changeActiveEquipment() {
        fakeDao.returnedCharacter = dummyCharacter;

        Weapon espada = new Weapon("Espada", 2, 0, true);
        dummyCharacter.getInventory().addWeapon(espada);

        List<Weapon> armasAEquipar = new ArrayList<>();
        armasAEquipar.add(espada);

        manager.changeActiveEquipment("dueño1", armasAEquipar, null);

        assertEquals(99, fakeDao.deletedId, "Debería borrar el personaje antiguo");
        assertNotNull(fakeDao.insertedCharacter, "Debería insertar el personaje actualizado");
        assertEquals(1, fakeDao.insertedCharacter.getInventory().getActiveWeapons().size(), "El arma debería estar equipada");
    }

    @Test
    void updateCharacter() {
        manager.updateCharacter("dueño1", dummyCharacter);

        assertEquals(99, fakeDao.deletedId, "Debería borrar el ID antiguo (99)");
        assertEquals(dummyCharacter, fakeDao.insertedCharacter, "Debería insertar el objeto modificado");

        assertThrows(IllegalArgumentException.class, () -> {
            manager.updateCharacter("dueño1", null);
        }, "No debería permitir actualizar con un personaje nulo");
    }

    @Test
    void addWeaponToInventory() {
        fakeDao.returnedCharacter = dummyCharacter;
        Weapon hacha = new Weapon("Hacha", 3, 0, false);

        manager.addWeaponToInventory("dueño1", hacha);

        assertTrue(fakeDao.insertedCharacter.getInventory().getWeapons().contains(hacha),
                "El arma nueva debería estar en el inventario del personaje insertado");
    }

    @Test
    void addArmorToInventory() {
        fakeDao.returnedCharacter = dummyCharacter;
        Armor cota = new Armor("Cota de Malla", 0, 2);

        manager.addArmorToInventory("dueño1", cota);

        assertTrue(fakeDao.insertedCharacter.getInventory().getArmors().contains(cota),
                "La armadura nueva debería estar en el inventario");
    }

    @Test
    void addMinion() {
        fakeDao.returnedCharacter = dummyCharacter;
        GhoulMinion ghoul = new GhoulMinion("Sirviente", 3, 1);

        manager.addMinion("dueño1", ghoul);

        assertTrue(fakeDao.insertedCharacter.getMinions().contains(ghoul),
                "El esbirro debería haberse añadido a la colección");
    }

    @Test
    void addStrength() {
        fakeDao.returnedCharacter = dummyCharacter;
        Strength fortaleza = new Strength("Sol de Medianoche", 2);

        manager.addStrength("dueño1", fortaleza);

        assertTrue(fakeDao.insertedCharacter.getStrengths().contains(fortaleza),
                "La fortaleza debería haberse añadido");
    }

    @Test
    void addWeakness() {
        fakeDao.returnedCharacter = dummyCharacter;
        Weakness debilidad = new Weakness("Ajo", 2);

        manager.addWeakness("dueño1", debilidad);

        assertTrue(fakeDao.insertedCharacter.getWeaknesses().contains(debilidad),
                "La debilidad debería haberse añadido");
    }

    @Test
    void getAllCharacters() {
        Map<String, Character> mapaSimulado = new HashMap<>();
        mapaSimulado.put("dueño1", dummyCharacter);
        fakeDao.returnedMap = mapaSimulado;

        assertEquals(mapaSimulado, manager.getAllCharacters(), "Debería devolver el mapa directamente del DAO");
    }

    @Test
    void getCatalog() {
        List<String[]> catalogoSimulado = new ArrayList<>();
        catalogoSimulado.add(new String[]{"1", "Espada", "weapon", "2", "0", "1"});
        fakeDao.returnedCatalog = catalogoSimulado;

        assertEquals(catalogoSimulado, manager.getCatalog(), "Debería devolver el catálogo directamente del DAO");
    }

    @Test
    void removeWeaponFromInventory() {
        fakeDao.returnedCharacter = dummyCharacter;
        Weapon daga = new Weapon("Daga", 1, 0, true);
        dummyCharacter.getInventory().addWeapon(daga);

        manager.removeWeaponFromInventory("dueño1", daga);

        assertFalse(fakeDao.insertedCharacter.getInventory().getWeapons().contains(daga),
                "El arma debería haber sido eliminada antes del reemplazo");
    }

    @Test
    void removeArmorFromInventory() {
        fakeDao.returnedCharacter = dummyCharacter;
        Armor casco = new Armor("Casco", 0, 1);
        dummyCharacter.getInventory().addArmor(casco);

        manager.removeArmorFromInventory("dueño1", casco);

        assertFalse(fakeDao.insertedCharacter.getInventory().getArmors().contains(casco),
                "La armadura debería haber sido eliminada");
    }

    @Test
    void deleteMinion() {
        fakeDao.returnedCharacter = dummyCharacter;
        GhoulMinion ghoul = new GhoulMinion("Sirviente", 3, 1);
        dummyCharacter.addMinion(ghoul);

    }
}
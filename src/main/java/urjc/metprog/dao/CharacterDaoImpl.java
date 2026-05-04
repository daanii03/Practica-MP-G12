package main.java.urjc.metprog.dao;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.CharacterBuilder;
import main.java.urjc.metprog.models.characters.Hunter;
import main.java.urjc.metprog.models.characters.Inventory;
import main.java.urjc.metprog.models.characters.Vampire;
import main.java.urjc.metprog.models.characters.Werewolf;
import main.java.urjc.metprog.models.equipment.Armor;
import main.java.urjc.metprog.models.equipment.Weapon;
import main.java.urjc.metprog.models.minions.DemonMinion;
import main.java.urjc.metprog.models.minions.GhoulMinion;
import main.java.urjc.metprog.models.minions.HumanMinion;
import main.java.urjc.metprog.models.minions.Loyalty;
import main.java.urjc.metprog.models.minions.Minion;
import main.java.urjc.metprog.models.modifiers.Strength;
import main.java.urjc.metprog.models.modifiers.Weakness;
import main.java.urjc.metprog.models.skills.Discipline;
import main.java.urjc.metprog.models.skills.Gift;
import main.java.urjc.metprog.models.skills.SpecialSkill;
import main.java.urjc.metprog.models.skills.Talent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CharacterDaoImpl implements CharacterDao {

    // ==========================================
    // OPERACIONES PRINCIPALES (CRUD)
    // ==========================================

    @Override
    public void insertCharacter(Character character, String ownerNick) {
        int userId = getUserIdByNick(ownerNick);
        
        if (userId == -1) {
            System.err.println("Error: No se puede insertar el personaje porque el dueño '" + ownerNick + "' no existe en la base de datos.");
            return;
        }

        String sqlInsert = "INSERT INTO personajes (user_id, name, race, gold, life, power, "
                         + "special_skill, skill_name, skill_attack, skill_defense, skill_cost, "
                         + "age, blood_points, rage, will) "
                         + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, character.getName());
            
            // Determinar raza
            String race = "vampire";
            if (character instanceof Werewolf) {
                race = "werewolf";
            } else if (character instanceof Hunter) {
                race = "hunter";
            }
            pstmt.setString(3, race);
            
            pstmt.setInt(4, character.checkGold());
            pstmt.setInt(5, character.getLife());
            pstmt.setInt(6, character.getPower());
            
            // Habilidad especial: tipo + detalles
            SpecialSkill skill = character.getSpecialSkill();
            String specialSkillType = "discipline";
            int skillCost = 0;
            if (skill instanceof Gift) {
                specialSkillType = "gift";
                skillCost = ((Gift) skill).getRageCost();
            } else if (skill instanceof Talent) {
                specialSkillType = "talent";
                skillCost = 0; // los talentos no tienen coste
            } else if (skill instanceof Discipline) {
                specialSkillType = "discipline";
                skillCost = ((Discipline) skill).getBloodPointsCost();
            }
            pstmt.setString(7, specialSkillType);
            pstmt.setString(8, skill.getName());
            pstmt.setInt(9, skill.getAttackValue());
            pstmt.setInt(10, skill.getDefenseValue());
            pstmt.setInt(11, skillCost);
            
            // Atributos exclusivos de raza
            if (character instanceof Vampire) {
                Vampire v = (Vampire) character;
                pstmt.setInt(12, v.getAge());
                pstmt.setInt(13, v.getBloodPoints());
                pstmt.setNull(14, java.sql.Types.INTEGER); // rage
                pstmt.setNull(15, java.sql.Types.INTEGER); // will
            } else if (character instanceof Werewolf) {
                Werewolf w = (Werewolf) character;
                pstmt.setNull(12, java.sql.Types.INTEGER); // age
                pstmt.setNull(13, java.sql.Types.INTEGER); // blood_points
                pstmt.setInt(14, w.getRage());
                pstmt.setNull(15, java.sql.Types.INTEGER); // will
            } else if (character instanceof Hunter) {
                Hunter h = (Hunter) character;
                pstmt.setNull(12, java.sql.Types.INTEGER); // age
                pstmt.setNull(13, java.sql.Types.INTEGER); // blood_points
                pstmt.setNull(14, java.sql.Types.INTEGER); // rage
                pstmt.setInt(15, h.getWill());
            }
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int characterId = generatedKeys.getInt(1);
                        character.setId(characterId);
                        
                        // Enlazar en la tabla usuarios
                        linkCharacterToUser(userId, characterId);
                        
                        // Persistir equipamiento, esbirros y modificadores
                        insertEquipment(characterId, character);
                        insertMinions(characterId, character.getMinions());
                        insertModifiers(characterId, character.getWeaknesses(), character.getStrengths());
                        
                        System.out.println("Personaje '" + character.getName() + "' insertado correctamente y enlazado al usuario '" + ownerNick + "'.");
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al insertar personaje: " + e.getMessage());
        }
    }

    @Override
    public Character getCharacter(int id) {
        String sql = "SELECT * FROM personajes WHERE id = ?";
        return fetchCharacterByQuery(sql, id, null);
    }

    @Override
    public Character getCharacterByOwner(String ownerNick) {
        int userId = getUserIdByNick(ownerNick);
        if (userId == -1) return null;
        
        String sql = "SELECT * FROM personajes WHERE user_id = ?";
        return fetchCharacterByQuery(sql, userId, null);
    }

    @Override
    public void deleteCharacter(int id) {
        // Las tablas secundarias tienen ON DELETE CASCADE,
        // así que al borrar el personaje se borran automáticamente
        // su equipamiento, esbirros y modificadores.
        String sql = "DELETE FROM personajes WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Primero quitamos el enlace en la tabla de usuarios
            unlinkCharacterFromUser(id);
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Personaje con ID " + id + " borrado correctamente (incluyendo equipamiento, esbirros y modificadores).");
            } else {
                System.out.println("No se ha encontrado un personaje con ID " + id + " para borrar.");
            }
            
        } catch (SQLException e) {
            System.err.println("Error al borrar personaje: " + e.getMessage());
        }
    }

    // ==========================================
    // MÉTODOS AUXILIARES PRIVADOS: USUARIOS
    // ==========================================

    private int getUserIdByNick(String nick) {
        String sql = "SELECT id FROM usuarios WHERE nick = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nick);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
        return -1;
    }

    private void linkCharacterToUser(int userId, int characterId) {
        String sql = "UPDATE usuarios SET character_id = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, characterId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error al enlazar personaje con usuario: " + e.getMessage());
        }
    }

    private void unlinkCharacterFromUser(int characterId) {
        String sql = "UPDATE usuarios SET character_id = NULL WHERE character_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, characterId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Error al desenlazar personaje del usuario: " + e.getMessage());
        }
    }

    // ==========================================
    // MÉTODOS AUXILIARES PRIVADOS: EQUIPAMIENTO
    // ==========================================

    private void insertEquipment(int characterId, Character character) {
        Inventory inv = character.getInventory();
        if (inv == null) return;

        String sql = "INSERT INTO equipamiento (character_id, type, name, attack_modifier, defense_modifier, is_one_hand, is_active) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Guardar todas las armas del inventario
            List<Weapon> activeWeapons = inv.getActiveWeapons();
            for (Weapon w : inv.getWeapons()) {
                pstmt.setInt(1, characterId);
                pstmt.setString(2, "weapon");
                pstmt.setString(3, w.getName());
                pstmt.setInt(4, w.getAttackModifier());
                pstmt.setInt(5, w.getDefenseModifier());
                pstmt.setInt(6, w.isOneHandWeapon() ? 1 : 0);
                pstmt.setInt(7, activeWeapons.contains(w) ? 1 : 0);
                pstmt.addBatch();
            }

            // Guardar todas las armaduras del inventario
            Armor activeArmor = inv.getActiveArmor();
            for (Armor a : inv.getArmors()) {
                pstmt.setInt(1, characterId);
                pstmt.setString(2, "armor");
                pstmt.setString(3, a.getName());
                pstmt.setInt(4, a.getAttackModifier());
                pstmt.setInt(5, a.getDefenseModifier());
                pstmt.setNull(6, java.sql.Types.INTEGER); // is_one_hand no aplica a armaduras
                pstmt.setInt(7, (activeArmor != null && activeArmor.equals(a)) ? 1 : 0);
                pstmt.addBatch();
            }

            pstmt.executeBatch();
        } catch (SQLException e) {
            System.err.println("Error al guardar equipamiento: " + e.getMessage());
        }
    }

    private Inventory loadEquipment(int characterId) {
        Inventory inv = new Inventory();
        inv.setGold(0); // el oro se carga desde la tabla personajes

        String sql = "SELECT * FROM equipamiento WHERE character_id = ?";
        
        List<Weapon> activeWeapons = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, characterId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("type");
                    String name = rs.getString("name");
                    int atk = rs.getInt("attack_modifier");
                    int def = rs.getInt("defense_modifier");
                    boolean isActive = rs.getInt("is_active") == 1;

                    if ("weapon".equals(type)) {
                        boolean oneHand = rs.getInt("is_one_hand") == 1;
                        Weapon w = new Weapon(name, atk, def, oneHand);
                        inv.addWeapon(w);
                        if (isActive) {
                            activeWeapons.add(w);
                        }
                    } else { // armor
                        Armor a = new Armor(name, atk, def);
                        inv.addArmor(a);
                        if (isActive) {
                            inv.setActiveArmor(a);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar equipamiento: " + e.getMessage());
        }

        // Establecer armas activas (solo si hay alguna)
        if (!activeWeapons.isEmpty()) {
            inv.setActiveWeapons(activeWeapons);
        }

        return inv;
    }

    // ==========================================
    // MÉTODOS AUXILIARES PRIVADOS: ESBIRROS
    // ==========================================

    private void insertMinions(int characterId, List<Minion> minions) {
        if (minions == null || minions.isEmpty()) return;

        String sql = "INSERT INTO esbirros (character_id, type, name, health, loyalty, dependence, pact_description) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (Minion m : minions) {
                pstmt.setInt(1, characterId);
                pstmt.setString(3, m.getName());
                pstmt.setInt(4, m.getHealth());

                if (m instanceof HumanMinion) {
                    HumanMinion hm = (HumanMinion) m;
                    pstmt.setString(2, "human");
                    pstmt.setString(5, hm.getLoyalty().name());
                    pstmt.setNull(6, java.sql.Types.INTEGER);
                    pstmt.setNull(7, java.sql.Types.VARCHAR);
                } else if (m instanceof GhoulMinion) {
                    GhoulMinion gm = (GhoulMinion) m;
                    pstmt.setString(2, "ghoul");
                    pstmt.setNull(5, java.sql.Types.VARCHAR);
                    pstmt.setInt(6, gm.getDependence());
                    pstmt.setNull(7, java.sql.Types.VARCHAR);
                } else if (m instanceof DemonMinion) {
                    DemonMinion dm = (DemonMinion) m;
                    pstmt.setString(2, "demon");
                    pstmt.setNull(5, java.sql.Types.VARCHAR);
                    pstmt.setNull(6, java.sql.Types.INTEGER);
                    pstmt.setString(7, dm.getPactDescription());
                }

                pstmt.addBatch();
            }

            pstmt.executeBatch();
        } catch (SQLException e) {
            System.err.println("Error al guardar esbirros: " + e.getMessage());
        }
    }

    private List<Minion> loadMinions(int characterId) {
        List<Minion> minions = new ArrayList<>();
        String sql = "SELECT * FROM esbirros WHERE character_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, characterId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("type");
                    String name = rs.getString("name");
                    int health = rs.getInt("health");

                    switch (type) {
                        case "human":
                            Loyalty loyalty = Loyalty.valueOf(rs.getString("loyalty"));
                            minions.add(new HumanMinion(name, health, loyalty));
                            break;
                        case "ghoul":
                            int dependence = rs.getInt("dependence");
                            minions.add(new GhoulMinion(name, health, dependence));
                            break;
                        case "demon":
                            String pact = rs.getString("pact_description");
                            minions.add(new DemonMinion(name, health, pact));
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar esbirros: " + e.getMessage());
        }
        return minions;
    }

    // ==========================================
    // MÉTODOS AUXILIARES PRIVADOS: MODIFICADORES
    // ==========================================

    private void insertModifiers(int characterId, List<Weakness> weaknesses, List<Strength> strengths) {
        String sql = "INSERT INTO modificadores (character_id, type, name, value) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (weaknesses != null) {
                for (Weakness w : weaknesses) {
                    pstmt.setInt(1, characterId);
                    pstmt.setString(2, "weakness");
                    pstmt.setString(3, w.getName());
                    pstmt.setInt(4, w.getValue());
                    pstmt.addBatch();
                }
            }
            if (strengths != null) {
                for (Strength s : strengths) {
                    pstmt.setInt(1, characterId);
                    pstmt.setString(2, "strength");
                    pstmt.setString(3, s.getName());
                    pstmt.setInt(4, s.getValue());
                    pstmt.addBatch();
                }
            }

            pstmt.executeBatch();
        } catch (SQLException e) {
            System.err.println("Error al guardar modificadores: " + e.getMessage());
        }
    }

    private void loadModifiers(int characterId, CharacterBuilder builder) {
        String sql = "SELECT * FROM modificadores WHERE character_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, characterId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("type");
                    String name = rs.getString("name");
                    int value = rs.getInt("value");

                    if ("weakness".equals(type)) {
                        builder.addWeakness(new Weakness(name, value));
                    } else {
                        builder.addStrength(new Strength(name, value));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar modificadores: " + e.getMessage());
        }
    }

    // ==========================================
    // MÉTODO DE LECTURA PRINCIPAL
    // ==========================================

    private Character fetchCharacterByQuery(String sql, int paramId, String paramStr) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (paramStr == null) {
                pstmt.setInt(1, paramId);
            } else {
                pstmt.setString(1, paramStr);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCharacter(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al recuperar personaje: " + e.getMessage());
        }
        return null;
    }

    private Character mapResultSetToCharacter(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String race = rs.getString("race");
        int life = rs.getInt("life");
        int power = rs.getInt("power");
        int gold = rs.getInt("gold");

        // Leer datos de la habilidad especial
        String skillType = rs.getString("special_skill");
        String skillName = rs.getString("skill_name");
        int skillAtk = rs.getInt("skill_attack");
        int skillDef = rs.getInt("skill_defense");
        int skillCost = rs.getInt("skill_cost");

        // Construir la habilidad real
        SpecialSkill skill;
        switch (skillType) {
            case "gift":
                skill = new Gift(skillName, skillCost, skillAtk, skillDef);
                break;
            case "talent":
                skill = new Talent(skillName, skillAtk, skillDef);
                break;
            default: // discipline
                skill = new Discipline(skillName, skillCost, skillAtk, skillDef);
                break;
        }

        // Cargar inventario desde la tabla equipamiento
        Inventory inventory = loadEquipment(id);
        inventory.setGold(gold);

        // Construir el personaje con el builder
        CharacterBuilder builder = new CharacterBuilder()
                .id(id)
                .name(name)
                .life(life)
                .power(power)
                .specialSkill(skill)
                .inventory(inventory);

        // Cargar esbirros desde su tabla
        List<Minion> minions = loadMinions(id);
        for (Minion m : minions) {
            builder.addMinion(m);
        }

        // Cargar modificadores (fortalezas y debilidades) desde su tabla
        loadModifiers(id, builder);

        // Instanciar la subclase correcta y aplicar atributos de raza
        if ("werewolf".equalsIgnoreCase(race)) {
            Werewolf werewolf = new Werewolf(builder);
            int rage = rs.getInt("rage");
            if (!rs.wasNull()) {
                for (int i = 0; i < rage; i++) {
                    werewolf.increaseRage();
                }
            }
            return werewolf;
        } else if ("hunter".equalsIgnoreCase(race)) {
            Hunter hunter = new Hunter(builder);
            int will = rs.getInt("will");
            if (!rs.wasNull()) {
                hunter.setWill(will);
            }
            return hunter;
        } else {
            Vampire vampire = new Vampire(builder);
            int age = rs.getInt("age");
            if (!rs.wasNull()) {
                vampire.setAge(age);
            }
            int bloodPoints = rs.getInt("blood_points");
            if (!rs.wasNull()) {
                vampire.setBloodPoints(bloodPoints);
            }
            return vampire;
        }
    }

    @Override
    public java.util.Map<String, Character> getAllCharactersWithOwners() {
        java.util.Map<String, Character> result = new java.util.HashMap<>();
        String sql = "SELECT p.*, u.nick FROM personajes p JOIN usuarios u ON p.user_id = u.id";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String ownerNick = rs.getString("nick");
                Character character = mapResultSetToCharacter(rs);
                result.put(ownerNick, character);
            }
        } catch (SQLException e) {
            System.err.println("Error al recuperar todos los personajes: " + e.getMessage());
        }
        return result;
    }
    @Override
    public java.util.List<String[]> getEquipmentCatalog() {
        java.util.List<String[]> catalog = new java.util.ArrayList<>();
        String sql = "SELECT * FROM catalogo_equipo";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String[] item = {
                    String.valueOf(rs.getInt("id")),
                    rs.getString("name"),
                    rs.getString("type"),
                    String.valueOf(rs.getInt("attack")),
                    String.valueOf(rs.getInt("defense")),
                    String.valueOf(rs.getInt("is_one_hand"))
                };
                catalog.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error al recuperar el catálogo de equipo: " + e.getMessage());
        }
        return catalog;
    }
}

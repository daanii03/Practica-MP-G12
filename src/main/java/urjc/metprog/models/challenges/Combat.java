package main.java.urjc.metprog.models.combat;

import main.java.urjc.metprog.dao.UserDaoImpl;
import main.java.urjc.metprog.models.challenge.Challenge;
import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.characters.Hunter;
import main.java.urjc.metprog.models.characters.Vampire;
import main.java.urjc.metprog.models.characters.Werewolf;
import main.java.urjc.metprog.models.equipment.Weapon;
import main.java.urjc.metprog.models.minions.Minion;
import main.java.urjc.metprog.models.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Combat {


    private final Challenge challenge;
    private final Character challenger;
    private final Character challenged;
    private final UserDaoImpl userDao;

    private int challengerMinionHealth;
    private int challengedMinionHealth;

    private final List<String> roundLog = new ArrayList<>();
    private int roundCount = 0;
    private User winner;

    private final LocalDateTime combatDate;
    private static final Random RANDOM = new Random();


    public Combat(Challenge challenge, UserDaoImpl userDao) {
        this.challenge  = challenge;
        this.challenger = challenge.getChallenger().getCharacter();
        this.challenged = challenge.getChallenged().getCharacter();
        this.userDao    = userDao;
        this.combatDate = LocalDateTime.now();

        this.challengerMinionHealth = calculateTotalMinionHealth(challenger.getMinions());
        this.challengedMinionHealth = calculateTotalMinionHealth(challenged.getMinions());

        resetCombatStats(challenger);
        resetCombatStats(challenged);
    }



    public CombatResult run() {
        while (challenger.getLife() > 0 && challenged.getLife() > 0) {
            roundCount++;
            playRound();
        }
        CombatResult result = buildResult();
        updateRanking(result);
        challenge.finish();
        return result;
    }

    private void playRound() {
        int challengerAttack  = rollDice(calculateAttackPotential(challenger));
        int challengerDefense = rollDice(calculateDefensePotential(challenger));
        int challengedAttack  = rollDice(calculateAttackPotential(challenged));
        int challengedDefense = rollDice(calculateDefensePotential(challenged));

        boolean challengerHit = challengedAttack >= challengerDefense;
        boolean challengedHit = challengerAttack >= challengedDefense;

        if (challengerHit) applyDamage(challenger);
        if (challengedHit) applyDamage(challenged);

        if (challengedHit && challenger instanceof Vampire) {
            Vampire v = (Vampire) challenger;
            v.setBloodPoints(Math.min(10, v.getBloodPoints() + 4));
        }
        if (challengerHit && challenged instanceof Vampire) {
            Vampire v = (Vampire) challenged;
            v.setBloodPoints(Math.min(10, v.getBloodPoints() + 4));
        }

        logRound(challengerAttack, challengerDefense, challengedAttack, challengedDefense,
                challengerHit, challengedHit);
    }


    private int calculateAttackPotential(Character character) {
        int potential = character.getPower();
        potential += character.getSpecialAttack();
        potential += getWeaponAttackModifier(character);

        if (character instanceof Vampire) {
            if (((Vampire) character).getBloodPoints() >= 5) potential += 2;
        } else if (character instanceof Werewolf) {
            potential += ((Werewolf) character).getRage();
        } else if (character instanceof Hunter) {
            potential += ((Hunter) character).getWill();
        }

        potential += getActiveModifiers(character);
        return Math.max(potential, 0);
    }

    private int calculateDefensePotential(Character character) {
        int potential = character.getPower();
        potential += character.getSpecialDefense();
        potential += getWeaponDefenseModifier(character);
        potential += getArmorDefenseModifier(character);

        if (character instanceof Vampire) {
            if (((Vampire) character).getBloodPoints() >= 5) potential += 2;
        } else if (character instanceof Werewolf) {
            potential += ((Werewolf) character).getRage();
        } else if (character instanceof Hunter) {
            potential += ((Hunter) character).getWill();
        }

        potential += getActiveModifiers(character);
        return Math.max(potential, 0);
    }


    private int getWeaponAttackModifier(Character character) {
        return character.getInventory().getActiveWeapons().stream()
                .mapToInt(Weapon::getAttackModifier)
                .sum();
    }

    private int getWeaponDefenseModifier(Character character) {
        return character.getInventory().getActiveWeapons().stream()
                .mapToInt(Weapon::getDefenseModifier)
                .sum();
    }

    private int getArmorDefenseModifier(Character character) {
        if (character.getInventory().getActiveArmor() == null) return 0;
        return character.getInventory().getActiveArmor().getDefenseModifier();
    }


    private int rollDice(int potential) {
        int successes = 0;
        for (int i = 0; i < potential; i++) {
            int roll = RANDOM.nextInt(6) + 1;
            if (roll >= 5) successes++;
        }
        return successes;
    }


    private void applyDamage(Character character) {
        boolean isChallenger = character == challenger;

        if (isChallenger && challengerMinionHealth > 0) {
            challengerMinionHealth--;
        } else if (!isChallenger && challengedMinionHealth > 0) {
            challengedMinionHealth--;
        } else {
            character.decreaseLife(1);
            updateCombatStats(character);
        }
    }

    private void updateCombatStats(Character character) {
        if (character instanceof Werewolf) ((Werewolf) character).increaseRage();
        if (character instanceof Hunter)   ((Hunter)   character).decreaseWill();
    }


    private int calculateTotalMinionHealth(List<Minion> minions) {
        int total = 0;
        for (Minion minion : minions) {
            total += minion.getHealth();
        }
        return total;
    }


    private int getActiveModifiers(Character character) {
        int total = 0;
        for (var entry : challenge.getActiveModifiers().entrySet()) {
            String modifierName = entry.getKey();
            int modifierValue   = entry.getValue();

            boolean hasStrength = character.getStrengths().stream()
                    .anyMatch(s -> s.getName().equalsIgnoreCase(modifierName));
            boolean hasWeakness = character.getWeaknesses().stream()
                    .anyMatch(w -> w.getName().equalsIgnoreCase(modifierName));

            if (hasStrength) total += modifierValue;
            if (hasWeakness) total -= modifierValue;
        }
        return total;
    }


    private void resetCombatStats(Character character) {
        if (character instanceof Werewolf) ((Werewolf) character).resetRage();
        if (character instanceof Hunter)   ((Hunter)   character).resetWill();
    }


    private void updateRanking(CombatResult result) {
        if (result.isDraw()) return;

        winner = result.getWinnerUser();
        winner.setVictories(winner.getVictories() + 1);
        userDao.updateUser(winner);
    }


    private void logRound(int cAtk, int cDef, int dAtk, int dDef,
                          boolean challengerHit, boolean challengedHit) {
        String log = String.format(
                "Round %d | %s → ATK:%d DEF:%d | %s → ATK:%d DEF:%d | %s took damage: %b | %s took damage: %b",
                roundCount,
                challenger.getName(), cAtk, cDef,
                challenged.getName(), dAtk, dDef,
                challenger.getName(), challengerHit,
                challenged.getName(), challengedHit
        );
        roundLog.add(log);
    }


    private CombatResult buildResult() {
        boolean challengerDead = challenger.getLife() == 0;
        boolean challengedDead = challenged.getLife() == 0;

        if (challengerDead && challengedDead) {
            return new CombatResult(challenge, null, roundCount, combatDate, roundLog,
                    challengerMinionHealth > 0, challengedMinionHealth > 0);
        }

        User winnerUser = challengerDead
                ? challenge.getChallenged()
                : challenge.getChallenger();

        return new CombatResult(challenge, winnerUser, roundCount, combatDate, roundLog,
                challengerMinionHealth > 0, challengedMinionHealth > 0);
    }


    public List<String> getRoundLog()    { return roundLog; }
    public int getRoundCount()           { return roundCount; }
    public User getWinner()              { return winner; }
    public LocalDateTime getCombatDate() { return combatDate; }
}
package test.main.java.urjc.metprog.models.challenges;

import main.java.urjc.metprog.models.challenges.Challenge;
import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.user.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeTest {

    static class StubCharacter extends Character {
        private int gold;

        StubCharacter(int gold) {
            this.gold = gold;
        }

        @Override public int  checkGold()              { return gold; }
        @Override public void increaseGold(int amount) { gold += amount; }
        @Override public void decreaseGold(int amount) { gold -= amount; }

        @Override
        public int getSpecialAttack() {
            return 0;
        }

        @Override
        public int getSpecialDefense() {
            return 0;
        }
    }

    static class StubUser extends User {
        private final String    nick;
        private final Character character;

        StubUser(String nick, Character character) {
            this.nick      = nick;
            this.character = character;
        }

        @Override public String    getNick()      { return nick; }
        @Override public Character getCharacter() { return character; }
    }

    private StubCharacter challengerCharacter;
    private StubCharacter challengedCharacter;
    private StubUser      challenger;
    private StubUser      challenged;

    @BeforeEach
    void setUp() {
        challengerCharacter = new StubCharacter(100);
        challengedCharacter = new StubCharacter(100);
        challenger          = new StubUser("Alice", challengerCharacter);
        challenged          = new StubUser("Bob",   challengedCharacter);
    }

    private Challenge buildChallenge(int goldBet) {
        return new Challenge(challenger, challenged, goldBet);
    }

    @Test
    void constructor_nullChallenger_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> new Challenge(null, challenged, 10));
    }

    @Test
    void constructor_nullChallenged_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> new Challenge(challenger, null, 10));
    }

    @Test
    void constructor_sameUser_throwsIllegalArgument() {
        StubUser sameUser = new StubUser("Alice", challengerCharacter);
        assertThrows(IllegalArgumentException.class,
                () -> new Challenge(challenger, sameUser, 10));
    }

    @Test
    void constructor_negativeGoldBet_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> buildChallenge(-1));
    }

    @Test
    void constructor_goldBetExceedsBalance_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> buildChallenge(200));
    }

    @Test
    void constructor_zeroGoldBet_isAllowed() {
        assertDoesNotThrow(() -> buildChallenge(0));
    }

    @Test
    void constructor_goldBetEqualsBalance_isAllowed() {
        assertDoesNotThrow(() -> buildChallenge(100));
    }

    @Test
    void constructor_validArguments_setsStatusToPendingValidation() {
        Challenge challenge = buildChallenge(50);
        assertEquals(Challenge.Status.PENDING_VALIDATION, challenge.getStatus());
    }

    @Test
    void constructor_validArguments_setsCreatedAtToNow() {
        LocalDateTime before   = LocalDateTime.now().minusSeconds(1);
        Challenge     challenge = buildChallenge(50);
        LocalDateTime after    = LocalDateTime.now().plusSeconds(1);

        assertNotNull(challenge.getCreatedAt());
        assertTrue(challenge.getCreatedAt().isAfter(before));
        assertTrue(challenge.getCreatedAt().isBefore(after));
    }

    @Test
    void getChallenger_returnsCorrectUser() {
        Challenge challenge = buildChallenge(50);
        assertSame(challenger, challenge.getChallenger());
    }

    @Test
    void getChallenged_returnsCorrectUser() {
        Challenge challenge = buildChallenge(50);
        assertSame(challenged, challenge.getChallenged());
    }

    @Test
    void getGoldBet_returnsCorrectAmount() {
        Challenge challenge = buildChallenge(75);
        assertEquals(75, challenge.getGoldBet());
    }

    @Test
    void getStatus_initiallyPendingValidation() {
        Challenge challenge = buildChallenge(50);
        assertEquals(Challenge.Status.PENDING_VALIDATION, challenge.getStatus());
    }

    @Test
    void getCreatedAt_isNotNull() {
        assertNotNull(buildChallenge(50).getCreatedAt());
    }

    @Test
    void getActiveModifiers_initiallyEmpty() {
        Challenge challenge = buildChallenge(50);
        assertNotNull(challenge.getActiveModifiers());
        assertTrue(challenge.getActiveModifiers().isEmpty());
    }

    @Test
    void validate_withModifiers_setsStatusToPublished() {
        Challenge challenge = buildChallenge(50);
        challenge.validate(Map.of("ATK", 10));
        assertEquals(Challenge.Status.PUBLISHED, challenge.getStatus());
    }

    @Test
    void validate_withModifiers_storesModifiers() {
        Challenge challenge = buildChallenge(50);
        Map<String, Integer> modifiers = Map.of("ATK", 10, "DEF", 5);
        challenge.validate(modifiers);
        assertEquals(modifiers, challenge.getActiveModifiers());
    }

    @Test
    void validate_withNullModifiers_setsEmptyMap() {
        Challenge challenge = buildChallenge(50);
        challenge.validate(null);
        assertNotNull(challenge.getActiveModifiers());
        assertTrue(challenge.getActiveModifiers().isEmpty());
    }

    @Test
    void validate_whenAlreadyPublished_throwsIllegalState() {
        Challenge challenge = buildChallenge(50);
        challenge.validate(null);
        assertThrows(IllegalStateException.class, () -> challenge.validate(null));
    }

    @Test
    void accept_whenPublished_setsStatusToAccepted() {
        Challenge challenge = buildChallenge(50);
        challenge.validate(null);
        challenge.accept();
        assertEquals(Challenge.Status.ACCEPTED, challenge.getStatus());
    }

    @Test
    void accept_whenPendingValidation_throwsIllegalState() {
        assertThrows(IllegalStateException.class, () -> buildChallenge(50).accept());
    }

    @Test
    void accept_whenRejected_throwsIllegalState() {
        Challenge challenge = buildChallenge(50);
        challenge.validate(null);
        challenge.reject();
        assertThrows(IllegalStateException.class, challenge::accept);
    }

    @Test
    void reject_whenPublished_setsStatusToRejected() {
        Challenge challenge = buildChallenge(50);
        challenge.validate(null);
        challenge.reject();
        assertEquals(Challenge.Status.REJECTED, challenge.getStatus());
    }

    @Test
    void reject_whenPendingValidation_throwsIllegalState() {
        assertThrows(IllegalStateException.class, () -> buildChallenge(50).reject());
    }

    @Test
    void reject_transfersPenaltyFromChallengedToChallenger() {
        Challenge challenge = buildChallenge(100);
        challenge.validate(null);
        challenge.reject();

        assertEquals(90,  challengedCharacter.checkGold());
        assertEquals(110, challengerCharacter.checkGold());
    }

    @Test
    void reject_whenChallengedHasLessGoldThanPenalty_transfersOnlyAvailableGold() {
        challengedCharacter = new StubCharacter(5);
        challenged          = new StubUser("Bob", challengedCharacter);

        Challenge challenge = new Challenge(challenger, challenged, 100);
        challenge.validate(null);
        challenge.reject();

        assertEquals(0,   challengedCharacter.checkGold());
        assertEquals(105, challengerCharacter.checkGold());
    }

    @Test
    void reject_whenGoldBetIsZero_noPenaltyTransferred() {
        int goldBefore = challengedCharacter.checkGold();
        Challenge challenge = buildChallenge(0);
        challenge.validate(null);
        challenge.reject();

        assertEquals(goldBefore, challengedCharacter.checkGold());
    }

    @Test
    void reject_penaltyRoundsUp() {
        Challenge challenge = buildChallenge(1);
        challenge.validate(null);
        challenge.reject();

        assertEquals(99,  challengedCharacter.checkGold());
        assertEquals(101, challengerCharacter.checkGold());
    }

    @Test
    void finish_setsStatusToFinished() {
        Challenge challenge = buildChallenge(50);
        challenge.validate(null);
        challenge.accept();
        challenge.finish();
        assertEquals(Challenge.Status.FINISHED, challenge.getStatus());
    }

    @Test
    void finish_canBeCalledFromAnyStatus() {
        Challenge challenge = buildChallenge(50);
        challenge.finish();
        assertEquals(Challenge.Status.FINISHED, challenge.getStatus());
    }

    @Test
    void toString_containsChallengerNick() {
        assertTrue(buildChallenge(50).toString().contains("Alice"));
    }

    @Test
    void toString_containsChallengedNick() {
        assertTrue(buildChallenge(50).toString().contains("Bob"));
    }

    @Test
    void toString_containsGoldBet() {
        assertTrue(buildChallenge(50).toString().contains("50"));
    }

    @Test
    void toString_containsStatus() {
        assertTrue(buildChallenge(50).toString().contains("PENDING_VALIDATION"));
    }

    @Test
    void toString_containsCreatedAt() {
        Challenge challenge = buildChallenge(50);
        assertTrue(challenge.toString().contains(challenge.getCreatedAt().toString()));
    }
}
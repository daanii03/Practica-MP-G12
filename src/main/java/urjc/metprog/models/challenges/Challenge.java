package main.java.urjc.metprog.models.challenges;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.user.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Challenge {

    // ── Status ─────────────────────────────────────────────────────────────

    public enum Status {
        PENDING_VALIDATION,
        PUBLISHED,
        ACCEPTED,
        REJECTED,
        FINISHED
    }

    // ── Attributes ─────────────────────────────────────────────────────────

    private final User challenger;
    private final User challenged;
    private final int goldBet;
    private Status status;
    private final LocalDateTime createdAt;
    private Map<String, Integer> activeModifiers;

    // ── Constructor ────────────────────────────────────────────────────────

    public Challenge(User challenger, User challenged, int goldBet) {
        validateChallenge(challenger, challenged, goldBet);
        this.challenger      = challenger;
        this.challenged      = challenged;
        this.goldBet         = goldBet;
        this.status          = Status.PENDING_VALIDATION;
        this.createdAt       = LocalDateTime.now();
        this.activeModifiers = new HashMap<>();
    }

    // ── Validations ────────────────────────────────────────────────────────

    private void validateChallenge(User challenger, User challenged, int goldBet) {
        if (challenger == null || challenged == null) {
            throw new IllegalArgumentException("Users cannot be null.");
        }
        if (challenger.getNick().equals(challenged.getNick())) {
            throw new IllegalArgumentException("A user cannot challenge themselves.");
        }
        if (goldBet < 0) {
            throw new IllegalArgumentException("Gold bet cannot be negative.");
        }
        if (goldBet > challenger.getCharacter().checkGold()) {
            throw new IllegalArgumentException("Challenger does not have enough gold.");
        }
    }

    // ── Getters ────────────────────────────────────────────────────────────

    public User getChallenger()                      { return challenger; }
    public User getChallenged()                      { return challenged; }
    public int getGoldBet()                          { return goldBet; }
    public Status getStatus()                        { return status; }
    public LocalDateTime getCreatedAt()              { return createdAt; }
    public Map<String, Integer> getActiveModifiers() { return activeModifiers; }

    // ── Methods ────────────────────────────────────────────────────────────

    public void validate(Map<String, Integer> modifiers) {
        if (status != Status.PENDING_VALIDATION) {
            throw new IllegalStateException("Challenge is not pending validation.");
        }
        this.activeModifiers = modifiers != null ? modifiers : new HashMap<>();
        this.status = Status.PUBLISHED;
    }

    public void accept() {
        if (status != Status.PUBLISHED) {
            throw new IllegalStateException("Challenge has not been published yet.");
        }
        this.status = Status.ACCEPTED;
    }

    public void reject() {
        if (status != Status.PUBLISHED) {
            throw new IllegalStateException("Challenge has not been published yet.");
        }
        int penalty = (int) Math.ceil(goldBet * 0.1);
        Character challengedCharacter = challenged.getCharacter();
        int actualPenalty = Math.min(penalty, challengedCharacter.checkGold());
        challengedCharacter.decreaseGold(actualPenalty);
        challenger.getCharacter().increaseGold(actualPenalty);
        this.status = Status.REJECTED;
    }

    public void finish() {
        this.status = Status.FINISHED;
    }

    @Override
    public String toString() {
        return String.format("Challenge[%s → %s | gold=%d | status=%s | date=%s]",
                challenger.getNick(), challenged.getNick(), goldBet, status, createdAt);
    }
}
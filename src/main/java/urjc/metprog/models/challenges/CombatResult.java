package main.java.urjc.metprog.models.challenges;

import main.java.urjc.metprog.models.challenges.Challenge;
import main.java.urjc.metprog.models.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class CombatResult {


    private final Challenge challenge;
    private final User winner;           
    private final int rounds;
    private final LocalDateTime date;
    private final List<String> roundLog;
    private final boolean challengerHasSurvivingMinions;
    private final boolean challengedHasSurvivingMinions;
    private final int goldWon;


    public CombatResult(Challenge challenge, User winner, int rounds,
                        LocalDateTime date, List<String> roundLog,
                        boolean challengerHasSurvivingMinions,
                        boolean challengedHasSurvivingMinions) {
        this.challenge  = challenge;
        this.winner     = winner;
        this.rounds     = rounds;
        this.date       = date;
        this.roundLog   = roundLog;
        this.challengerHasSurvivingMinions = challengerHasSurvivingMinions;
        this.challengedHasSurvivingMinions = challengedHasSurvivingMinions;

        this.goldWon = isDraw() ? 0 : challenge.getGoldBet();

        applyGoldTransfer();
    }



    private void applyGoldTransfer() {
        if (isDraw()) return;

        User loser = getLoserUser();
        loser.getCharacter().decreaseGold(goldWon);
        winner.getCharacter().increaseGold(goldWon);
    }


    public boolean isDraw()           { return winner == null; }
    public Challenge getChallenge()   { return challenge; }
    public User getWinner()           { return winner; }
    public int getRounds()            { return rounds; }
    public LocalDateTime getDate()    { return date; }
    public List<String> getRoundLog() { return roundLog; }
    public int getGoldWon()           { return goldWon; }

    public User getWinnerUser() {
        return winner;
    }

    public User getLoserUser() {
        if (isDraw()) return null;
        return winner.getNick().equals(challenge.getChallenger().getNick())
                ? challenge.getChallenged()
                : challenge.getChallenger();
    }

    public boolean challengerHasSurvivingMinions() { return challengerHasSurvivingMinions; }
    public boolean challengedHasSurvivingMinions()  { return challengedHasSurvivingMinions; }
}
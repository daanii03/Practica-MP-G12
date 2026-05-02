package main.java.urjc.metprog.dao;

import main.java.urjc.metprog.models.challenges.Challenge;
import main.java.urjc.metprog.models.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChallengeManager {


    private final List<Challenge> challenges = new ArrayList<>();


    private static ChallengeManager instance;

    private ChallengeManager() {}

    public static ChallengeManager getInstance() {
        if (instance == null) {
            instance = new ChallengeManager();
        }
        return instance;
    }


    public void save(Challenge challenge) {
        if (challenge == null) {
            throw new IllegalArgumentException("Challenge cannot be null.");
        }
        challenges.add(challenge);
    }

    public void delete(Challenge challenge) {
        challenges.remove(challenge);
    }

    public List<Challenge> findAll() {
        return new ArrayList<>(challenges);
    }


    public List<Challenge> findPendingValidation() {
        return challenges.stream()
                .filter(c -> c.getEstado() == Challenge.Estado.PENDIENTE_VALIDACION)
                .collect(Collectors.toList());
    }

    public Optional<Challenge> findPublishedChallengeForUser(User user) {
        return challenges.stream()
                .filter(c -> c.getEstado() == Challenge.Estado.PUBLICADO)
                .filter(c -> c.getChallenged().getNick().equals(user.getNick()))
                .findFirst();
    }

    /*Challenges que ha enviado éste usuario */
    public List<Challenge> findByChallengerNick(String nick) {
        return challenges.stream()
                .filter(c -> c.getChallenger().getNick().equals(nick))
                .collect(Collectors.toList());
    }

    /*Challenges que ha recibido éste usuario */
    public List<Challenge> findByChallengedNick(String nick) {
        return challenges.stream()
                .filter(c -> c.getChallenged().getNick().equals(nick))
                .collect(Collectors.toList());
    }

    public boolean hasActiveChallenge(User user) {
        return challenges.stream()
                .filter(c -> c.getEstado() == Challenge.Estado.PENDIENTE_VALIDACION
                          || c.getEstado() == Challenge.Estado.PUBLICADO)
                .anyMatch(c -> c.getChallenger().getNick().equals(user.getNick())
                            || c.getChallenged().getNick().equals(user.getNick()));
    }
}
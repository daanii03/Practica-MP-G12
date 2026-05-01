package main.java.urjc.metprog.dao;

import main.java.urjc.metprog.models.challenge.Challenge;
import main.java.urjc.metprog.models.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChallengeManager {


    private final List<Desafio> challenges = new ArrayList<>();


    private static ChallengeManager instance;

    private ChallengeManager() {}

    public static ChallengeManager getInstance() {
        if (instance == null) {
            instance = new ChallengeManager();
        }
        return instance;
    }


    public void save(Desafio challenge) {
        if (challenge == null) {
            throw new IllegalArgumentException("Challenge cannot be null.");
        }
        challenges.add(challenge);
    }

    public void delete(Desafio challenge) {
        challenges.remove(challenge);
    }

    public List<Desafio> findAll() {
        return new ArrayList<>(challenges);
    }


    public List<Desafio> findPendingValidation() {
        return challenges.stream()
                .filter(c -> c.getEstado() == Desafio.Estado.PENDIENTE_VALIDACION)
                .collect(Collectors.toList());
    }

    public Optional<Desafio> findPublishedChallengeForUser(Usuario user) {
        return challenges.stream()
                .filter(c -> c.getEstado() == Desafio.Estado.PUBLICADO)
                .filter(c -> c.getChallenged().getNick().equals(user.getNick()))
                .findFirst();
    }

    /*Desafios que ha enviado éste usuario */
    public List<Desafio> findByChallengerNick(String nick) {
        return challenges.stream()
                .filter(c -> c.getChallenger().getNick().equals(nick))
                .collect(Collectors.toList());
    }

    /*Desafios que ha recibido éste usuario */
    public List<Desafio> findByChallengedNick(String nick) {
        return challenges.stream()
                .filter(c -> c.getChallenged().getNick().equals(nick))
                .collect(Collectors.toList());
    }

    public boolean hasActiveChallenge(Usuario user) {
        return challenges.stream()
                .filter(c -> c.getEstado() == Desafio.Estado.PENDIENTE_VALIDACION
                          || c.getEstado() == Desafio.Estado.PUBLICADO)
                .anyMatch(c -> c.getChallenger().getNick().equals(user.getNick())
                            || c.getChallenged().getNick().equals(user.getNick()));
    }
}
package main.java.urjc.metprog.models.challenge;

import main.java.urjc.metprog.models.characters.Character;
import main.java.urjc.metprog.models.users.Usuario;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Challenge {

    // Estado del desafío

    public enum Status {
        PENDING_VALIDATION,  
        PUBLISHED,             
        ACCEPTED,              
        REJECTED              
    }

    private final Usuario challenger;       
    private final Usuario challenged;       
    private final int goldBet;              
    private Status status;
    private final LocalDateTime createdAt;
    private Map<String, Integer> activeModifiers;

    // Constructor

    public Challenge(Usuario challenger, Usuario challenged, int goldBet) {
        validateChallenge(challenger, challenged, goldBet);
        this.challenger      = challenger;
        this.challenged      = challenged;
        this.goldBet         = goldBet;
        this.status          = Status.PENDING_VALIDATION;
        this.createdAt       = LocalDateTime.now();
        this.activeModifiers = new HashMap<>();
    }

    // Validaciones

    private void validateChallenge(Usuario challenger, Usuario challenged, int goldBet) {
        if (challenger == null || challenged == null) {
            throw new IllegalArgumentException("Los usuarios no pueden ser nulos.");
        }
        if (challenger.getNick().equals(challenged.getNick())) {
            throw new IllegalArgumentException("Un usuario no puede desafiarse a sí mismo.");
        }
        if (goldBet < 0) {
            throw new IllegalArgumentException("La apuesta no puede ser negativa.");
        }
        if (goldBet > challenger.getCharacter().checkGold()) {
            throw new IllegalArgumentException("El desafiante no tiene suficiente oro para esa apuesta.");
        }
        if (!challenger.hasActiveEquipment()) {
            throw new IllegalStateException("El desafiante no tiene equipo activo.");
        }
        if (challenger.isBlocked()) {
            throw new IllegalStateException("El desafiante está bloqueado.");
        }
        if (challenger.isWithin24hBlock()) {
            throw new IllegalStateException("El desafiante perdió un combate en las últimas 24h.");
        }
        if (challenged.isWithin24hBlock()) {
            throw new IllegalStateException("El desafiado perdió un combate en las últimas 24h.");
        }
    }

    // Getters 

    public Usuario getChallenger() { 
        return challenger; 
    }
    
    public Usuario getChallenged() { 
        return challenged; 
    }

    public int getGoldBet() { 
        return goldBet; 
    }

    public Status getStatus() { 
        return status; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }

    public Map<String, Integer> getActiveModifiers() { 
        return activeModifiers; 
    }


    public void validate(Map<String, Integer> modifiers) {
        if (status != Status.PENDING_VALIDATION) {
            throw new IllegalStateException("El desafío no está en estado PENDIENTE_VALIDACION.");
        }
        if (!challenged.hasActiveEquipment()) {
            throw new IllegalStateException("El desafiado no tiene equipo activo.");
        }
        if (modifiers != null) {
            this.activeModifiers = modifiers;
        } else {
            this.activeModifiers = new HashMap<>();
        }
        this.status = Status.PUBLISHED;
    }


    public void accept() {
        if (status != Status.PUBLISHED) {
            throw new IllegalStateException("El desafío no está publicado aún.");
        }
        this.status = Status.ACCEPTED;
    }


    public void reject() {
        if (status != Status.PUBLISHED) {
            throw new IllegalStateException("El desafío no está publicado aún.");
        }
        int penalty = (int) Math.ceil(goldBet * 0.1);
        Character challengedCharacter = challenged.getCharacter();
        int availableGold = challengedCharacter.checkGold();

        // Si no tiene suficiente oro, paga lo que pueda
        int actualPenalty = Math.min(penalty, availableGold);
        challengedCharacter.decreaseGold(actualPenalty);
        challenger.getCharacter().increaseGold(actualPenalty);

        this.status = Status.REJECTED;
    }

    @Override
    public String toString() {
        return String.format("Challenge[%s → %s | gold=%d | status=%s | date=%s]",
                challenger.getNick(), challenged.getNick(), goldBet, status, createdAt);
    }
}
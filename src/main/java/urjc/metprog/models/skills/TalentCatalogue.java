package main.java.urjc.metprog.models.skills;

/**
 * Catálogo predefinido de Talentos para los personajes de tipo {@link main.java.urjc.metprog.models.characters.Hunter}.
 * <p>
 * Este enumerado actúa como un registro de habilidades técnicas que los cazadores
 * pueden poseer. Define el nombre y los modificadores de combate (ataque y defensa)
 * que el talento aporta al personaje.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 */

public enum TalentCatalogue {

    /** Talento enfocado en la precisión ofensiva a distancia. */

    DISPARO_CERTERO("Disparo Certero", 2, 1),

    /** Habilidad defensiva técnica diseñada para inmovilizar enemigos. */

    TRAMPA_DE_PLATA("Trampa de Plata", 1, 3),

    /** Talento de alto impacto ofensivo contra criaturas sobrenaturales. */

    ESTACA_BENDITA("Estaca Bendita", 3, 2);

    private final String name;
    private final int attack;
    private final int defense;

    /**
     * Constructor del enumerado para definir las estadísticas del talento.
     *
     * @param name Nombre del talento.
     * @param attack Modificador de puntos de ataque.
     * @param defense Modificador de puntos de defensa.
     */

    TalentCatalogue(String name, int attack, int defense) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
    }

    /**
     * Crea una instancia de {@link Talent} con los parámetros configurados en el catálogo.
     *
     * @return Un nuevo objeto {@link Talent} listo para ser equipado.
     */

    public Talent generateTalent(){
        return new Talent(this.name,this.attack,this.defense);
    }
}

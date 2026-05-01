package main.java.urjc.metprog.models.skills;

/**
 * Catálogo predefinido de Dones (habilidades especiales) para los licántropos.
 * <p>
 * Centraliza la configuración de los dones, definiendo sus costes de rabia y
 * sus efectos en el combate (ataque y defensa). Se utiliza principalmente
 * en las factorías para asignar habilidades de forma aleatoria o predefinida.
 * </p>
 *
 * @author Daniel Marcos Serrano
 * @version 1.0
 */

public enum GiftCatalogue {

    /** Don ofensivo que potencia el daño físico mediante garras. */

    GARRAS_DESGARRADORAS("Garras Desgarradoras", 2, 3, 1),

    /** Don defensivo que endurece la piel del licántropo bajo la influencia lunar. */

    PIEL_DE_LUNA("Piel de Luna",1,1,3),

    /** Habilidad de alto coste que otorga una agresividad máxima en el ataque. */

    FURIA_CIEGA("Furia Ciega",3,3,1);

    private final String name;
    private final int rageCost;
    private final int attack;
    private final int defense;

    /**
     * Constructor del enumerado para inicializar las propiedades del don.
     *
     * @param name Nombre identificativo del don.
     * @param rageCost Cantidad de rabia necesaria para activar el don.
     * @param attack Modificador de ataque que proporciona.
     * @param defense Modificador de defensa que proporciona.
     */

    GiftCatalogue(String name, int rageCost, int attack, int defense){
        this.name = name;
        this.rageCost = rageCost;
        this.attack = attack;
        this.defense = defense;
    }

    /**
     * Instancia un objeto {@link Gift} con los valores definidos en la constante del catálogo.
     *
     * @return Un nuevo objeto {@link Gift} configurado.
     */

    public Gift generateGift(){
        return new Gift(this.name,this.rageCost,this.attack,this.defense);
    }

}

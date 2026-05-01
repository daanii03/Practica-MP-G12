package main.java.urjc.metprog.models.skills;

public enum GiftCatalogue {

    GARRAS_DESGARRADORAS("Garras Desgarradoras", 2, 3, 1),
    PIEL_DE_LUNA("Piel de Luna",1,1,3),
    FURIA_CIEGA("Furia Ciega",3,3,1);

    private final String name;
    private final int rageCost;
    private final int attack;
    private final int defense;

    GiftCatalogue(String name, int rageCost, int attack, int defense){
        this.name = name;
        this.rageCost = rageCost;
        this.attack = attack;
        this.defense = defense;
    }

    public Gift generateGift(){
        return new Gift(this.name,this.rageCost,this.attack,this.defense);
    }

}

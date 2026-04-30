package main.java.urjc.metprog.models.skills;

public enum TalentCatalogue {
    DISPARO_CERTERO("Disparo Certero", 2, 4, 0),
    TRAMPA_DE_PLATA("Trampa de Plata", 1, 1, 3),
    ESTACA_BENDITA("Estaca Bendita", 3, 3, 2);

    private final String name;
    private final int willCost;
    private final int attack;
    private final int defense;

    TalentCatalogue(String name, int willCost, int attack, int defense) {
        this.name = name;
        this.willCost = willCost;
        this.attack = attack;
        this.defense = defense;
    }

    public Talent generateTalent(){
        return new Talent(this.name,this.willCost,this.attack,this.defense);
    }
}

package main.java.urjc.metprog.models.skills;

public enum TalentCatalogue {
    DISPARO_CERTERO("Disparo Certero", 2, 1),
    TRAMPA_DE_PLATA("Trampa de Plata", 1, 3),
    ESTACA_BENDITA("Estaca Bendita", 3, 2);

    private final String name;
    private final int attack;
    private final int defense;

    TalentCatalogue(String name, int attack, int defense) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
    }

    public Talent generateTalent(){
        return new Talent(this.name,this.attack,this.defense);
    }
}

package main.java.urjc.metprog.models.equipment;

public enum WeaponCatalogue {

    ESPADA_CELESTIAL("Espada Celestial",3,1,true),
    DAGA_DE_LAS_SOMBRAS("Daga de las Sombras",2,0, true),
    HACHA_DE_GUERRA_PESADA("Hacha de Guerra Pesada",3,0, false),
    ARCO_DE_CAZADOR("Arco de Cazador",2,1, false),
    CUCHILLO_DE_PLATA("Cuchillo de Plata",3,1, true);

    private String name;
    private int attack;
    private int defense;
    private boolean isOneHand;

    WeaponCatalogue(String name, int attack, int defense,boolean isOneHand){
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.isOneHand = isOneHand;
    }

    public Weapon generateWeapon(){
        return new Weapon(this.name,this.attack,this.defense,this.isOneHand);
    }


}

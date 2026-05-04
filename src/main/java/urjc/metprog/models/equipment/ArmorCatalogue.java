package main.java.urjc.metprog.models.equipment;

public enum ArmorCatalogue {

    ARMADURA_DE_PLACAS_EBANO("Armadura de Placas de Ébano",0,3),
    COTA_DE_MALLA_REFORZADA("Cota de Malla Reforzada",0,2),
    CAPA_DE_INVISIBILIDAD("Capa de Invisibilidad",1,2),
    ESCUDO_DE_ROBLE_ANTIGUO("Escudo de Roble Antiguo",0,2),
    MANTO_DE_SANGRE("Manto de Sangre",2,1);

    private String name;
    private int attack;
    private int defense;


    ArmorCatalogue(String name, int attack, int defense){
        this.name = name;
        this.attack = attack;
        this.defense = defense;
    }

    public Armor generateArmor(){
        return new Armor(this.name,this.attack,this.defense);
    }

}

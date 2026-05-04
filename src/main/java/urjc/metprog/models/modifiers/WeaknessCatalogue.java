package main.java.urjc.metprog.models.modifiers;

public enum WeaknessCatalogue {

    // VAMPIRES
    ANEMIA_SOLAR("Anemia Solar", 5),
    SED_SANGRE("Sed de Sangre", 3),
    AVERSION_PLATA_V("Aversión a la Plata", 4),

    // HUNTERS
    CONSTITUCION_HUMANA("Constitución Humana", 2),
    DEPENDENCIA_EQUIPO("Dependencia de Equipo", 4),
    FATIGA_COMBATE("Fatiga de Combate", 3),

    // WEREWOLF
    INESTABILIDAD_LUNAR("Inestabilidad Lunar", 3),
    IMPULSO_SALVAJE("Impulso Salvaje", 2),
    VULNERABILIDAD_PLATA_L("Vulnerabilidad de Plata", 5);

    private final String name;
    private final int value;

    WeaknessCatalogue(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public Weakness generateWeakness(){
        return new Weakness(this.name,this.value);
    }

}

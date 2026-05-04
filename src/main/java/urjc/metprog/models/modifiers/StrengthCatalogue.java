package main.java.urjc.metprog.models.modifiers;

public enum StrengthCatalogue {

    // VAMPIRES
    DRENAJE_VITAL("Drenaje Vital", 4),
    VELOCIDAD_IMPIA("Velocidad Impía", 5),
    DOMINACION_MENTAL("Dominación Mental", 3),

    // HUNTERS
    ARSENAL_SAGRADO("Arsenal Sagrado", 5),
    SENTIDO_RASTREADOR("Sentido del Rastreador", 2),
    ADRENALINA_SUPERVIVENCIA("Adrenalina de Supervivencia", 4),

    // WEREWOLF
    FURIA_MANADA("Furia de Manada", 3),
    REGENERACION_FERAL("Regeneración Feral", 5),
    SALTO_BESTIA("Salto de la Bestia", 4);

    private final String name;
    private final int value;

    StrengthCatalogue(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public Strength generateStrength(){
        return new Strength(this.name, this.value);
    }

}

package Characters;

public abstract class Character {
    private String name;
    private Weapon[] activeWeapons;
    private Armor activeArmor;
    private int life;
    private int power;
    private int id;
    private SpecialSkill specialSkill;
    private Inventory inventory;


    // Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setActiveArmor(Armor activeArmor) {
        this.activeArmor = activeArmor;
    }

    public void setActiveWeapons(Weapon[] activeWeapons) {
        this.activeWeapons = activeWeapons;
    }

    public void setLife(int life){
        if(life >= 0 && life <= 5){
            this.life = life;
        }
        else{
            throw new IllegalArgumentException("La vida debe estar entre 0 y 5");
        }
    }

    public void setPower(int power){
        if(power >= 0 && power <= 5){
            this.power = power;
        }
        else {
            throw new IllegalArgumentException("El poder debe estar entre 0 y 5");
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getters
    public String getName(){
        return this.name;
    }

    public Weapon[] getActiveWeapons(){
        return this.activeWeapons;
    }
    public Armor getActiveArmor(){
        return this.activeArmor;
    }

    public int getLife(){
        return this.life;
    }

    public int getPower(){
        return this.power;
    }

    public int getId(){
        return this.id;
    }

    public void increaseLife(int incrementLife){
        if(this.life + incrementLife <= 5){
            this.life += incrementLife;
        }
        else{
            throw new RuntimeException("La vida no puede tener un valor mayor a 5.");
        }
    }

    public void decreaseLife(int decreaseLife){
        if(this.life - decreaseLife >= 0){
            this.life -= decreaseLife;
        }
        else{
            throw new RuntimeException("La vida no puede tener un valor menor a 0");
        }
    }

    public void increaseGold(int gold){
       this.inventory.addGold(gold);
    }

    public void decreaseGold(int gold){
        this.inventory.decreaseGold(gold);
    }

    public void increasePower(int power){
        if (this.power + power <= 5){
            this.power += power;
        }
        else {
            throw new RuntimeException("El poder no puede ser mayor a 5.");
        }
    }

    public void decreasePower(int power){
        if (this.power - power >= 0){
            this.power -= power;

        }
        else {
            throw new RuntimeException("El poder no puede ser menor a 5.");
        }
    }
}

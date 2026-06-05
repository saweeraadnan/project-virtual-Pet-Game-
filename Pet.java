package virtualpet;

public class Pet {

    private String name;
    private String species;
    private int hunger;
    private int happiness;
    private int energy;
    private int health;
    private int age;
    private boolean alive;

    public Pet(String name, String species) {
        this.name      = name;
        this.species   = species;
        this.hunger    = 30;
        this.happiness = 70;
        this.energy    = 80;
        this.health    = 100;
        this.age       = 0;
        this.alive     = true;
    }

    public String feed() {
        if (!alive) return name + " is no longer with us...";
        if (hunger <= 5) return name + " is already full!";
        hunger    = Math.max(0, hunger - 30);
        happiness = Math.min(100, happiness + 10);
        health    = Math.min(100, health + 5);
        return name + " enjoyed the meal! ";
    }

    public String play() {
        if (!alive) return name + " is no longer with us...";
        if (energy < 20) return name + " is too tired to play!";
        happiness = Math.min(100, happiness + 25);
        energy    = Math.max(0,   energy    - 20);
        hunger    = Math.min(100, hunger    + 15);
        return name + " had a great time playing!";
    }

    public String sleep() {
        if (!alive) return name + " is no longer with us...";
        if (energy >= 90) return name + " is not sleepy right now!";
        energy    = Math.min(100, energy    + 40);
        hunger    = Math.min(100, hunger    + 10);
        happiness = Math.min(100, happiness + 5);
        return name + " took a nap and feels refreshed!";
    }

    public String giveMedicine() {
        if (!alive) return name + " is no longer with us...";
        if (health >= 90) return name + " is already healthy!";
        health    = Math.min(100, health    + 30);
        happiness = Math.max(0,   happiness - 10);
        return name + " took the medicine. Health improving!";
    }

    public void tick() {
        if (!alive) return;
        hunger    = Math.min(100, hunger    + 3);
        happiness = Math.max(0,   happiness - 2);
        energy    = Math.max(0,   energy    - 1);
        if (hunger > 80)    health = Math.max(0, health - 3);
        if (happiness < 20) health = Math.max(0, health - 2);
        age++;
        if (health <= 0) alive = false;
    }

    public String getMood() {
        if (!alive)          return "Gone";
        if (happiness >= 80) return "Ecstatic";
        if (happiness >= 60) return "Happy";
        if (happiness >= 40) return "Okay";
        if (happiness >= 20) return "Sad";
        return                      "Miserable";
    }

    public String getPetEmoji() {
        if (!alive) return "X";
        if (species.equalsIgnoreCase("cat"))   return happiness >= 60 ? ":3" : ":C";
        if (species.equalsIgnoreCase("dog"))   return happiness >= 60 ? ":D" : ":|";
        if (species.equalsIgnoreCase("bunny")) return happiness >= 60 ? "^_^" : "T_T";
        return "?";
    }

    public String getName()     { return name; }
    public String getSpecies()  { return species; }
    public int    getHunger()   { return hunger; }
    public int    getHappiness(){ return happiness; }
    public int    getEnergy()   { return energy; }
    public int    getHealth()   { return health; }
    public int    getAge()      { return age; }
    public boolean isAlive()    { return alive; }
}
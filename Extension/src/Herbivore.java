public class Herbivore extends Organism {

    private int hunger;

    public Herbivore(int age, int hunger) {
        super(age);
        this.hunger = hunger;
    }

    public int getHunger() {
        return this.hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public void incrementHunger() {
        this.hunger += 1;
    }

    public void eatDaisy() {
        this.hunger -= 25;
    }

    public String toString() {
        return "H";
    }
}
